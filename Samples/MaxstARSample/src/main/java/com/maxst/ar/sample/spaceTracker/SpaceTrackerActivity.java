/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.spaceTracker;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;
import com.maxst.ar.sample.util.TrackerResultListener;

import java.lang.ref.WeakReference;

public class SpaceTrackerActivity extends AppCompatActivity {

	private SpaceTrackerRenderer spaceTargetRenderer;
	private GLSurfaceView glSurfaceView;
	private int preferCameraResolution = 0;
	private View guideView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_space_tracker);

		spaceTargetRenderer = new SpaceTrackerRenderer(this);
		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(spaceTargetRenderer);

		guideView = (View)findViewById(R.id.guideView);
		spaceTargetRenderer.listener = resultListener;

		preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);

		MaxstAR.init(this.getApplicationContext(), getResources().getString(R.string.app_key));
		MaxstAR.setScreenOrientation(getResources().getConfiguration().orientation);
	}

	@Override
	protected void onResume() {
		super.onResume();

		glSurfaceView.onResume();

		if(TrackerManager.getInstance().isFusionSupported()) {
			CameraDevice.getInstance().setCameraApi(CameraDevice.CameraApi.Fusion);
			CameraDevice.getInstance().start(0, 1280, 720);
			TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_SPACE);
			TrackerManager.getInstance().addTrackerData("space_map.mmap", true);
			TrackerManager.getInstance().loadTrackerData();
		} else {
			String message = TrackerManager.requestARCoreApk(this);
			if(message != null) {
				Log.i("MaxstAR", message);
			}
		}

		MaxstAR.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

		glSurfaceView.onPause();
		CameraDevice.getInstance().stop();
		TrackerManager.getInstance().stopTracker();
		MaxstAR.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private TrackerResultListener resultListener = new TrackerResultListener() {
		@Override
		public void sendData(final String metaData) {
			(SpaceTrackerActivity.this).runOnUiThread(new Runnable(){
				@Override
				public void run() {
				}
			});
		}

		@Override
		public void sendFusionState(final int state) {
			(SpaceTrackerActivity.this).runOnUiThread(new Runnable(){
				@Override
				public void run() {
					if(state == 1) {
						guideView.setVisibility(View.INVISIBLE);
					} else {
						guideView.setVisibility(View.VISIBLE);
					}
				}
			});
		}
	};

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
