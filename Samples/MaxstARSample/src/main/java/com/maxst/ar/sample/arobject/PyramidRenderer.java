/*
 * Copyright 2016 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.arobject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.maxst.ar.sample.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PyramidRenderer extends BaseRenderer {

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

	private static final float[] VERTEX_BUF = {
			-0.2f, -0.5f, -0.5f,	// 0
			0.2f, -0.5f, -0.5f,		// 1
			0.5f, -0.2f, -0.5f,		// 2
			0.5f, 0.2f, -0.5f,		// 3
			0.2f, 0.5f, -0.5f,		// 4
			-0.2f, 0.5f, -0.5f,		// 5
			-0.5f, 0.2f, -0.5f,		// 6
			-0.5f, -0.2f, -0.5f,	// 7

			0.0f, 0.0f, 0.5f,		// 8
	};

	private static final short[] INDEX_BUF = {
			0, 1, 8,
			1, 2, 8,
			2, 3, 8,
			3, 4, 8,
			4, 5, 8,
			5, 6, 8,
			6, 7, 8,
			7, 0, 8
	};

	public PyramidRenderer() {
		super();
		float r = 1.0f, g = 1.0f, b = 1.0f, a= 1.0f;
		initialize(r, g, b, a);
	}

	public PyramidRenderer(float r, float g, float b, float a) {
		super();
		initialize(r, g, b, a);
	}

	private void initialize(float r, float g, float b, float a)
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(VERTEX_BUF.length * Float.SIZE / 8);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(VERTEX_BUF);
		vertexBuffer.position(0);

		bb = ByteBuffer.allocateDirect(INDEX_BUF.length * Short.SIZE / 8);
		bb.order(ByteOrder.nativeOrder());
		indexBuffer = bb.asShortBuffer();
		indexBuffer.put(INDEX_BUF);
		indexBuffer.position(0);

		final int vertexCount =  VERTEX_BUF.length / 3;
		float[] COLOR_BUF = new float[vertexCount * 4];
		for(int i=0; i<vertexCount; i++)
		{
			COLOR_BUF[i*4+0] = r;
			COLOR_BUF[i*4+1] = g;
			COLOR_BUF[i*4+2] = b;
			COLOR_BUF[i*4+3] = a;
		}

		bb = ByteBuffer.allocateDirect(COLOR_BUF.length * Float.SIZE / 8);
		bb.order(ByteOrder.nativeOrder());
		colorBuffer = bb.asFloatBuffer();
		colorBuffer.put(COLOR_BUF);
		colorBuffer.position(0);

		shaderProgramId = ShaderUtil.createProgram(VERTEX_SHADER_SRC, FRAGMENT_SHADER_SRC);

		positionHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_position");
		colorHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_color");
		mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgramId, "u_mvpMatrix");
	}

	public void draw() {
		GLES20.glUseProgram(shaderProgramId);

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

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, INDEX_BUF.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

		GLES20.glDisableVertexAttribArray(positionHandle);
		GLES20.glDisableVertexAttribArray(colorHandle);
	}
}
