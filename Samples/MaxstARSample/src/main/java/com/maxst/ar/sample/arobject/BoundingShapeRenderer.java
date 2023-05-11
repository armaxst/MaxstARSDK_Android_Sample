/*
 * Copyright 2016 Maxst, Inc. All Rights Reserved.
 */
package com.maxst.ar.sample.arobject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.maxst.ar.sample.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

public class BoundingShapeRenderer extends BaseRenderer {
    private static final String VERTEX_SHADER_SRC =
            "attribute vec4 a_position;\n" +
                    "attribute vec4 a_color;\n" +
                    "uniform mat4 u_mvpMatrix;\n" +
                    "varying vec4 v_color;\n" +
                    "void main()\n" +
                    "{\n" +
                    "	gl_Position = u_mvpMatrix  * a_position;\n" +
                    "	v_color = a_color;\n" +
                    "}\n";

    private static final String FRAGMENT_SHADER_SRC =
            "precision mediump float;\n" +
                    "varying vec4 v_color;\n" +
                    "void main()\n" +
                    "{\n" +
                    "	gl_FragColor = v_color;\n" +
                    "}\n";


    private final static float center_height = -0.2f;
    private final static float line_width = 0.015f;
    private final static float line_diagonal_width = line_width * 0.707f;

    private final static float vertices_data[] =
    {
        0.2f, 0.5f,	0.5f,	    // V0
        0.5f, 0.2f,	0.5f,	    // V1
        0.5f, -0.2f, 0.5f,		// V2
        0.2f, -0.5f, 0.5f, 	    // V3
        -0.2f, -0.5f, 0.5f,	    // V4
        -0.5f, -0.2f, 0.5f,	    // V5
        -0.5f, 0.2f, 0.5f,		// V6
        -0.2f, 0.5f, 0.5f,		// V7

        0.2f, 0.5f,	center_height,  	// V8
        0.5f, 0.2f,	center_height,  	// V9
        0.5f, -0.2f, center_height,	// V10
        0.2f, -0.5f, center_height,	// V11
        -0.2f, -0.5f, center_height,	// V12
        -0.5f, -0.2f, center_height, 	// V13
        -0.5f, 0.2f, center_height,	// V14
        -0.2f, 0.5f, center_height,	// V15

        0.1f, 0.25f, -0.5f,		// V16
        0.25f, 0.1f, -0.5f,		// V17
        0.25f, -0.1f, -0.5f,	// V18
        0.1f, -0.25f, -0.5f,	// V19
        -0.1f, -0.25f, -0.5f,	// V20
        -0.25f, -0.1f, -0.5f,	// V21
        -0.25f, 0.1f, -0.5f,	// V22
        -0.1f, 0.25f, -0.5f,	// V23

        0.2f, 0.5f,	0.5f+line_width,	    // V24
        0.5f, 0.2f,	0.5f+line_width,	    // V25
        0.5f, -0.2f, 0.5f+line_width,		// V26
        0.2f, -0.5f, 0.5f+line_width, 	// V27
        -0.2f, -0.5f, 0.5f+line_width,	// V28
        -0.5f, -0.2f, 0.5f+line_width,	// V29
        -0.5f, 0.2f, 0.5f+line_width,		// V30
        -0.2f, 0.5f, 0.5f+line_width,		// V31

        0.2f, 0.5f,	center_height+line_width,  	// V32
        0.5f, 0.2f,	center_height+line_width,  	// V33
        0.5f, -0.2f, center_height+line_width,	// V34
        0.2f, -0.5f, center_height+line_width,	// V35
        -0.2f, -0.5f, center_height+line_width,	// V36
        -0.5f, -0.2f, center_height+line_width, // V37
        -0.5f, 0.2f, center_height+line_width,	// V38
        -0.2f, 0.5f, center_height+line_width,	// V39

         0.1f, 0.25f, -0.5f+line_width,		// V40
         0.25f, 0.1f, -0.5f+line_width,		// V41
         0.25f, -0.1f, -0.5f+line_width,	// V42
         0.1f, -0.25f, -0.5f+line_width,	// V43
         -0.1f, -0.25f, -0.5f+line_width,	// V44
         -0.25f, -0.1f, -0.5f+line_width,	// V45
         -0.25f, 0.1f, -0.5f+line_width,	// V46
         -0.1f, 0.25f, -0.5f+line_width,	// V47

            0.2f+line_diagonal_width, 0.5f-line_diagonal_width,	0.5f,	    // V48
            0.5f, 0.2f-line_diagonal_width,	0.5f,	                            // V49
            0.5f-line_diagonal_width, -0.2f-line_diagonal_width, 0.5f,		// V50
            0.2f-line_diagonal_width, -0.5f, 0.5f, 	                            // V51
            -0.2f-line_diagonal_width, -0.5f+line_diagonal_width, 0.5f,	    // V52
            -0.5f, -0.2f+line_diagonal_width, 0.5f,	                            // V53
            -0.5f+line_diagonal_width, 0.2f+line_diagonal_width, 0.5f,		// V54
            -0.2f+line_diagonal_width, 0.5f, 0.5f,		                        // V55

            0.2f+line_diagonal_width, 0.5f-line_diagonal_width, center_height,	    // V56
            0.5f, 0.2f-line_diagonal_width,	center_height,	                            // V57
            0.5f-line_diagonal_width, -0.2f-line_diagonal_width, center_height,		// V58
            0.2f-line_diagonal_width, -0.5f, center_height, 	                        // V59
            -0.2f-line_diagonal_width, -0.5f+line_diagonal_width, center_height,	 // V60
            -0.5f, -0.2f+line_diagonal_width, center_height,	                        // V61
            -0.5f+line_diagonal_width, 0.2f+line_diagonal_width, center_height,		// V62
            -0.2f+line_diagonal_width, 0.5f, center_height,		                    // V63

            0.1f+line_diagonal_width, 0.25f-line_diagonal_width, -0.5f,		        // V64
            0.25f, 0.1f-line_diagonal_width, -0.5f,		                                // V65
            0.25f-line_diagonal_width, -0.1f-line_diagonal_width, -0.5f,	            // V66
            0.1f-line_diagonal_width, -0.25f, -0.5f,	                                    // V67
            -0.1f-line_diagonal_width, -0.25f+line_diagonal_width, -0.5f,	            // V68
            -0.25f, -0.1f+line_diagonal_width, -0.5f,	                                // V69
            -0.25f+line_diagonal_width, 0.1f+line_diagonal_width, -0.5f,	            // V70
            -0.1f+line_diagonal_width, 0.25f, -0.5f,	                                    // V71
    };
    private static final float face_color_data[] = {
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,

        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,

        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
        1.0f, 1.0f, 0.0f, 0.5f,
    };

