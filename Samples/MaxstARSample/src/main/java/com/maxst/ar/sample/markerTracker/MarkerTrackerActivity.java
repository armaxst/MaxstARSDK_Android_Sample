/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.markerTracker;

import android.app.Activity;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;
import com.maxst.ar.sample.util.TrackerResultListener;

public class MarkerTrackerActivity extends AppCompatActivity implements View.OnClickListener {

	private MarkerTrackerRenderer markerTargetRenderer;
	private GLSurfaceView glSurfaceView;
	private int preferCameraResolution = 0;
	private TextView recognizedMarkerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_marker_tracker);

		markerTargetRenderer = new MarkerTrackerRenderer(this);
		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(markerTargetRenderer);

		recognizedMarkerView = (TextView)findViewById(R.id.recognized_marker);
		markerTargetRenderer.listener = resultListener;

		MaxstAR.init(this.getApplicationContext(), getResources().getString(R.string.app_key));
		MaxstAR.setScreenOrientation(getResources().getConfiguration().orientation);

		TrackerManager.getInstance().addTrackerData("{\"marker\":\"scale\",\"all\":1}", true);
		findViewById(R.id.normal_tracking).setOnClickListener(this);
		findViewById(R.id.enhanced_tracking).setOnClickListener(this);

		TrackerManager.getInstance().loadTrackerData();
		preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();

		glSurfaceView.onResume();
		TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_MARKER);

		ResultCode resultCode = ResultCode.Success;
		switch (preferCameraResolution) {
			case 0:
				resultCode = CameraDevice.getInstance().start(0, 640, 480);
				break;

			case 1:
				resultCode = CameraDevice.getInstance().start(0, 1280, 720);
				break;

			case 2:
				resultCode = CameraDevice.getInstance().start(0, 1920, 1080);
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
		TrackerManager.getInstance().destroyTracker();
		MaxstAR.deinit();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.normal_tracking:
				TrackerManager.getInstance().setTrackingOption(TrackerManager.TrackingOption.NORMAL_TRACKING);
				//TrackerManager.getInstance().setTrackingOption(TrackerManager.TrackingOption.JITTER_REDUCTION_DEACTIVATION);
				TrackerManager.getInstance().setTrackingOption(TrackerManager.TrackingOption.JITTER_REDUCTION_ACTIVATION);
				break;

			case R.id.enhanced_tracking:
				TrackerManager.getInstance().setTrackingOption(TrackerManager.TrackingOption.ENHANCED_TRACKING);
				TrackerManager.getInstance().setTrackingOption(TrackerManager.TrackingOption.JITTER_REDUCTION_ACTIVATION);
				break;
		}
	}

	private TrackerResultListener resultListener = new TrackerResultListener() {
		@Override
		public void sendData(final String metaData) {
			(MarkerTrackerActivity.this).runOnUiThread(new Runnable(){
				@Override
				public void run() {
					recognizedMarkerView.setText("Recognized Marker ID : "+ metaData);
				}
			});
		}

		@Override
		public void sendFusionState(final int state) {
			(MarkerTrackerActivity.this).runOnUiThread(new Runnable(){
				@Override
				public void run() {
				}
			});

		}
	};

	static final int NONE = 0;
	static final int ZOOM = 1;
	int mode = NONE;

	float oldDist = 1f;
	float newDist = 1f;

	private float deltaMagnitudediff;
	private	float CurrentCameraZoom=0.0f;
	private float CameraZoomSpeed = 0.02f;

	public boolean onTouchEvent(MotionEvent event) {
		int act = event.getAction();

		switch(act & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_MOVE:
				if (mode == ZOOM) {
					newDist = spacing(event);

					deltaMagnitudediff = newDist - oldDist;
					CurrentCameraZoom+=deltaMagnitudediff * CameraZoomSpeed;

					if(CurrentCameraZoom > CameraDevice.getInstance().getMaxZoomValue())
						CurrentCameraZoom = CameraDevice.getInstance().getMaxZoomValue();
					else if(CurrentCameraZoom < 0)
						CurrentCameraZoom = 0.0f;

					CameraDevice.getInstance().setZoom((int)CurrentCameraZoom);
					oldDist = newDist;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				mode = ZOOM;

				newDist = spacing(event);
				oldDist = spacing(event);

				break;
			case MotionEvent.ACTION_CANCEL:
			default :
				break;
		}

		return super.onTouchEvent(event);
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float)Math.sqrt(x * x + y * y);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}

		MaxstAR.setScreenOrientation(newConfig.orientation);
	}
}
