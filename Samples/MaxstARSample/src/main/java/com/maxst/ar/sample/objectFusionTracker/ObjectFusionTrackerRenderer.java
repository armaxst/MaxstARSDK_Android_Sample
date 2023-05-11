/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.objectFusionTracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.GuideInfo;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.MaxstARUtil;
import com.maxst.ar.TagAnchor;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackedImage;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;
import com.maxst.ar.sample.arobject.AxisRenderer;
import com.maxst.ar.sample.arobject.BackgroundRenderHelper;
import com.maxst.ar.sample.arobject.BoundingShapeRenderer;
import com.maxst.ar.sample.arobject.ColoredCubeRenderer;
import com.maxst.ar.sample.arobject.FeaturePointRenderer;
import com.maxst.ar.sample.arobject.SphereRenderer;
import com.maxst.ar.sample.arobject.Yuv420spRenderer;
import com.maxst.ar.sample.arobject.TexturedCubeRenderer;
import com.maxst.ar.sample.util.TrackerResultListener;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.content.ContentValues.TAG;

class ObjectFusionTrackerRenderer implements Renderer {

	public int surfaceWidth;
	public int surfaceHeight;
	private final Activity activity;

	private TexturedCubeRenderer texturedCubeRenderer;
	private SphereRenderer sphereRenderer;
	private BackgroundRenderHelper backgroundRenderHelper;
	public TrackerResultListener listener = null;

	ObjectFusionTrackerRenderer(Activity activity) {
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

		GuideInfo gi = TrackerManager.getInstance().getGuideInformation();
		int fusionState = TrackerManager.getInstance().getFusionTrackingState();
		listener.sendFusionState(fusionState);

		if(fusionState != 1) {
			return;
		}

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		if(trackingResult.getCount() > 0) {
			Trackable trackable = trackingResult.getTrackable(0);

			float[] poseMatrix = trackable.getPoseMatrix();

			int anchorCount = gi.getTagAnchorCount();

			if(anchorCount > 0) {
				TagAnchor[] anchors = gi.getTagAnchors();
				for(int i=0; i<anchorCount; i++) {
					TagAnchor eachAnchor = anchors[i];
					sphereRenderer.setProjectionMatrix(projectionMatrix);
					sphereRenderer.setTransform(poseMatrix);
					sphereRenderer.setTranslate((float)eachAnchor.positionX, (float)eachAnchor.positionY, (float)eachAnchor.positionZ);
					sphereRenderer.setScale(0.02f, 0.02f, 0.02f);
					sphereRenderer.setRotation(-90.0f, 1.0f, 0.0f, 0.0f);
					sphereRenderer.draw();
				}
			}

			texturedCubeRenderer.setProjectionMatrix(projectionMatrix);
			texturedCubeRenderer.setTransform(poseMatrix);
			texturedCubeRenderer.setTranslate(0, 0.0f, 0.0f);
			texturedCubeRenderer.setScale(0.15f, 0.15f, 0.0001f);
			texturedCubeRenderer.draw();
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

		texturedCubeRenderer = new TexturedCubeRenderer();
		Bitmap bitmap = MaxstARUtil.getBitmapFromAsset("MaxstAR_Cube.png", activity.getAssets());
		texturedCubeRenderer.setTextureBitmap(bitmap);

		sphereRenderer = new SphereRenderer(1.0f, 0.0f, 0.0f, 1.0f);

		backgroundRenderHelper = new BackgroundRenderHelper();

		CameraDevice.getInstance().setARCoreTexture();
		CameraDevice.getInstance().setClippingPlane(0.03f, 70.0f);
	}
}
