/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.thej.stickyworld.code;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import com.maxst.ar.BackgroundRenderer;
import com.maxst.ar.MaxstAR;
import com.thej.newar.util.BackgroundRenderHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class CodeScanRenderer implements Renderer {

	private int surfaceWidth;
	private int surfaceHeight;
	private BackgroundRenderHelper backgroundRenderHelper;

	public CodeScanRenderer() {}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);

		backgroundRenderHelper.drawBackground();
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {

		surfaceWidth = width;
		surfaceHeight = height;

		MaxstAR.onSurfaceChanged(width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		backgroundRenderHelper = new BackgroundRenderHelper();
		backgroundRenderHelper.init();
		backgroundRenderHelper.setRenderingOption(BackgroundRenderer.RenderingOption.VIEW_FINDER_RENDERER);

		MaxstAR.onSurfaceCreated();
	}
}
