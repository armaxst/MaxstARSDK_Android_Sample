/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.maxst.ar.sample.util.SampleUtil;

public class SettingsActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		RadioButton sdResolution = (RadioButton) findViewById(R.id.sd_resolution);
		RadioButton hdResolution = (RadioButton) findViewById(R.id.hd_resolution);
		RadioButton FhdResolution = (RadioButton) findViewById(R.id.Fhd_resolution);

		sdResolution.setOnClickListener(this);
		hdResolution.setOnClickListener(this);
		FhdResolution.setOnClickListener(this);

		int resolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
		switch (resolution) {
			case 0:
				sdResolution.setChecked(true);
				break;

			case 1:
				hdResolution.setChecked(true);
				break;
			case 2:
				FhdResolution.setChecked(true);
				break;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.sd_resolution:
				SharedPreferences.Editor editor = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).edit();
				editor.putInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
				editor.apply();
				break;

			case R.id.hd_resolution:
				editor = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).edit();
				editor.putInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 1);
				editor.apply();
				break;

			case R.id.Fhd_resolution:
				editor = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).edit();
				editor.putInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 2);
				editor.apply();
				break;
		}
	}
}
