/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.qrcodeFusionTracker;

import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.TrackerResultListener;

public class QrCodeFusionTrackerActivity extends AppCompatActivity implements View.OnClickListener {

	private QrCodeFusionTrackerRenderer qrCodeTargetRenderer;
	private GLSurfaceView glSurfaceView;
	private TextView recognizedQrCodeView;
	private View guideView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qrcode_fusion_tracker);

		qrCodeTargetRenderer = new QrCodeFusionTrackerRenderer(this);
		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(qrCodeTargetRenderer);

		recognizedQrCodeView = (TextView)findViewById(R.id.recognized_QrCode);
		guideView = (View)findViewById(R.id.guideView);
		qrCodeTargetRenderer.listener = resultListener;

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
			TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_QR_FUSION);
			TrackerManager.getInstance().addTrackerData("{\"qr_fusion\":\"set_scale\",\"content\":\"Maxst1\", \"scale\":0.063}", false);
			TrackerManager.getInstance().addTrackerData("{\"qr_fusion\":\"set_scale\",\"content\":\"Maxst2\", \"scale\":0.063}", false);
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

		glSurfaceView.queueEvent(new Runnable() {
			@Override
			public void run() {
			}
		});

		glSurfaceView.onPause();

		CameraDevice.getInstance().stop();
		TrackerManager.getInstance().stopTracker();
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

	}

	private TrackerResultListener resultListener = new TrackerResultListener() {
		@Override
		public void sendData(final String metaData) {
			(QrCodeFusionTrackerActivity.this).runOnUiThread(new Runnable(){
				@Override
				public void run() {
					recognizedQrCodeView.setText("QRCODE : " + metaData);
				}
			});
		}

		@Override
		public void sendFusionState(final int state) {
			(QrCodeFusionTrackerActivity.this).runOnUiThread(new Runnable(){
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
