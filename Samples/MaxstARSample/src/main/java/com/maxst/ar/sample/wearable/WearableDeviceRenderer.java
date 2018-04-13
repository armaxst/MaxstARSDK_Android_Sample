/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */
package com.maxst.ar.sample.wearable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.MaxstARUtil;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;
import com.maxst.ar.WearableCalibration;
import com.maxst.ar.sample.arobject.TexturedCube;
import com.maxst.ar.wearable.WearableDeviceController;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class WearableDeviceRenderer implements Renderer {

	public static final String TAG = WearableDeviceRenderer.class.getSimpleName();

	private TexturedCube texturedCube;

	private int surfaceWidth;
	private int surfaceHeight;

	private final Activity activity;
	private float[] leftEyeProjectionMatrix;
	private float[] rightEyeProjectionMatrix;
	private float[] rightEyeViewport;
	private float[] leftEyeViewport;
	private WearableDeviceController wearableDeviceController;

	WearableDeviceRenderer(Activity activity, WearableDeviceController wearableDeviceController) {
		this.activity = activity;
		this.wearableDeviceController = wearableDeviceController;
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		Bitmap bitmap = MaxstARUtil.getBitmapFromAsset("MaxstAR_Cube.png", activity.getAssets());

		texturedCube = new TexturedCube();
		texturedCube.setTextureBitmap(bitmap);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		surfaceWidth = width;
		surfaceHeight = height;

		MaxstAR.onSurfaceChanged(surfaceWidth, surfaceHeight);
		WearableCalibration.getInstance().setSurfaceSize(surfaceWidth, surfaceHeight);

		leftEyeProjectionMatrix = WearableCalibration.getInstance().getProjectionMatrix(WearableCalibration.EyeType.EYE_LEFT.getValue());
		rightEyeProjectionMatrix = WearableCalibration.getInstance().getProjectionMatrix(WearableCalibration.EyeType.EYE_RIGHT.getValue());
		leftEyeViewport = WearableCalibration.getInstance().getViewport(WearableCalibration.EyeType.EYE_LEFT);
		rightEyeViewport = WearableCalibration.getInstance().getViewport(WearableCalibration.EyeType.EYE_RIGHT);
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);

		TrackingState state = TrackerManager.getInstance().updateTrackingState();
		renderFrame(state);
	}

	private void renderFrame(TrackingState state) {
		if (wearableDeviceController.isSupportedWearableDevice()) {
			GLES20.glViewport((int) leftEyeViewport[0], (int) leftEyeViewport[1], (int) leftEyeViewport[2], (int) leftEyeViewport[3]);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);

			TrackingResult trackingResult = state.getTrackingResult();

			for (int i = 0; i < trackingResult.getCount(); i++) {
				Trackable trackable = trackingResult.getTrackable(i);

				texturedCube.setProjectionMatrix(leftEyeProjectionMatrix);
				texturedCube.setTransform(trackable.getPoseMatrix());
				texturedCube.setTranslate(0, 0, -0.005f);
				texturedCube.setScale(0.26f, 0.18f, 0.01f);
				texturedCube.draw();
			}

			GLES20.glViewport((int) rightEyeViewport[0], (int) rightEyeViewport[1], (int) rightEyeViewport[2], (int) rightEyeViewport[3]);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);

			trackingResult = state.getTrackingResult();

			for (int i = 0; i < trackingResult.getCount(); i++) {
				Trackable trackable = trackingResult.getTrackable(i);

				texturedCube.setProjectionMatrix(rightEyeProjectionMatrix);
				texturedCube.setTransform(trackable.getPoseMatrix());
				texturedCube.setTranslate(0, 0, -0.005f);
				texturedCube.setScale(0.26f, 0.18f, 0.01f);
				texturedCube.draw();
			}
		} else {
			TrackingResult trackingResult = state.getTrackingResult();
			float[] projectionMatrix = CameraDevice.getInstance().getProjectionMatrix();

			GLES20.glViewport(0, 0, surfaceWidth / 2, surfaceHeight);

			GLES20.glEnable(GLES20.GL_DEPTH_TEST);

			for (int i = 0; i < trackingResult.getCount(); i++) {
				Trackable trackable = trackingResult.getTrackable(i);

				texturedCube.setProjectionMatrix(projectionMatrix);
				texturedCube.setTransform(trackable.getPoseMatrix());
				texturedCube.setTranslate(0, 0, -0.005f);
				texturedCube.setScale(0.26f, 0.18f, 0.01f);
				texturedCube.draw();
			}

			GLES20.glViewport(surfaceWidth / 2, 0, surfaceWidth / 2, surfaceHeight);

			GLES20.glEnable(GLES20.GL_DEPTH_TEST);

			for (int i = 0; i < trackingResult.getCount(); i++) {
				Trackable trackable = trackingResult.getTrackable(i);

				texturedCube.setProjectionMatrix(projectionMatrix);
				texturedCube.setTransform(trackable.getPoseMatrix());
				texturedCube.setTranslate(0, 0, -0.005f);
				texturedCube.setScale(0.26f, 0.18f, 0.01f);
				texturedCube.draw();
			}
		}
	}
}
