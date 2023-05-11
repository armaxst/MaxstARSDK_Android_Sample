/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.instantFusionTracker;

import android.app.Activity;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.maxst.ar.MaxstAR;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;
import com.maxst.ar.sample.util.TrackerResultListener;

public class InstantFusionTrackerActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

	private InstantFusionTrackerRenderer instantTargetRenderer;
	private int preferCameraResolution = 0;
	private Button startTrackingButton;
	private GLSurfaceView glSurfaceView;
	private View guideView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_instant_fusion_tracker);

		startTrackingButton = (Button) findViewById(R.id.start_tracking);
		startTrackingButton.setOnClickListener(this);

		instantTargetRenderer = new InstantFusionTrackerRenderer(this);
		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(instantTargetRenderer);
		glSurfaceView.setOnTouchListener(this);

		guideView = (View)findViewById(R.id.guideView);
		instantTargetRenderer.listener = resultListener;

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
			TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_INSTANT_FUSION);
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
		TrackerManager.getInstance().destroyTracker();
		MaxstAR.deinit();
	}

	private static final float TOUCH_TOLERANCE = 5;
	private float touchStartX;
	private float touchStartY;
	private float translationX;
	private float translationY;

	@Override
	public boolean onTouch(View v, final MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				touchStartX = x;
				touchStartY = y;

				final float[] screen = new float[2];
				screen[0] = x;
				screen[1] = y;

				final float[] world = new float[3];

				TrackerManager.getInstance().getWorldPositionFromScreenCoordinate(screen, world);
				translationX = world[0];
				translationY = world[1];
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				float dx = Math.abs(x - touchStartX);
				float dy = Math.abs(y - touchStartY);
				if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
					touchStartX = x;
					touchStartY = y;

					final float[] screen = new float[2];
					screen[0] = x;
					screen[1] = y;

					final float[] world = new float[3];

					TrackerManager.getInstance().getWorldPositionFromScreenCoordinate(screen, world);
					float posX = world[0];
					float posY = world[1];

					instantTargetRenderer.setTranslate(posX - translationX, posY - translationY);
					translationX = posX;
					translationY = posY;
				}
				break;
			}

			case MotionEvent.ACTION_UP:
//				touchStartX = x;
//				touchStartY = y;
//
//				final float[] screen = new float[2];
//				screen[0] = x;
//				screen[1] = y;
//
//				final float[] modelMatrix = new float[16];
//
//				TrackerManager.getInstance().getWorldPositionFromScreenCoordinate(screen, modelMatrix);
//				instantTargetRenderer.setTouchPose(modelMatrix);
				break;
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.start_tracking:
				String text = startTrackingButton.getText().toString();
				if (text.equals(getResources().getString(R.string.start_tracking))) {
					TrackerManager.getInstance().findSurface();
					instantTargetRenderer.resetPosition();
					startTrackingButton.setText(getResources().getString(R.string.stop_tracking));
				} else {
					TrackerManager.getInstance().quitFindingSurface();
					startTrackingButton.setText(getResources().getString(R.string.start_tracking));
				}
				break;
		}
	}

	private TrackerResultListener resultListener = new TrackerResultListener() {
		@Override
		public void sendData(final String metaData) {
			(InstantFusionTrackerActivity.this).runOnUiThread(new Runnable(){
				@Override
				public void run() {

				}
			});
		}

		@Override
		public void sendFusionState(final int state) {
			(InstantFusionTrackerActivity.this).runOnUiThread(new Runnable(){
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
