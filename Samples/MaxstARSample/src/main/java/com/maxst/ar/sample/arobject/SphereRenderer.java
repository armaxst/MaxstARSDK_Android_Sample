/*
 * Copyright 2016 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.arobject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.maxst.ar.sample.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SphereRenderer extends BaseRenderer {

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
			0.452477f, 1.43869f, -0f,
			0f, 0f, 0f,
			-0.31995f, 1.43869f, 0.31995f,
			-0.452477f, 1.43869f, 0f,
			0.31995f, 1.43869f, -0.31995f,
			-0.31995f, 1.43869f, -0.31995f,
			0.31995f, 1.43869f, 0.31995f,
			0f, 1.43869f, 0.452477f,
			0f, 1.43869f, -0.452477f,
			0f, 2.66178f, 0.850651f,
			0f, 1.61032f, 0.850651f,
			0.850651f, 2.13605f, 0.525731f,
			0.850651f, 2.13605f, -0.525731f,
			0f, 2.66178f, -0.850651f,
			0f, 1.61032f, -0.850651f,
			-0.850651f, 2.13605f, -0.525731f,
			-0.850651f, 2.13605f, 0.525731f,
			0.525731f, 2.9867f, 0f,
			-0.525731f, 2.9867f, 0f,
			-0.525731f, 1.2854f, 0f,
			0.525731f, 1.2854f, 0f,
			0f, 3.13605f, 0f,
			0.309017f, 2.94506f, 0.5f,
			-0.309017f, 2.94506f, 0.5f,
			0.809017f, 2.63605f, 0.309017f,
			0.5f, 2.44506f, 0.809017f,
			0.809017f, 2.63605f, -0.309017f,
			1f, 2.13605f, 0f,
			0.309017f, 2.94506f, -0.5f,
			0.5f, 2.44506f, -0.809017f,
			-0.309017f, 2.94506f, -0.5f,
			-0.809017f, 2.63605f, -0.309017f,
			-0.5f, 2.44506f, -0.809017f,
			-0.809017f, 2.63605f, 0.309017f,
			-1f, 2.13605f, 0f,
			-0.5f, 2.44506f, 0.809017f,
			0f, 1.13605f, 0f,
			-0.309017f, 1.32703f, 0.5f,
			0.309017f, 1.32703f, 0.5f,
			0.809017f, 1.63605f, 0.309017f,
			0.5f, 1.82703f, 0.809017f,
			0.809017f, 1.63605f, -0.309017f,
			0.5f, 1.82703f, -0.809017f,
			0.309017f, 1.32703f, -0.5f,
			-0.309017f, 1.32703f, -0.5f,
			-0.5f, 1.82703f, -0.809017f,
			-0.809017f, 1.63605f, -0.309017f,
			-0.809017f, 1.63605f, 0.309017f,
			-0.5f, 1.82703f, 0.809017f,
			0f, 2.13605f, 1f,
			0f, 2.13605f, -1f,
	};

	private static final short[] INDEX_BUF = {
			0, 1, 4,
			3, 1, 2,
			7, 1, 6,
			5, 1, 3,
			2, 1, 7,
			4, 1, 8,
			6, 1, 0,
			8, 1, 5,
			23, 21, 18,
			22, 17, 21,
			21, 23, 22,
			9, 22, 23,
			22, 24, 17,
			25, 11, 24,
			24, 22, 25,
			9, 25, 22,
			24, 26, 17,
			27, 12, 26,
			26, 24, 27,
			11, 27, 24,
			26, 28, 17,
			29, 13, 28,
			28, 26, 29,
			12, 29, 26,
			28, 21, 17,
			30, 18, 21,
			21, 28, 30,
			13, 30, 28,
			32, 30, 13,
			31, 18, 30,
			30, 32, 31,
			15, 31, 32,
			34, 31, 15,
			33, 18, 31,
			31, 34, 33,
			16, 33, 34,
			33, 23, 18,
			35, 9, 23,
			23, 33, 35,
			16, 35, 33,
			38, 36, 20,
			37, 19, 36,
			36, 38, 37,
			10, 37, 38,
			40, 39, 11,
			38, 20, 39,
			39, 40, 38,
			10, 38, 40,
			39, 27, 11,
			41, 12, 27,
			27, 39, 41,
			20, 41, 39,
			41, 42, 12,
			43, 14, 42,
			42, 41, 43,
			20, 43, 41,
			43, 44, 14,
			36, 19, 44,
			44, 43, 36,
			20, 36, 43,
			44, 45, 14,
			46, 15, 45,
			45, 44, 46,
			19, 46, 44,
			46, 34, 15,
			47, 16, 34,
			34, 46, 47,
			19, 47, 46,
			47, 48, 16,
			37, 10, 48,
			48, 47, 37,
			19, 37, 47,
			49, 48, 10,
			35, 16, 48,
			48, 49, 35,
			9, 35, 49,
			25, 40, 11,
			49, 10, 40,
			40, 25, 49,
			9, 49, 25,
			29, 50, 13,
			42, 14, 50,
			50, 29, 42,
			12, 42, 29,
			50, 32, 13,
			45, 15, 32,
			32, 50, 45,
			14, 45, 50,
	};

	public SphereRenderer() {
		super();
		float r = 1.0f, g = 1.0f, b = 1.0f, a= 1.0f;
		initialize(r, g, b, a);
	}

	public SphereRenderer(float r, float g, float b, float a) {
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
