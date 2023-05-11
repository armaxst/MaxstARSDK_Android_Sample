/*
 * Copyright 2018 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.arobject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.maxst.ar.TrackedImage;
import com.maxst.ar.sample.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Yuv420_888Renderer extends BackgroundRenderer {
	private static final String VERTEX_SHADER_SRC =
			"attribute vec4 a_position;\n" +
					"uniform mat4 u_mvpMatrix;\n" +
					"attribute vec2 a_vertexTexCoord;\n" +
					"varying vec2 v_texCoord;\n" +
					"void main()\n" +
					"{\n" +
					"   gl_Position = u_mvpMatrix * a_position;\n" +
					"   v_texCoord = a_vertexTexCoord;          \n" +
					"}\n";

//
private static final String FRAGMENT_SHADER_SRC =
		"precision mediump float;\n" +
		"uniform sampler2D u_texture_1;\n" +
		"uniform sampler2D u_texture_2;\n" +
		"uniform sampler2D u_texture_3;\n"							+
		"varying vec2 v_texCoord;\n" +
		"void main()\n" +
		"{\n" +
		"    float y = texture2D(u_texture_1, v_texCoord).r;\n" +
		"    float v = texture2D(u_texture_2, v_texCoord).a;\n" +
		"    float u = texture2D(u_texture_3, v_texCoord).a;\n" +
		"    y = 1.1643 * (y - 0.0625);\n" +
		"    u = u - 0.5;\n" +
		"    v = v - 0.5;\n" +
		"    float r = y + 1.5958 * v;\n" +
		"    float g = y - 0.39173 * u - 0.81290 * v;\n" +
		"    float b = y + 2.017 * u;\n" +
		"    gl_FragColor = vec4(r, g, b, 1.0);\n" +
		"}\n";


	private static final float[] VERTEX_BUF = {
			-0.5f, 0.5f, 0.0f,
			-0.5f, -0.5f, 0.0f,
			0.5f, -0.5f, 0.0f,
			0.5f, 0.5f, 0.0f
	};

	private static final short[] INDEX_BUF = {
			0, 1, 2, 2, 3, 0
	};

	private static final float[] TEXTURE_COORD_BUF = {
			0.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f,
	};

	private ByteBuffer yBuffer;
	private ByteBuffer uBuffer;
	private ByteBuffer vBuffer;

	Yuv420_888Renderer() {
		super();
		ByteBuffer bb = ByteBuffer.allocateDirect(VERTEX_BUF.length * Float.SIZE / 8);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(VERTEX_BUF);
		vertexBuffer.position(0);

		bb = ByteBuffer.allocateDirect(INDEX_BUF.length * Integer.SIZE / 8);
		bb.order(ByteOrder.nativeOrder());
		indexBuffer = bb.asShortBuffer();
		indexBuffer.put(INDEX_BUF);
		indexBuffer.position(0);

		bb = ByteBuffer.allocateDirect(TEXTURE_COORD_BUF.length * Float.SIZE / 8);
		bb.order(ByteOrder.nativeOrder());
		textureCoordBuff = bb.asFloatBuffer();
		textureCoordBuff.put(TEXTURE_COORD_BUF);
		textureCoordBuff.position(0);

		shaderProgramId = ShaderUtil.createProgram(VERTEX_SHADER_SRC, FRAGMENT_SHADER_SRC);

		positionHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_position");
		textureCoordHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_vertexTexCoord");
		mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgramId, "u_mvpMatrix");

		textureNames = new int[3];
		textureHandles = new int[3];

		GLES20.glGenTextures(3, textureNames, 0);
		for (int i = 0; i < 3; i++) {
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[i]);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			String textureHandleId = "u_texture_" + (i + 1);
			textureHandles[i] = GLES20.glGetUniformLocation(shaderProgramId, textureHandleId);
		}
	}

	private int yDataLength = 0;
	private int uDataLength = 0;
	private int vDataLength = 0;

	public void draw(TrackedImage image) {

		if (image.getData() == null || image.getWidth() == 0) {
			return;
		}

		int yDataLength = image.getWidth() * image.getHeight();
		int uDataLength = image.getWidth() * image.getHeight() / 2 - 1;
		int vDataLength = image.getWidth() * image.getHeight() / 2 - 1;

		if (this.yDataLength != yDataLength || this.uDataLength != uDataLength || this.vDataLength != vDataLength) {
			this.yDataLength = yDataLength;
			this.uDataLength = uDataLength;
			this.vDataLength = vDataLength;

			yBuffer = ByteBuffer.allocateDirect(yDataLength);
			uBuffer = ByteBuffer.allocateDirect(uDataLength);
			vBuffer = ByteBuffer.allocateDirect(vDataLength);
			yBuffer.order(ByteOrder.nativeOrder());
			uBuffer.order(ByteOrder.nativeOrder());
			vBuffer.order(ByteOrder.nativeOrder());
		}

		yBuffer.put(image.getData(), 0, yDataLength);
		yBuffer.position(0);

		uBuffer.put(image.getData(), yDataLength, uDataLength);
		uBuffer.position(0);

		vBuffer.put(image.getData(), yDataLength + uDataLength, vDataLength);
		vBuffer.position(0);

		GLES20.glUseProgram(shaderProgramId);

		GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false,
				0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(positionHandle);

		GLES20.glVertexAttribPointer(textureCoordHandle, 2, GLES20.GL_FLOAT, false,
				0, textureCoordBuff);
		GLES20.glEnableVertexAttribArray(textureCoordHandle);

		Matrix.multiplyMM(localMvpMatrix, 0, projectionMatrix, 0, transform, 0);
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, localMvpMatrix, 0);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, image.getWidth(), image.getHeight(), 0,
				GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, yBuffer);
		GLES20.glUniform1i(textureHandles[0], 0);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[1]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE_ALPHA, image.getWidth() / 2, image.getHeight() / 2, 0,
				GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, uBuffer);
		GLES20.glUniform1i(textureHandles[1], 1);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[2]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE_ALPHA, image.getWidth() / 2, image.getHeight() / 2, 0,
				GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, vBuffer);
		GLES20.glUniform1i(textureHandles[2], 2);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, INDEX_BUF.length,
				GLES20.GL_UNSIGNED_SHORT, indexBuffer);

		GLES20.glDisableVertexAttribArray(positionHandle);
		GLES20.glDisableVertexAttribArray(textureCoordHandle);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		GLES20.glUseProgram(0);
	}
}
