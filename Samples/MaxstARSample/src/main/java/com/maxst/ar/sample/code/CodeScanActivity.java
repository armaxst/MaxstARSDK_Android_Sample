/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.code;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingState;
import com.maxst.ar.sample.ARActivity;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class CodeScanActivity extends ARActivity implements View.OnClickListener {

	private GLSurfaceView glSurfaceView;
	private CodeScanResultHandler resultHandler;
	private Button startScan;
	TextView codeFormatView;
	TextView codeValueView;
	private AutoFocusHandler autoFocusHandler;
	private int preferCameraResolution = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_code_scan);

		startScan = (Button)findViewById(R.id.start_scan);
		startScan.setOnClickListener(this);

		CodeScanRenderer renderer = new CodeScanRenderer();
		glSurfaceView = (GLSurfaceView)findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(renderer);

		codeFormatView = (TextView)findViewById(R.id.code_format);
		codeValueView = (TextView)findViewById(R.id.code_value);

		resultHandler = new CodeScanResultHandler(this);
		autoFocusHandler = new AutoFocusHandler();

		preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();

		glSurfaceView.onResume();
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

		//CameraDevice.getInstance().setAutoWhiteBalanceLock(true); // For ODG-R7 preventing camera flickering

		TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_CODE_SCANNER);

		resultHandler.sendEmptyMessageDelayed(0, 33);
		startScan.setEnabled(false);
		startScan.setText(getString(R.string.now_scanning));
		autoFocusHandler.start();
	}

	@Override
	protected void onPause() {
		super.onPause();

		glSurfaceView.onPause();

		resultHandler.removeCallbacksAndMessages(null);
		TrackerManager.getInstance().stopTracker();
		CameraDevice.getInstance().stop();
		autoFocusHandler.stop();

		MaxstAR.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		resultHandler = null;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.start_scan:
				TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_CODE_SCANNER);
				resultHandler.sendEmptyMessageDelayed(0, 33);
				startScan.setEnabled(false);
				startScan.setText(getString(R.string.now_scanning));
				codeFormatView.setText("");
				codeValueView.setText("");
				break;
		}
	}

	private static class CodeScanResultHandler extends Handler {

		WeakReference<CodeScanActivity> activityWeakReference;

		CodeScanResultHandler(CodeScanActivity activity) {
			activityWeakReference = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg) {

			CodeScanActivity activity = activityWeakReference.get();

			if (activity == null) {
				return;
			}

			TrackingState state = TrackerManager.getInstance().updateTrackingState();
			String code = state.getCodeScanResult();

			if (code != null && code.length() > 0) {
				try {
					JSONObject jsonObject = new JSONObject(code);
					activity.codeFormatView.setText(String.format(Locale.US, "%s%s", activity.getResources().getString(R.string.code_format), jsonObject.getString("Format")));
					activity.codeValueView.setText(String.format(Locale.US, "%s%s", activity.getResources().getString(R.string.code_value), jsonObject.getString("Value")));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				TrackerManager.getInstance().stopTracker();
				TrackerManager.getInstance().destroyTracker();
				activity.startScan.setEnabled(true);
				activity.startScan.setText(activity.getString(R.string.start_scan));
			} else {
				sendEmptyMessageDelayed(0, 30);
			}
		}
	}

	private static class AutoFocusHandler extends Handler {

		void start() {
			sendEmptyMessage(0);
		}

		void stop() {
			removeCallbacksAndMessages(null);
		}

		@Override
		public void handleMessage(Message msg) {
			CameraDevice.getInstance().setFocusMode(CameraDevice.FocusMode.FOCUS_MODE_AUTO);
			sendEmptyMessageDelayed(0, 3000);
		}
	}
}