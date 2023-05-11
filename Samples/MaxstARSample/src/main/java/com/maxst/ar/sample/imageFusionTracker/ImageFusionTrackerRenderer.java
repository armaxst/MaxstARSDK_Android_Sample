/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */
package com.maxst.ar.sample.imageFusionTracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.Debug;
import android.renderscript.Matrix4f;
import android.util.Log;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.ColorFormat;
import com.maxst.ar.MatrixF4x4;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.MaxstARUtil;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackedImage;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;
import com.maxst.ar.sample.arobject.BackgroundRenderHelper;
import com.maxst.ar.sample.arobject.Yuv420spRenderer;
import com.maxst.ar.sample.arobject.ChromaKeyVideoRenderer;
import com.maxst.ar.sample.arobject.ColoredCubeRenderer;
import com.maxst.ar.sample.arobject.TexturedCubeRenderer;
import com.maxst.ar.sample.arobject.VideoRenderer;
import com.maxst.ar.sample.util.TrackerResultListener;
import com.maxst.videoplayer.VideoPlayer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


class ImageFusionTrackerRenderer implements Renderer {

	public static final String TAG = ImageFusionTrackerRenderer.class.getSimpleName();

	private TexturedCubeRenderer texturedCubeRenderer;

	private int surfaceWidth;
	private int surfaceHeight;
	private BackgroundRenderHelper backgroundRenderHelper;

	private final Activity activity;
	public TrackerResultListener listener = null;

	ImageFusionTrackerRenderer(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		Bitmap bitmap = MaxstARUtil.getBitmapFromAsset("MaxstAR_Cube.png", activity.getAssets());

		texturedCubeRenderer = new TexturedCubeRenderer();
		texturedCubeRenderer.setTextureBitmap(bitmap);

		backgroundRenderHelper = new BackgroundRenderHelper();

		CameraDevice.getInstance().setARCoreTexture();
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

		int fusionState = TrackerManager.getInstance().getFusionTrackingState();

		TrackedImage image = state.getImage();
		float[] projectionMatrix = CameraDevice.getInstance().getProjectionMatrix();
		float[] backgroundPlaneInfo = CameraDevice.getInstance().getBackgroundPlaneInfo();

		backgroundRenderHelper.drawBackground(image, projectionMatrix, backgroundPlaneInfo);

		listener.sendFusionState(fusionState);

		if(fusionState != 1) {
			return;
		}

		int trackingCount = trackingResult.getCount();

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		if(trackingCount > 0 && fusionState == 1) {
			Trackable trackable = trackingResult.getTrackable(0);

			float[] poseMatrix = trackable.getPoseMatrix();
			texturedCubeRenderer.setProjectionMatrix(projectionMatrix);
			texturedCubeRenderer.setTransform(poseMatrix);
			texturedCubeRenderer.setTranslate(0, 0.0f, 0.0f);
			texturedCubeRenderer.setScale(0.15f, 0.15f, 0.0001f);
			texturedCubeRenderer.draw();

		}
	}
}
