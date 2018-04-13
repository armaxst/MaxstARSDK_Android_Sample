package com.maxst.ar.sample.arobject;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.renderscript.Float3;
import android.renderscript.Float4;
import android.util.Log;

import com.maxst.ar.GuideInfo;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.content.ContentValues.TAG;

/**
 * Created by Acper on 2018. 3. 12..
 */

public class FeaturePoint {
    Bitmap blueImage;
    Bitmap redImage;

    private static final String VERTEX_SHADER_SRC =
    "attribute vec4 a_position;\n"                                +
    "uniform mat4 u_mvpMatrix;\n"                                +
    "attribute vec2 a_vertexTexCoord;\n"                    +
    "varying vec2 v_texCoord;\n"                        +
    "void main()\n"                                                    +
    "{\n"                                                            +
    "    gl_Position = u_mvpMatrix  * a_position;\n"                    +
    "    v_texCoord = a_vertexTexCoord;             \n"        +
    "}\n";

    private static final String FRAGMENT_SHADER_SRC =
    "precision mediump float;\n" +
    "uniform sampler2D u_texture_1;\n"                            +
    "varying vec2 v_texCoord;\n"                                +
    "void main()\n"                                    +
    "{\n"                                            +
    "    vec4 val = texture2D(u_texture_1, v_texCoord.xy);\n"                                            +
    "    if(val.a == 0.0) { \n"                                            +
    "      discard;\n"                                            +
    "    } else {\n"    +
    "      gl_FragColor = texture2D(u_texture_1, v_texCoord.xy);\n"    +
    "    }\n"    +
    "}\n";

    boolean trackingState = false;
    final int maxFeatureCount = 2000;

    float[] vertices;
    short[] indices;
    float[] textureCoords;

    int vertexCount;
    int indexCount;
    int texCoordCount;

    int[] vertexBufferObject = new int[4];
    int program;
    int positionHandle = -1;
    int textureCoordHandle = -1;
    int mvpMatrixHandle = -1;
    int textureCount;
    int[] textureIds = {-1, -1};
    int[] textureHandles = {-1, -1};

    float[] modelMatrix = new float[4 * 4];
    float[] localMVPMatrix = new float[4 * 4];
    float[] projectionMatrix = new float[4 * 4];

    float[] vertexSample = new float[4 * 3];
    float[] texCoordSample = new float[4 * 2];
    short[] indexSample = new short[3 * 2];

    float[] VERTEX_BUF = new float[maxFeatureCount * 4 * 3];
    FloatBuffer VERTEX_Buffer;
    float[] TEX_COORD_BUF = new float[maxFeatureCount * 4 * 2];
    FloatBuffer TEX_COORD_Buffer;
    short[] INDEX_BUF = new short[maxFeatureCount * 3 * 2];
    ShortBuffer INDEX_Buffer;

    Float3 featureVertex = new Float3();

    float[] VERTEX_SAMPLE = {
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
    };

    float[] TEXTURE_SAMPLE = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    short[] INDEX_SAMPLE = {
            0, 1, 2, 2, 3, 0
    };

    final float featureSize = 0.01f;

