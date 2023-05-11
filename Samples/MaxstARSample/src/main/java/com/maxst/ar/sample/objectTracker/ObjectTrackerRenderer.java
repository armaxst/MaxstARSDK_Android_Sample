/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.objectTracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

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
import com.maxst.ar.sample.arobject.FeaturePointRenderer;
import com.maxst.ar.sample.arobject.SphereRenderer;
import com.maxst.ar.sample.arobject.TexturedCubeRenderer;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class ObjectTrackerRenderer implements Renderer {

	public int surfaceWidth;
	public int surfaceHeight;
	private final Activity activity;

	private TexturedCubeRenderer texturedCubeRenderer;
	private FeaturePointRenderer featurePointRenderer;
	private SphereRenderer sphereRenderer;
	private BoundingShapeRenderer boundingShapeRenderer;
	private BackgroundRenderHelper backgroundRenderHelper;

	ObjectTrackerRenderer(Activity activity) {
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

		featurePointRenderer.setProjectionMatrix(projectionMatrix);
		GuideInfo gi = TrackerManager.getInstance().getGuideInformation();
		featurePointRenderer.draw(gi, trackingResult);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		if (trackingResult.getCount() > 0) {
			Trackable trackable = trackingResult.getTrackable(0);


			float[] bb = gi.getBoundingBox();

			boundingShapeRenderer.setProjectionMatrix(projectionMatrix);
			boundingShapeRenderer.setTransform(trackable.getPoseMatrix());
			boundingShapeRenderer.setTranslate(bb[0], bb[1], bb[2]);
			boundingShapeRenderer.setScale(bb[3], bb[4], bb[5]);
			boundingShapeRenderer.draw();

			int anchorCount = gi.getTagAnchorCount();

			if(anchorCount > 0) {
				TagAnchor[] anchors = gi.getTagAnchors();
				for(int i=0; i<anchorCount; i++) {
					TagAnchor eachAnchor = anchors[i];
					sphereRenderer.setProjectionMatrix(projectionMatrix);
					sphereRenderer.setTransform(trackable.getPoseMatrix());
					sphereRenderer.setTranslate((float)eachAnchor.positionX, (float)eachAnchor.positionY, (float)eachAnchor.positionZ);
					sphereRenderer.setScale(0.02f, 0.02f, 0.02f);
					sphereRenderer.setRotation(-90.0f, 1.0f, 0.0f, 0.0f);
					sphereRenderer.draw();
				}
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

		texturedCubeRenderer = new TexturedCubeRenderer();
		Bitmap bitmap = MaxstARUtil.getBitmapFromAsset("MaxstAR_Cube.png", activity.getAssets());
		texturedCubeRenderer.setTextureBitmap(bitmap);

		featurePointRenderer = new FeaturePointRenderer();
		Bitmap blueBitmap = MaxstARUtil.getBitmapFromAsset("bluedot.png", activity.getAssets());
		Bitmap redBitmap = MaxstARUtil.getBitmapFromAsset("reddot.png", activity.getAssets());
		featurePointRenderer.setFeatureImage(blueBitmap, redBitmap);

		sphereRenderer = new SphereRenderer(1.0f, 0.0f, 0.0f, 1.0f);

		boundingShapeRenderer = new BoundingShapeRenderer();
		backgroundRenderHelper = new BackgroundRenderHelper();
		CameraDevice.getInstance().setClippingPlane(0.03f, 70.0f);
	}
}
