/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.imageTracker;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import com.maxst.ar.sample.ARActivity;
import com.maxst.ar.sample.R;

public class ImageTrackerActivity extends ARActivity {

	static {
		System.loadLibrary("ImageTrackerSample");
	}

	private GLSurfaceView glSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_tracker);

		ImageTrackerRenderer imageTargetRenderer = new ImageTrackerRenderer();
		glSurfaceView = findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(imageTargetRenderer);

		ImageTrackerJni.onCreate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();

		ImageTrackerJni.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();

		ImageTrackerJni.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		ImageTrackerJni.onDestroy();
	}
}
