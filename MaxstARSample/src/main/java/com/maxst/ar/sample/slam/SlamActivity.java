/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.slam;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.SensorDevice;
import com.maxst.ar.SurfaceThumbnail;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.ARActivity;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;

import java.io.File;
import java.util.Locale;

public class SlamActivity extends ARActivity implements View.OnClickListener /*, View.OnTouchListener */ {

	private static final String TAG = SlamActivity.class.getSimpleName();

	private EditText mapFileNameEdit;
	private int preferCameraResolution = 0;
	private GLSurfaceView glSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slam);

		findViewById(R.id.find_surface).setOnClickListener(this);
		findViewById(R.id.quit_finding_surface).setOnClickListener(this);
		findViewById(R.id.save_map).setOnClickListener(this);

		mapFileNameEdit = (EditText) findViewById(R.id.map_file_name_edit);
		mapFileNameEdit.setText("Sample3D");

		ObjectTrackerRenderer renderer = new ObjectTrackerRenderer(this, TrackerManager.TRACKER_TYPE_SLAM, false);
		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(renderer);

		preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
	}


	@Override
	protected void onResume() {
		super.onResume();

		glSurfaceView.onResume();
		SensorDevice.getInstance().start();
		TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_SLAM);

		ResultCode resultCode = ResultCode.Success;

		switch (preferCameraResolution) {
			case 0:
				resultCode = CameraDevice.getInstance().start(0, 640, 480);
				break;

			case 1:
				resultCode = CameraDevice.getInstance().start(0, 1280, 720);
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
		glSurfaceView.onPause();

		TrackerManager.getInstance().quitFindingSurface();
		TrackerManager.getInstance().stopTracker();
		CameraDevice.getInstance().stop();
		SensorDevice.getInstance().stop();

		MaxstAR.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.find_surface:
				TrackerManager.getInstance().findSurface();
				break;

			case R.id.quit_finding_surface:
				TrackerManager.getInstance().quitFindingSurface();
				break;

			case R.id.save_map:
				File dir = new File(getExternalCacheDir().getAbsolutePath() + "/MaxstAR/3dmap");
				if (!dir.exists()) {
					boolean result = dir.mkdirs();
					Log.d(TAG, "Make directory result : " + result);
				}
				String mapFileName = dir.getAbsolutePath() + "/" + mapFileNameEdit.getText().toString() + ".3dmap";
				SurfaceThumbnail surfaceThumbnail = TrackerManager.getInstance().saveSurfaceData(mapFileName);
				if (surfaceThumbnail != null) {
					if (surfaceThumbnail.getWidth() != 0 && surfaceThumbnail.getHeight() != 0) {
						Toast.makeText(this, "Success to save surface data", Toast.LENGTH_SHORT).show();
						Log.i(TAG, String.format(Locale.US, "Thumbnail width:%d, height:%d, bpp:%d, length:%d",
								surfaceThumbnail.getWidth(), surfaceThumbnail.getHeight(), surfaceThumbnail.getBpp(), surfaceThumbnail.getLength()));
					}
					else {
						Toast.makeText(this, "Fail to save surface data", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(this, "Fail to save surface data", Toast.LENGTH_SHORT).show();
				}

				break;
		}
	}
}
