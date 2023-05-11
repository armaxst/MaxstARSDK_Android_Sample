/*
 * Copyright 2016 Maxst, Inc. All Rights Reserved.
 */
package com.maxst.ar.sample.arobject;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.renderscript.Float3;
import android.util.Log;

import com.maxst.ar.GuideInfo;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.sample.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.content.ContentValues.TAG;

public class FeaturePointRenderer extends BaseRenderer {

	private static final String VERTEX_SHADER_SRC =
			"attribute vec4 a_position;\n" +
					"uniform mat4 u_mvpMatrix;\n" +
					"attribute vec2 a_vertexTexCoord;\n" +
					"varying vec2 v_texCoord;\n" +
					"void main()\n" +
					"{\n" +
					"    gl_Position = u_mvpMatrix  * a_position;\n" +
					"    v_texCoord = a_vertexTexCoord;             \n" +
					"}\n";

	private static final String FRAGMENT_SHADER_SRC =
			"precision mediump float;\n" +
					"uniform sampler2D u_texture;\n" +
					"varying vec2 v_texCoord;\n" +
					"void main()\n" +
					"{\n" +
					"    gl_FragColor = texture2D(u_texture, v_texCoord.xy);\n" +
					"}\n";

	private static final int MAX_FEATURE_COUNT = 2000;
	private float[] featureVertexBuf = new float[MAX_FEATURE_COUNT * 4 * 3];
	private float[] featureTexCoordBuf = new float[MAX_FEATURE_COUNT * 4 * 2];
	private short[] featureIndexBuf = new short[MAX_FEATURE_COUNT * 3 * 2];

	private int[] vertexBufferObject = new int[3];

	private static final float[] TEX_COORD_BUFFER = {
			0.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f,
	};

	private static final short[] INDEX_BUFFER = {
			0, 1, 2, 2, 3, 0
	};

	private static final float POINT_SIZE = 0.01f;

	public FeaturePointRenderer() {
		ByteBuffer bb = ByteBuffer.allocateDirect(featureVertexBuf.length * Float.SIZE / 8);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(featureVertexBuf);
		vertexBuffer.position(0);

		bb = ByteBuffer.allocateDirect(featureIndexBuf.length * Short.SIZE / 8);
		bb.order(ByteOrder.nativeOrder());
		indexBuffer = bb.asShortBuffer();
		indexBuffer.put(featureIndexBuf);
		indexBuffer.position(0);

		bb = ByteBuffer.allocateDirect(featureTexCoordBuf.length * Float.SIZE / 8);
		bb.order(ByteOrder.nativeOrder());
		textureCoordBuff = bb.asFloatBuffer();
		textureCoordBuff.put(featureTexCoordBuf);
		textureCoordBuff.position(0);

		shaderProgramId = ShaderUtil.createProgram(VERTEX_SHADER_SRC, FRAGMENT_SHADER_SRC);

		positionHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_position");
		textureCoordHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_vertexTexCoord");
		mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgramId, "u_mvpMatrix");