    public FeaturePoint() {
        textureCount = 2;

        vertices = VERTEX_BUF;
        textureCoords = TEX_COORD_BUF;
        indices = INDEX_BUF;

        vertexCount = VERTEX_SAMPLE.length;
        texCoordCount = TEXTURE_SAMPLE.length;
        indexCount = INDEX_SAMPLE.length;

        ByteBuffer bb = ByteBuffer.allocateDirect(VERTEX_BUF.length * Float.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        VERTEX_Buffer = bb.asFloatBuffer();
        VERTEX_Buffer.put(VERTEX_BUF);
        VERTEX_Buffer.position(0);

        bb = ByteBuffer.allocateDirect(INDEX_BUF.length * Short.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        INDEX_Buffer = bb.asShortBuffer();
        INDEX_Buffer.put(INDEX_BUF);
        INDEX_Buffer.position(0);

        bb = ByteBuffer.allocateDirect(TEX_COORD_BUF.length * Float.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        TEX_COORD_Buffer = bb.asFloatBuffer();
        TEX_COORD_Buffer.put(TEX_COORD_BUF);
        TEX_COORD_Buffer.position(0);

        program = ShaderUtil.createProgram(VERTEX_SHADER_SRC, FRAGMENT_SHADER_SRC);

        positionHandle = GLES20.glGetAttribLocation(program, "a_position");
        textureCoordHandle = GLES20.glGetAttribLocation(program, "a_vertexTexCoord");
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "u_mvpMatrix");

        GLES20.glGenTextures(textureCount, textureIds, 0);
        for (int i = 0; i < textureCount; i++)
        {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[i]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            String textureName = "u_texture_" + (i + 1);
            textureHandles[i] = GLES20.glGetUniformLocation(program, textureName);
        }

        initVBO();
    }

    public void initVBO() {
        GLES20.glGenBuffers(4, vertexBufferObject, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, VERTEX_BUF.length * Float.SIZE / 8, VERTEX_Buffer, GLES20.GL_DYNAMIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[1]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, TEX_COORD_BUF.length * Float.SIZE / 8, TEX_COORD_Buffer, GLES20.GL_DYNAMIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vertexBufferObject[3]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, INDEX_BUF.length * Short.SIZE / 8, INDEX_Buffer, GLES20.GL_DYNAMIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public void setFeatureImage(Bitmap blueImage, Bitmap redImage) {
        this.blueImage = blueImage;
        this.redImage = redImage;

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);

        if (blueImage != null) {
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, blueImage, 0);
            blueImage.recycle();
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[1]);

        if (redImage != null) {
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, redImage, 0);
            redImage.recycle();
        }
    }

    public void setTrackState(boolean tracked) {
        this.trackingState = tracked;
    }

    public void draw(GuideInfo guide, float [] projectionMatrix) {
        int featureCount = guide.getGuideFeatureCount();
        if(featureCount == 0) {
            return;
        }

        float[] featureBuffer = guide.getGuideFeatureBuffer();

        System.arraycopy(VERTEX_SAMPLE,0, vertexSample,0, VERTEX_SAMPLE.length);
        System.arraycopy(TEXTURE_SAMPLE,0, texCoordSample,0, TEXTURE_SAMPLE.length);
        System.arraycopy(INDEX_SAMPLE,0, indexSample,0, INDEX_SAMPLE.length);

        float[] vertexPtr = vertices;
        float[] texturePtr = textureCoords;
        short[] indexPtr = indices;

        for(int i=0; i < featureCount; i++) {
            featureVertex.x = featureBuffer[i*3 + 0];
            featureVertex.y = featureBuffer[i*3 + 1];
            featureVertex.z = featureBuffer[i*3 + 2];
            convertFeatureToPlane(featureVertex, vertexSample, indexSample, i);
            System.arraycopy(vertexSample, 0, vertexPtr,vertexCount * i, VERTEX_SAMPLE.length);
            System.arraycopy(texCoordSample,0, texturePtr,texCoordCount * i, TEXTURE_SAMPLE.length);
            System.arraycopy(indexSample,0, indexPtr,indexCount * i, INDEX_SAMPLE.length);
        }

        VERTEX_Buffer.put(vertexPtr);
        VERTEX_Buffer.position(0);

        INDEX_Buffer.put(indexPtr);
        INDEX_Buffer.position(0);

        TEX_COORD_Buffer.put(texturePtr);
        TEX_COORD_Buffer.position(0);

        GLES20.glUseProgram(program);

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glEnableVertexAttribArray(textureCoordHandle);

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, VERTEX_SAMPLE.length * featureCount * Float.SIZE / 8, VERTEX_Buffer);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[1]);
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, TEXTURE_SAMPLE.length * featureCount * Float.SIZE / 8, TEX_COORD_Buffer);
        GLES20.glVertexAttribPointer(textureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, 0);

        localMVPMatrix = projectionMatrix;
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, localMVPMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(textureHandles[0], 0);

        if (this.trackingState)
        {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
        }
        else
        {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[1]);
        }

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vertexBufferObject[3]);
        GLES20.glBufferSubData(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0, INDEX_SAMPLE.length * featureCount * Short.SIZE / 8, INDEX_Buffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount * featureCount, GLES20.GL_UNSIGNED_SHORT, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        GLES20.glDisable(GLES20.GL_BLEND);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20. glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(textureCoordHandle);

        GLES20.glUseProgram(0);
        //Log.d("test", "vertex count " + VERTEX_SAMPLE.length * featureCount * 4);
        //Log.d("test", "texture count " + TEXTURE_SAMPLE.length * featureCount * 4);
        //Log.d("test", "index count " + INDEX_SAMPLE.length * featureCount * 2);
    }

    private void convertFeatureToPlane(Float3 srcVertex, float[] dstPlane, short[] index, int count) {
        float x = srcVertex.x;
        float y = srcVertex.y;
        float z = srcVertex.z;

        dstPlane[0] = x - featureSize;
        dstPlane[1] = y + featureSize;
        dstPlane[2] = z;

        dstPlane[3] = x - featureSize;
        dstPlane[4] = y - featureSize;
        dstPlane[5] = z;

        dstPlane[6] = x + featureSize;
        dstPlane[7] = y - featureSize;
        dstPlane[8] = z;

        dstPlane[9] = x + featureSize;
        dstPlane[10] = y + featureSize;
        dstPlane[11] = z;

        if(count == 0) {
            return;
        }

        for (int i = 0; i < 6; i++)
        {
            index[i] += 4;
        }
    }
}
