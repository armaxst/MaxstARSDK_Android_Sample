/*
 * Copyright 2016 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.util;

import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class ShaderUtil {

	public static int createProgram(String vertexSrc, String fragmentSrc) {
		int vertexShader = ShaderUtil.loadShader(GLES20.GL_VERTEX_SHADER, vertexSrc);
		int fragmentShader = ShaderUtil.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSrc);

		int shaderProgramId = GLES20.glCreateProgram();
		GLES20.glAttachShader(shaderProgramId, vertexShader);
		GLES20.glAttachShader(shaderProgramId, fragmentShader);
		GLES20.glLinkProgram(shaderProgramId);

		return shaderProgramId;
	}

	private static int loadShader(int type, String shaderSrc) {
		int shader;
		shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderSrc);
		GLES20.glCompileShader(shader);
		return shader;
	}

	public static void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e("GL Error", op + ": glError " + GLUtils.getEGLErrorString(error));
		}
	}
}
