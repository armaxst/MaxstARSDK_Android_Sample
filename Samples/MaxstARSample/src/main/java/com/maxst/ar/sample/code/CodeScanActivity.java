/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.code;

import android.app.Activity;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;
import com.maxst.ar.sample.util.TrackerResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class CodeScanActivity extends AppCompatActivity {

	private GLSurfaceView glSurfaceView;
	TextView codeFormatView;
	TextView codeValueView;
	private AutoFocusHandler autoFocusHandler;
	private int preferCameraResolution = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_code_scan);

		CodeScanRenderer renderer = new CodeScanRenderer();
		glSurfaceView = (GLSurfaceView)findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(renderer);

		codeFormatView = (TextView)findViewById(R.id.code_format);
		codeValueView = (TextView)findViewById(R.id.code_value);

		autoFocusHandler = new AutoFocusHandler();

		preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
		renderer.listener = resultListener;

		MaxstAR.init(this.getApplicationContext(), getResources().getString(R.string.app_key));
		MaxstAR.setScreenOrientation(getResources().getConfiguration().orientation);
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
		autoFocusHandler.start();
	}

	@Override
	protected void onPause() {
		super.onPause();

		glSurfaceView.onPause();
		TrackerManager.getInstance().stopTracker();
		CameraDevice.getInstance().stop();
		autoFocusHandler.stop();

		MaxstAR.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		TrackerManager.getInstance().destroyTracker();
		MaxstAR.deinit();
	}

	private TrackerResultListener resultListener = new TrackerResultListener() {
		@Override
		public void sendData(final String metaData) {
			(CodeScanActivity.this).runOnUiThread(new Runnable(){
				@Override
				public void run() {
					if (metaData != null && metaData.length() > 0) {
						try {
							JSONObject jsonObject = new JSONObject(metaData);
							codeFormatView.setText(String.format(Locale.US, "%s%s", (CodeScanActivity.this).getResources().getString(R.string.code_format), jsonObject.getString("Format")));
							codeValueView.setText(String.format(Locale.US, "%s%s", (CodeScanActivity.this).getResources().getString(R.string.code_value), jsonObject.getString("Value")));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}

		@Override
		public void sendFusionState(final int state) {
			(CodeScanActivity.this).runOnUiThread(new Runnable(){
				@Override
				public void run() {

				}
			});

		}
	};

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