/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.slam;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.MaxstARUtil;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackedImage;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;
import com.maxst.ar.sample.arobject.Axis;
import com.maxst.ar.sample.arobject.BackgroundCameraQuad;
import com.maxst.ar.sample.arobject.FeaturePoint;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class SlamRenderer implements Renderer {

	private int surfaceWidth;
	private int surfaceHeight;

	private FeaturePoint featurePoint;
	private Axis axis;
	private final Activity activity;

	private boolean showTrackingLostPopup = false;
	private int trackerType = TrackerManager.TRACKER_TYPE_OBJECT;

	private BackgroundCameraQuad backgroundCameraQuad;

	SlamRenderer(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);

		TrackingState state = TrackerManager.getInstance().updateTrackingState();
		TrackingResult trackingResult = state.getTrackingResult();

		TrackedImage image = state.getImage();
		float[] cameraProjectionMatrix = CameraDevice.getInstance().getBackgroundPlaneProjectionMatrix();
		backgroundCameraQuad.setProjectionMatrix(cameraProjectionMatrix);
		backgroundCameraQuad.draw(image);

		float[] projectionMatrix = CameraDevice.getInstance().getProjectionMatrix();

		featurePoint.draw(TrackerManager.getInstance().getGuideInformation(), projectionMatrix);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		if (trackingResult.getCount() > 0) {
			Trackable trackable = trackingResult.getTrackable(0);

			axis.setTransform(trackable.getPoseMatrix());
			axis.setTranslate(0, 0, 0);
			axis.setScale(0.3f, 0.3f, 0.3f);
			axis.setProjectionMatrix(projectionMatrix);
			axis.draw();

			showTrackingLostPopup = true;
		} else {
			if (showTrackingLostPopup) {
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(activity, "Lost Tracking. Please restart", Toast.LENGTH_SHORT).show();
					}
				});

				showTrackingLostPopup = false;
			}
		}
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

		backgroundCameraQuad = new BackgroundCameraQuad();
		featurePoint = new FeaturePoint();
		axis = new Axis();

		Bitmap blueBitmap = MaxstARUtil.getBitmapFromAsset("bluedot.png", activity.getAssets());
		Bitmap redBitmap = MaxstARUtil.getBitmapFromAsset("reddot.png", activity.getAssets());
		featurePoint.setFeatureImage(blueBitmap, redBitmap);
		featurePoint.setTrackState(true);
	}
}