    private static final float shape_color_data[] = {
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
    };

    private final static short indices_shape[] = {
            0,1,25,
            25,24,0,
            1,2,26,
            26,25,1,
            2,3,27,
            27,26,2,
            3,4,28,
            28,27,3,
            4,5,29,
            29,28,4,
            5,6,30,
            30,29,5,
            6,7,31,
            31,30,6,
            7,0,24,
            24,31,7,

            8,9,33,
            33,32,8,
            9,10,34,
            34,33,9,
            10,11,35,
            35,34,10,
            11,12,36,
            36,35,11,
            12,13,37,
            37,36,12,
            13,14,38,
            38,37,13,
            14,15,39,
            39,38,14,
            15,8,32,
            32,39,15,

            16,17,41,
            41,40,16,
            17,18,42,
            42,41,17,
            18,19,43,
            43,42,18,
            19,20,44,
            44,43,19,
            20,21,45,
            45,44,20,
            21,22,46,
            46,45,21,
            22,23,47,
            47,46,22,
            23,16,40,
            40,47,23,

            8,0,48,
            48,56,8,
            9,1,49,
            49,57,9,
            10,2,50,
            50,58,10,
            11,3,51,
            51,59,11,
            12,4,52,
            52,60,12,
            13,5,53,
            53,61,13,
            14,6,54,
            54,62,14,
            15,7,55,
            55,63,15,

            16,8,56,
            56,64,16,
            17,9,57,
            57,65,17,
            18,10,58,
            58,66,18,
            19,11,59,
            59,67,19,
            20,12,60,
            60,68,20,
            21,13,61,
            61,69,21,
            22,14,62,
            62,70,22,
            23,15,63,
            63,71,23,
    };

    private final static short indices_mesh[] = {
            0, 1, 8,	// 0
            1, 9, 8,
            1, 2, 9,	// 1
            2, 10, 9,
            2, 3, 10,	// 2
            3, 11, 10,
            3, 4, 11,	// 3
            4, 12, 11,
            4, 5, 12,	// 4
            5, 13, 12,
            5, 6, 13,	// 5
            6, 14, 13,
            6, 7, 14,	// 6
            7, 15, 14,
            7, 0, 15,	// 7
            0, 8, 15,

            8, 9, 16,	// 8
            9, 17, 16,
            9, 10, 17,	// 9
            10, 18, 17,
            10, 11, 18,	// 10
            11, 19, 18,
            11, 12, 19,	// 11
            12, 20, 19,
            12, 13, 20,	// 12
            13, 21, 20,
            13, 14, 21,	// 13
            14, 22, 21,
            14, 15, 22,	// 14
            15, 23, 22,
            15, 8, 23,	// 15
            8, 16, 23,

            18, 19, 20,	// 16
            18, 20, 21,
            18, 21, 22,
            18, 22, 23,
            18, 23, 16,
            18, 16, 17,
    };

