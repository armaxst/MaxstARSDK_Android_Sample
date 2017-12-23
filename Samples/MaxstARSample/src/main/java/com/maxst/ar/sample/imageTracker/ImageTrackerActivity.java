/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.imageTracker;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.ARActivity;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;

import java.io.IOException;

public class ImageTrackerActivity extends ARActivity implements View.OnClickListener, View.OnTouchListener {

	private ImageTrackerRenderer imageTargetRenderer;
	private GLSurfaceView glSurfaceView;
	private int preferCameraResolution = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_tracker);

		findViewById(R.id.normal_tracking).setOnClickListener(this);
		findViewById(R.id.extended_tracking).setOnClickListener(this);
		findViewById(R.id.multi_tracking).setOnClickListener(this);

		imageTargetRenderer = new ImageTrackerRenderer(this);
		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(imageTargetRenderer);
		glSurfaceView.setOnTouchListener(this);

		try {
			String [] mapFiles = getAssets().list("ImageTarget");
			for (String fileName : mapFiles) {
				TrackerManager.getInstance().addTrackerData("ImageTarget/" + fileName, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		TrackerManager.getInstance().loadTrackerData();

		preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();

		glSurfaceView.onResume();
		TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_IMAGE);

		ResultCode resultCode = ResultCode.Success;
		switch (preferCameraResolution) {
			case 0:
				resultCode = CameraDevice.getInstance().start(0, 640, 480);
				break;

			case 1:
				resultCode = CameraDevice.getInstance().start(0, 1280, 720);
				break;
		}

		if (resultCode != ResultCode.Success) {
			Toast.makeText(this, R.string.camera_open_fail, Toast.LENGTH_SHORT).show();
			finish();
		}

		MaxstAR.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

		glSurfaceView.queueEvent(new Runnable() {
			@Override
			public void run() {
				imageTargetRenderer.destroyVideoPlayer();
			}
		});

		glSurfaceView.onPause();

		TrackerManager.getInstance().stopTracker();
		CameraDevice.getInstance().stop();
		MaxstAR.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.normal_tracking:
				TrackerManager.getInstance().setTrackingOption(TrackerManager.TrackingOption.NORMAL_TRACKING);
				break;

			case R.id.extended_tracking:
				TrackerManager.getInstance().setTrackingOption(TrackerManager.TrackingOption.EXTENDED_TRACKING);
				break;

			case R.id.multi_tracking:
				TrackerManager.getInstance().setTrackingOption(TrackerManager.TrackingOption.MULTI_TRACKING);
				break;
		}
	}

	private static final float TOUCH_TOLERANCE = 5;
	private float touchX;
	private float touchY;

	@Override
	public boolean onTouch(View v, final MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				touchX = x;
				touchY = y;
				imageTargetRenderer.touchStart(x, y);
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				float dx = Math.abs(x - touchX);
				float dy = Math.abs(y - touchY);
				if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
					imageTargetRenderer.touchMove(x, y);
					touchX = x;
					touchY = y;
				}
				break;
			}

			case MotionEvent.ACTION_UP:
				imageTargetRenderer.touchEnd(x, y);
				break;
		}

		return true;
	}
}