		textureNames = new int[2];
		GLES20.glGenTextures(2, textureNames, 0);
		for (int i = 0; i < 2; i++) {
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[i]);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		}

		textureHandles = new int[1];
		textureHandles[0] = GLES20.glGetUniformLocation(shaderProgramId, "u_texture");

		initVBO();
	}

	private void initVBO() {
		GLES20.glGenBuffers(3, vertexBufferObject, 0);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, featureVertexBuf.length * Float.SIZE / 8, vertexBuffer,
				GLES20.GL_DYNAMIC_DRAW);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[1]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, featureTexCoordBuf.length * Float.SIZE / 8, textureCoordBuff,
				GLES20.GL_DYNAMIC_DRAW);

		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vertexBufferObject[2]);
		GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, featureIndexBuf.length * Short.SIZE / 8, indexBuffer,
				GLES20.GL_DYNAMIC_DRAW);

		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}

	public void setFeatureImage(Bitmap blueImage, Bitmap redImage) {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);

		if (blueImage != null) {
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, blueImage, 0);
			blueImage.recycle();
		}

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[1]);

		if (redImage != null) {
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, redImage, 0);
			redImage.recycle();
		}
	}

	public void draw(GuideInfo guide, TrackingResult trackingResult) {
		int featureCount = guide.getGuideFeatureCount();
		if (featureCount == 0) {
			return;
		}
		float[] featureBuffer = guide.getGuideFeatureBuffer();

		float[] vertexPtr = featureVertexBuf;
		float[] texturePtr = featureTexCoordBuf;
		short[] indexPtr = featureIndexBuf;

		float [] newVertex = new float[4 * 3];
		short[] newIndex = {0, 1, 2, 2, 3, 0};
		Float3 featureVertex = new Float3();
		for (int i = 0; i < featureCount; i++) {
			featureVertex.x = featureBuffer[i * 3];
			featureVertex.y = featureBuffer[i * 3 + 1];
			featureVertex.z = featureBuffer[i * 3 + 2];
			convertFeatureToPlane(featureVertex, newVertex, newIndex, i);
			System.arraycopy(newVertex, 0, vertexPtr, newVertex.length * i, newVertex.length);
			System.arraycopy(TEX_COORD_BUFFER, 0, texturePtr, TEX_COORD_BUFFER.length * i, TEX_COORD_BUFFER.length);
			System.arraycopy(newIndex, 0, indexPtr, newIndex.length * i, newIndex.length);
		}

		vertexBuffer.put(vertexPtr);
		vertexBuffer.position(0);

		indexBuffer.put(indexPtr);
		indexBuffer.position(0);

		textureCoordBuff.put(texturePtr);
		textureCoordBuff.position(0);

		GLES20.glUseProgram(shaderProgramId);

		GLES20.glEnableVertexAttribArray(positionHandle);
		GLES20.glEnableVertexAttribArray(textureCoordHandle);

		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, newVertex.length * featureCount * Float.SIZE / 8, vertexBuffer);
		GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, 0);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[1]);
		GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, TEX_COORD_BUFFER.length * featureCount * Float.SIZE / 8, textureCoordBuff);
		GLES20.glVertexAttribPointer(textureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, 0);

		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, projectionMatrix, 0);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glUniform1i(textureHandles[0], 0);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);

//		if (trackingResult.getCount() > 0) {
//			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);
//		} else {
//			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[1]);
//		}

		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vertexBufferObject[2]);
		GLES20.glBufferSubData(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0, newIndex.length * featureCount * Short.SIZE / 8, indexBuffer);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, newIndex.length * featureCount, GLES20.GL_UNSIGNED_SHORT, 0);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

		GLES20.glDisable(GLES20.GL_BLEND);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

		GLES20.glDisableVertexAttribArray(positionHandle);
		GLES20.glDisableVertexAttribArray(textureCoordHandle);

		GLES20.glUseProgram(0);
	}

	private void convertFeatureToPlane(Float3 srcVertex, float[] dstPlane, short[] index, int count) {
		float x = srcVertex.x;
		float y = srcVertex.y;
		float z = srcVertex.z;

		dstPlane[0] = x - POINT_SIZE;
		dstPlane[1] = y + POINT_SIZE;
		dstPlane[2] = z;

		dstPlane[3] = x - POINT_SIZE;
		dstPlane[4] = y - POINT_SIZE;
		dstPlane[5] = z;

		dstPlane[6] = x + POINT_SIZE;
		dstPlane[7] = y - POINT_SIZE;
		dstPlane[8] = z;

		dstPlane[9] = x + POINT_SIZE;
		dstPlane[10] = y + POINT_SIZE;
		dstPlane[11] = z;

		if (count == 0) {
			return;
		}

		for (int i = 0; i < 6; i++) {
			index[i] += 4;
		}
	}
}