    protected Vector<Integer> faces;
    private static short[] FACE_INDEX_BUF = new short[indices_mesh.length];
    protected ShortBuffer faceIndexBuffer;
    protected FloatBuffer faceColorBuffer;

    public BoundingShapeRenderer() {
        super();

        ByteBuffer bb = ByteBuffer.allocateDirect(vertices_data.length * Float.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices_data);
        vertexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(indices_shape.length * Short.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        indexBuffer = bb.asShortBuffer();
        indexBuffer.put(indices_shape);
        indexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(shape_color_data.length * Float.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        colorBuffer = bb.asFloatBuffer();
        colorBuffer.put(shape_color_data);
        colorBuffer.position(0);

        bb = ByteBuffer.allocateDirect(face_color_data.length * Float.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        faceColorBuffer = bb.asFloatBuffer();
        faceColorBuffer.put(face_color_data);
        faceColorBuffer.position(0);

        shaderProgramId = ShaderUtil.createProgram(VERTEX_SHADER_SRC, FRAGMENT_SHADER_SRC);

        positionHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_position");
        colorHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_color");
        mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgramId, "u_mvpMatrix");


        faces = new Vector<Integer>();
        bb = ByteBuffer.allocateDirect(indices_mesh.length * Short.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        faceIndexBuffer = bb.asShortBuffer();
        faceIndexBuffer.position(0);
    }

    @Override
    public void draw() {
        // draw frame
        GLES20.glUseProgram(shaderProgramId);

        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
        GLES20.glEnableVertexAttribArray(colorHandle);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(modelMatrix, 0, translation, 0, rotation, 0);
        Matrix.multiplyMM(modelMatrix, 0, modelMatrix, 0, scale, 0);
        Matrix.multiplyMM(modelMatrix, 0, transform, 0, modelMatrix, 0);

        Matrix.multiplyMM(localMvpMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, localMvpMatrix, 0);

        //GLES20.glDrawElements(GLES20.GL_LINES, indices_line.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices_shape.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);

        // draw face
        String msg = "";
        int faceCount = faces.size();
        msg += "count " + faceCount + " : ";
        int iindex = 0;
        for(int i=1; i<faceCount; i++) {
            int face = faces.elementAt(i);
            msg += face + ",";

            if(face < 16)
            {
                FACE_INDEX_BUF[iindex++] = indices_mesh[face * 2 * 3 + 0];
                FACE_INDEX_BUF[iindex++] = indices_mesh[face * 2 * 3 + 1];
                FACE_INDEX_BUF[iindex++] = indices_mesh[face * 2 * 3 + 2];
                FACE_INDEX_BUF[iindex++] = indices_mesh[face * 2 * 3 + 3];
                FACE_INDEX_BUF[iindex++] = indices_mesh[face * 2 * 3 + 4];
                FACE_INDEX_BUF[iindex++] = indices_mesh[face * 2 * 3 + 5];
            }
            else
            {
                for(int j=32; j<38; j++)
                {
                    FACE_INDEX_BUF[iindex++] = indices_mesh[j * 3 + 0];
                    FACE_INDEX_BUF[iindex++] = indices_mesh[j * 3 + 1];
                    FACE_INDEX_BUF[iindex++] = indices_mesh[j * 3 + 2];
                }
            }
        }

        if(faceCount > 0)
        {
            faceIndexBuffer.put(FACE_INDEX_BUF);
            faceIndexBuffer.position(0);
            GLES20.glUseProgram(shaderProgramId);

            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
            GLES20.glEnableVertexAttribArray(positionHandle);

            GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, faceColorBuffer);
            GLES20.glEnableVertexAttribArray(colorHandle);

            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, translation, 0, rotation, 0);
            Matrix.multiplyMM(modelMatrix, 0, modelMatrix, 0, scale, 0);
            Matrix.multiplyMM(modelMatrix, 0, transform, 0, modelMatrix, 0);

            Matrix.multiplyMM(localMvpMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
            GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, localMvpMatrix, 0);

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, iindex, GLES20.GL_UNSIGNED_SHORT, faceIndexBuffer);

            GLES20.glDisableVertexAttribArray(positionHandle);
            GLES20.glDisableVertexAttribArray(colorHandle);
        }
    }

    public void clearFace() {
        faces.clear();
    }

    public void addFace(int face) {
        faces.add(face);
    }
}
