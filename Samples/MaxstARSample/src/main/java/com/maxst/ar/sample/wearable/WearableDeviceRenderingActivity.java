/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.wearable;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.WearableCalibration;
import com.maxst.ar.sample.ARActivity;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;
import com.maxst.ar.wearable.WearableDeviceController;

public class WearableDeviceRenderingActivity extends ARActivity implements View.OnClickListener {

	private static final String TAG = WearableDeviceRenderingActivity.class.getSimpleName();

	private GLSurfaceView glSurfaceView;
	private int preferCameraResolution = 0;
	private WearableDeviceController wearableDeviceController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_wearable_device_rendering);

		findViewById(R.id.normal_tracking).setOnClickListener(this);
		findViewById(R.id.extended_tracking).setOnClickListener(this);
		findViewById(R.id.multi_tracking).setOnClickListener(this);

		wearableDeviceController = WearableDeviceController.createDeviceController(this);
		WearableDeviceRenderer imageTargetRenderer = new WearableDeviceRenderer(this, wearableDeviceController);
		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(imageTargetRenderer);
//		glSurfaceView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

		TrackerManager.getInstance().addTrackerData("ImageTarget/Blocks.2dmap", true);
		TrackerManager.getInstance().addTrackerData("ImageTarget/Glacier.2dmap", true);
		TrackerManager.getInstance().addTrackerData("ImageTarget/Lego.2dmap", true);
		TrackerManager.getInstance().loadTrackerData();

		preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);

		if (!wearableDeviceController.isSupportedWearableDevice()) {
			DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
			int offsetX = displayMetrics.widthPixels / 2;
			int offsetY = 0;

			Toast toast = Toast.makeText(this, "This android device is not in our supported list!", Toast.LENGTH_LONG);
			ViewGroup group = (ViewGroup) toast.getView();
			TextView messageTextView = (TextView) group.getChildAt(0);
			messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

			toast.setGravity(Gravity.CENTER, offsetX, offsetY);
			toast.show();
//			Toast.makeText(this, "This android device is not in our supported list!", Toast.LENGTH_LONG).show();
			finish();
		}

		WearableCalibration.getInstance().init(wearableDeviceController.getModelName());
		boolean readResult = WearableCalibration.getInstance().readActiveProfile(MaxstAR.getApplicationContext(), wearableDeviceController.getModelName());
		Log.d(TAG, "Read Active Profile result : " + readResult);
	}

	@Override
	protected void onResume() {
		super.onResume();

		wearableDeviceController.setStereoMode(true);
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

			case 3:
				resultCode = CameraDevice.getInstance().start(0, 2560, 1440);
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

		wearableDeviceController.setStereoMode(false);

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

//	ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
//
//		@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//		@Override
//		public void onGlobalLayout() {
//			wearableDeviceController.setStereoMode(true);
//			glSurfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
//		}
//	};
}
