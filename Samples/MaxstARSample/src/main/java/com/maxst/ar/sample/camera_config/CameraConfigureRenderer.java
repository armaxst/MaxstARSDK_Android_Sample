/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */
package com.maxst.ar.sample.camera_config;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.Image;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.MaxstARUtil;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;
import com.maxst.ar.sample.arobject.TexturedCube;
import com.maxst.ar.sample.util.BackgroundRenderHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


class CameraConfigureRenderer implements Renderer {

	public static final String TAG = CameraConfigureRenderer.class.getSimpleName();

	private TexturedCube texturedCube;

	private int surfaceWidth;
	private int surfaceHeight;
	private BackgroundRenderHelper backgroundRenderHelper;

	private final Activity activity;

	CameraConfigureRenderer(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		backgroundRenderHelper = new BackgroundRenderHelper();
		backgroundRenderHelper.init();

		Bitmap bitmap = MaxstARUtil.getBitmapFromAsset("MaxstAR_Cube.png", activity.getAssets());

		texturedCube = new TexturedCube();
		texturedCube.setTextureBitmap(bitmap);

		MaxstAR.onSurfaceCreated();
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
		TrackingResult trackingResult = TrackerManager.getInstance().getTrackingResult(state);

		// Get Image test
		Image image = state.getImage();
		if (image.getWidth() > 0 && image.getHeight() > 0) {
			Log.d(TAG, "width : " + image.getWidth());
			Log.d(TAG, "height : " + image.getHeight());
			Log.d(TAG, "format : " + image.getFormat());
			byte [] data = image.getData();
			if (data != null) {
				Log.d(TAG, "data : " + data[0] + ", " + data[1] + ", " + data[2] + ", " + data[3]);
			}
		}

		backgroundRenderHelper.drawBackground();

		float[] projectionMatrix = CameraDevice.getInstance().getProjectionMatrix();

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		for (int i = 0; i < trackingResult.getCount(); i++) {
			Trackable trackable = trackingResult.getTrackable(i);
			texturedCube.setProjectionMatrix(projectionMatrix);
			texturedCube.setTransform(trackable.getPoseMatrix());
			texturedCube.setTranslate(0, 0, -0.05f);
			texturedCube.setScale(0.3f, 0.3f, 0.1f);
			texturedCube.draw();
		}
	}
}
