/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.thej.stickyworld.slam;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.maxst.ar.BackgroundRenderer;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.MaxstARUtil;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.thej.newar.arobject.TexturedCube;
import com.thej.newar.util.BackgroundRenderHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class ObjectTrackerRenderer implements Renderer {

	private int surfaceWidth;
	private int surfaceHeight;
	private BackgroundRenderHelper backgroundRenderHelper;

	private TexturedCube texturedCube;
	private final Activity activity;

	private boolean drawCube = false;
	private boolean showTrackingLostPopup = false;
	private int trackerType = TrackerManager.TRACKER_TYPE_OBJECT;

	ObjectTrackerRenderer(Activity activity, boolean drawCube) {
		this.activity = activity;
		this.drawCube = drawCube;
		backgroundRenderHelper = new BackgroundRenderHelper();
	}

	ObjectTrackerRenderer(Activity activity, int trackerType, boolean drawCube) {
		this.activity = activity;
		this.drawCube = drawCube;
		this.trackerType = trackerType;
		backgroundRenderHelper = new BackgroundRenderHelper();
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);

		TrackingResult trackingResult = TrackerManager.getInstance().getTrackingResult();

		backgroundRenderHelper.drawBackground();

		float[] projectionMatrix = CameraDevice.getInstance().getProjectionMatrix();

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		if (trackingResult.getCount() > 0) {
			if (drawCube) {
				Trackable trackable = trackingResult.getTrackable(0);
				texturedCube.setTransform(trackable.getPoseMatrix());
				texturedCube.setTranslate(0, 0, -0.0005f);
				texturedCube.setScale(0.4f, 0.4f, 0.001f);
				texturedCube.setProjectionMatrix(projectionMatrix);
				texturedCube.draw();
			}

			if (trackerType == TrackerManager.TRACKER_TYPE_SLAM) {
				showTrackingLostPopup = true;
			}
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

		backgroundRenderHelper.init();
		if (trackerType == TrackerManager.TRACKER_TYPE_SLAM) {
			backgroundRenderHelper.setRenderingOption(
					BackgroundRenderer.RenderingOption.FEATURE_RENDERER,
					BackgroundRenderer.RenderingOption.PROGRESS_RENDERER,
					BackgroundRenderer.RenderingOption.SURFACE_MESH_RENDERER,
					BackgroundRenderer.RenderingOption.AXIS_RENDERER);
		}

		texturedCube = new TexturedCube();
		Bitmap bitmap = MaxstARUtil.getBitmapFromAsset("MaxstAR_Cube.png", activity.getAssets());
		texturedCube.setTextureBitmap(bitmap);

		MaxstAR.onSurfaceCreated();
	}
}
