/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.webview;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.ARActivity;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;

public class WebViewAugmentActivity extends ARActivity implements View.OnClickListener {

	private GLSurfaceView glSurfaceView;
	private int preferCameraResolution = 0;
	private WebViewAugmentRenderer webViewAugmentRenderer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_web_view_augment);

		WebViewContainer webViewContainer = findViewById(R.id.web_view_container);
		webViewAugmentRenderer = new WebViewAugmentRenderer(webViewContainer);
		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(webViewAugmentRenderer);

		WebView webView = findViewById(R.id.web_view);
		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
		webView.loadUrl("https://developer.maxst.com");

		TrackerManager.getInstance().addTrackerData("ImageTarget/Lego.2dmap", true);
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

		glSurfaceView.queueEvent( () -> webViewAugmentRenderer.onPause());
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
}
