/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.code;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.TrackedImage;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingState;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.arobject.BackgroundRenderHelper;
import com.maxst.ar.sample.arobject.Yuv420spRenderer;
import com.maxst.ar.sample.util.TrackerResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class CodeScanRenderer implements Renderer {

	private int surfaceWidth;
	private int surfaceHeight;
	private BackgroundRenderHelper backgroundRenderHelper;
	public TrackerResultListener listener = null;

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);

		TrackingState state = TrackerManager.getInstance().updateTrackingState();

		TrackedImage image = state.getImage();
		float[] projectionMatrix = CameraDevice.getInstance().getProjectionMatrix();
		float[] backgroundPlaneInfo = CameraDevice.getInstance().getBackgroundPlaneInfo();

		backgroundRenderHelper.drawBackground(image, projectionMatrix, backgroundPlaneInfo);

		String code = state.getCodeScanResult();

		if (code != null && code.length() > 0) {
			listener.sendData(code);
		}

	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {

		surfaceWidth = width;
		surfaceHeight = height;

		MaxstAR.onSurfaceChanged(width, height);
		backgroundRenderHelper = new BackgroundRenderHelper();
		CameraDevice.getInstance().setClippingPlane(0.03f, 70.0f);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}
}
