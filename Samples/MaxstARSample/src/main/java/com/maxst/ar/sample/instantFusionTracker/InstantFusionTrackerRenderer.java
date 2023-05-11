/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.instantFusionTracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.MaxstARUtil;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackedImage;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;
import com.maxst.ar.sample.arobject.BackgroundRenderHelper;
import com.maxst.ar.sample.arobject.Yuv420spRenderer;
import com.maxst.ar.sample.arobject.TexturedCubeRenderer;
import com.maxst.ar.sample.util.TrackerResultListener;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


class InstantFusionTrackerRenderer implements Renderer {

	public static final String TAG = InstantFusionTrackerRenderer.class.getSimpleName();

	private int surfaceWidth;
	private int surfaceHeight;

	private TexturedCubeRenderer texturedCubeRenderer;
	private float[] modelMatrix = new float[16];
	private Activity activity;
	private float posX = 0.0f;
	private float posY = 0.0f;

	private BackgroundRenderHelper backgroundRenderHelper;
	public TrackerResultListener listener = null;

	InstantFusionTrackerRenderer(Activity activity) {
		this.activity = activity;
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

		backgroundRenderHelper.drawBackground(image, projectionMatrix, backgroundPlaneInfo);

		int fusionState = TrackerManager.getInstance().getFusionTrackingState();
		int trackingCount = trackingResult.getCount();

		listener.sendFusionState(fusionState);

		if(fusionState != 1) {
			return;
		}

		if (trackingCount == 0) {
			return;
		}

		Trackable trackable = trackingResult.getTrackable(0);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		float[] pose = trackable.getPoseMatrix();

		texturedCubeRenderer.setTransform(pose);
		texturedCubeRenderer.setTranslate(posX, 0.0f, posY);
		texturedCubeRenderer.setScale(0.1f, 0.1f, 0.0001f);
		texturedCubeRenderer.setProjectionMatrix(projectionMatrix);
		texturedCubeRenderer.draw();
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

		texturedCubeRenderer = new TexturedCubeRenderer();
		Bitmap bitmap = MaxstARUtil.getBitmapFromAsset("MaxstAR_Cube.png", activity.getAssets());
		texturedCubeRenderer.setTextureBitmap(bitmap);

		backgroundRenderHelper = new BackgroundRenderHelper();

		CameraDevice.getInstance().setARCoreTexture();
		CameraDevice.getInstance().setClippingPlane(0.03f, 70.0f);
	}

	void setTranslate(float x, float y) {
		posX += x;
		posY += y;
	}

	void resetPosition() {
		posX = 0;
		posY = 0;
	}
}
