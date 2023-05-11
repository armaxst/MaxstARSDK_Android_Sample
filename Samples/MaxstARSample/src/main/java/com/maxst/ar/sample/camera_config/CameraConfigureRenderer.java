/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */
package com.maxst.ar.sample.camera_config;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.MaxstARUtil;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackedImage;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;
import com.maxst.ar.sample.arobject.BackgroundRenderHelper;
import com.maxst.ar.sample.arobject.TexturedCubeRenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


class CameraConfigureRenderer implements Renderer {

	public static final String TAG = CameraConfigureRenderer.class.getSimpleName();

	private TexturedCubeRenderer texturedCubeRenderer;

	private int surfaceWidth;
	private int surfaceHeight;

	private final Activity activity;

	private BackgroundRenderHelper backgroundRenderHelper;

	CameraConfigureRenderer(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		Bitmap bitmap = MaxstARUtil.getBitmapFromAsset("MaxstAR_Cube.png", activity.getAssets());

		texturedCubeRenderer = new TexturedCubeRenderer();
		texturedCubeRenderer.setTextureBitmap(bitmap);
		backgroundRenderHelper = new BackgroundRenderHelper();
		CameraDevice.getInstance().setClippingPlane(0.03f, 70.0f);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		surfaceWidth = width;
		surfaceHeight = height;

		MaxstAR.onSurfaceChanged(width, height);
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);

		TrackingState state = TrackerManager.getInstance().updateTrackingState();
		TrackingResult trackingResult = state.getTrackingResult();
		TrackedImage image = state.getImage();
		float[] projectionMatrix = CameraDevice.getInstance().getProjectionMatrix();
		float[] backgroundPlaneInfo = CameraDevice.getInstance().getBackgroundPlaneInfo();
		boolean flipHorizontal = CameraDevice.getInstance().isVideoFlipped(CameraDevice.FlipDirection.HORIZONTAL);
		boolean flipVertical = CameraDevice.getInstance().isVideoFlipped(CameraDevice.FlipDirection.VERTICAL);

		backgroundRenderHelper.drawBackground(image, projectionMatrix, backgroundPlaneInfo, flipHorizontal, flipVertical);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		for (int i = 0; i < trackingResult.getCount(); i++) {
			Trackable trackable = trackingResult.getTrackable(i);
			texturedCubeRenderer.setProjectionMatrix(projectionMatrix);
			texturedCubeRenderer.setTransform(trackable.getPoseMatrix());
			texturedCubeRenderer.setTranslate(0, 0, -0.05f);
			texturedCubeRenderer.setScale(0.15f, 0.15f, 0.1f);
			texturedCubeRenderer.draw();
		}
	}
}
