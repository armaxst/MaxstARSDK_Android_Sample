/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.maxst.ar.sample.imageTracker.ImageTrackerActivity;

public class MainActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.image_target).setOnClickListener(this);
		findViewById(R.id.settings).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_target:
				startActivity(new Intent(MainActivity.this, ImageTrackerActivity.class));
				break;
			case R.id.settings:
				startActivity(new Intent(MainActivity.this, SettingsActivity.class));
				break;
		}
	}
}
