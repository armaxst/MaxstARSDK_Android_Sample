/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.imageTracker;

import android.app.Activity;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;

public class ImageTrackerActivity extends AppCompatActivity implements View.OnClickListener {

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

		MaxstAR.init(this.getApplicationContext(), getResources().getString(R.string.app_key));
		MaxstAR.setScreenOrientation(getResources().getConfiguration().orientation);

		TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_IMAGE);
		TrackerManager.getInstance().addTrackerData("ImageTarget/Glacier.2dmap", true);
		TrackerManager.getInstance().addTrackerData("ImageTarget/Lego.2dmap", true);
		TrackerManager.getInstance().addTrackerData("ImageTarget/Blocks.2dmap", true);
//		TrackerManager.getInstance().addTrackerData("{\"image\":\"add_image\",\"image_path\":\"ImageTarget/Blocks.png\",\"image_width\":0.26,\"inclusion\":[{\"x\":50, \"y\":100, \"width\":400, \"height\":400}, {\"x\":400, \"y\":80, \"width\":400, \"height\":400}], \"exclusion\":[{\"x\":200, \"y\":200, \"width\":150, \"height\":150}]}", true);
		//TrackerManager.getInstance().addTrackerData("{\"image\":\"add_image\",\"image_path\":\"ImageTarget/Blocks.png\",\"image_width\":0.26}", true);
		//TrackerManager.getInstance().addTrackerData("{\"image\":\"add_image\",\"image_path\":\"ImageTarget/Glacier.png\",\"image_width\":0.26}", true);
		//TrackerManager.getInstance().addTrackerData("{\"image\":\"add_image\",\"image_path\":\"/sdcard/Download/sample/Blocks.png\",\"image_width\":0.26}", false);
		//TrackerManager.getInstance().addTrackerData("{\"image\":\"add_image\",\"image_path\":\"/sdcard/Download/sample/Glacier.png\",\"image_width\":0.26}", false);
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
		TrackerManager.getInstance().destroyTracker();
		MaxstAR.deinit();
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
