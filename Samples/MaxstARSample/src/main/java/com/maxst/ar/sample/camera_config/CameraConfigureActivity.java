/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.camera_config;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.ARActivity;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;

public class CameraConfigureActivity extends ARActivity {

	private static final String TAG = CameraConfigureActivity.class.getSimpleName();

	private GLSurfaceView glSurfaceView;
	private int preferCameraWidth = 0;
	private int preferCameraHeight = 0;

	private int currentDirectionViewId;
	private int currentFlashViewId;
	private int currentFocusViewId;
	private boolean autoWhiteBalanceLocked = false;
	private int preferCameraResolution = 0;

	private int cameraNumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_camera_configuration);

		findViewById(R.id.front_camera).setOnClickListener(cameraDirectionClickListener);
		findViewById(R.id.rear_camera).setOnClickListener(cameraDirectionClickListener);
		findViewById(R.id.flash_on).setOnClickListener(flashLightClickListener);
		findViewById(R.id.flash_off).setOnClickListener(flashLightClickListener);
		findViewById(R.id.focus_continuous).setOnClickListener(focusModeClickListener);
		findViewById(R.id.focus_auto).setOnClickListener(focusModeClickListener);
		findViewById(R.id.white_balance_auto).setOnClickListener(wbClickListener);
		findViewById(R.id.white_balance_lock).setOnClickListener(wbClickListener);
		((CheckBox)findViewById(R.id.horizontal_flip)).setOnCheckedChangeListener(checkedChangeListener);
		((CheckBox)findViewById(R.id.vertical_flip)).setOnCheckedChangeListener(checkedChangeListener);
		((CheckBox)findViewById(R.id.resize_camera_view)).setOnCheckedChangeListener(checkedChangeListener);

		currentDirectionViewId = R.id.rear_camera;
		currentFlashViewId = R.id.flash_off;
		currentFocusViewId = R.id.focus_continuous;

		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(new CameraConfigureRenderer(this));

		TrackerManager.getInstance().addTrackerData("ImageTarget/Blocks.2dmap", true);
		TrackerManager.getInstance().addTrackerData("ImageTarget/Glacier.2dmap", true);
		TrackerManager.getInstance().addTrackerData("ImageTarget/Lego.2dmap", true);
		TrackerManager.getInstance().loadTrackerData();

		preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();

		glSurfaceView.onResume();
		ResultCode resultCode = ResultCode.Success;

		switch (preferCameraResolution) {
			case 0:
				preferCameraWidth = 640;
				preferCameraHeight = 480;
				resultCode = CameraDevice.getInstance().start(0, 640, 480);
				break;

			case 1:
				preferCameraWidth = 1280;
				preferCameraHeight = 720;
				resultCode = CameraDevice.getInstance().start(0, 1280, 720);
				break;

			case 2:
				preferCameraWidth = 1920;
				preferCameraHeight = 1080;
				resultCode = CameraDevice.getInstance().start(0, 1920, 1080);
				break;
		}

		if (resultCode != ResultCode.Success) {
			Toast.makeText(this, R.string.camera_open_fail, Toast.LENGTH_SHORT).show();
			finish();
		}

		TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_IMAGE);

		MaxstAR.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

		glSurfaceView.onPause();

		TrackerManager.getInstance().stopTracker();
		CameraDevice.getInstance().stop();
		MaxstAR.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private RadioButton.OnClickListener cameraDirectionClickListener = new RadioButton.OnClickListener() {

		@Override
		public void onClick(View view) {
			if (currentDirectionViewId == view.getId()) {
				return;
			}

			currentDirectionViewId = view.getId();

			switch (view.getId()) {
				case R.id.front_camera:
					CameraDevice.getInstance().stop();
					cameraNumber = 1;
					CameraDevice.getInstance().start(cameraNumber, preferCameraWidth, preferCameraHeight);

					CameraDevice.getInstance().flipVideo(CameraDevice.FlipDirection.VERTICAL, true);
					break;

				case R.id.rear_camera:
					CameraDevice.getInstance().stop();
					cameraNumber = 0;
					CameraDevice.getInstance().start(cameraNumber, preferCameraWidth, preferCameraHeight);

					CameraDevice.getInstance().flipVideo(CameraDevice.FlipDirection.VERTICAL, false);
					break;
			}
		}
	};

	private RadioButton.OnClickListener flashLightClickListener = new RadioButton.OnClickListener() {

		@Override
		public void onClick(View view) {
			if (currentFlashViewId == view.getId()) {
				return;
			}

			currentFlashViewId = view.getId();

			switch (view.getId()) {
				case R.id.flash_off:
					CameraDevice.getInstance().setFlashLightMode(false);
					break;

				case R.id.flash_on:
					CameraDevice.getInstance().setFlashLightMode(true);
					break;
			}
		}
	};

	private RadioButton.OnClickListener focusModeClickListener = new RadioButton.OnClickListener() {

		@Override
		public void onClick(View view) {
			if (currentFocusViewId == view.getId()) {
				return;
			}

			currentFocusViewId = view.getId();

			switch (view.getId()) {
				case R.id.focus_continuous:
					CameraDevice.getInstance().setFocusMode(CameraDevice.FocusMode.FOCUS_MODE_CONTINUOUS_AUTO);
					break;

				case R.id.focus_auto:
					CameraDevice.getInstance().setFocusMode(CameraDevice.FocusMode.FOCUS_MODE_AUTO);
					break;
			}
		}
	};

	private CheckBox.OnCheckedChangeListener checkedChangeListener = new CheckBox.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
			switch (compoundButton.getId()) {
				case R.id.horizontal_flip:
					CameraDevice.getInstance().flipVideo(CameraDevice.FlipDirection.HORIZONTAL, b);
					break;

				case R.id.vertical_flip:
					CameraDevice.getInstance().flipVideo(CameraDevice.FlipDirection.VERTICAL, b);
					break;

				case R.id.resize_camera_view:
					DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
					ViewGroup.LayoutParams layoutParams = glSurfaceView.getLayoutParams();
					if (b) {
						layoutParams.width = displayMetrics.widthPixels / 2;
						layoutParams.height = displayMetrics.widthPixels / 2;
						glSurfaceView.setLayoutParams(layoutParams);
					} else {
						layoutParams.width = displayMetrics.widthPixels;
						layoutParams.height = displayMetrics.heightPixels;
						glSurfaceView.setLayoutParams(layoutParams);
					}
					break;
			}
		}
	};

	private RadioButton.OnClickListener wbClickListener = new RadioButton.OnClickListener() {

		@Override
		public void onClick(View view) {

			switch (view.getId()) {
				case R.id.white_balance_auto:
					CameraDevice.getInstance().setAutoWhiteBalanceLock(false);
					break;

				case R.id.white_balance_lock:
					CameraDevice.getInstance().setAutoWhiteBalanceLock(true);
					break;
			}
		}
	};
}
