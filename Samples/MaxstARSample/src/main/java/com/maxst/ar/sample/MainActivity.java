/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.maxst.ar.sample.camera_config.CameraConfigureActivity;
import com.maxst.ar.sample.code.CodeScanActivity;
import com.maxst.ar.sample.imageTracker.ImageTrackerActivity;
import com.maxst.ar.sample.instantTracker.InstantTrackerActivity;
import com.maxst.ar.sample.slam.MapSelectActivity;
import com.maxst.ar.sample.slam.SlamActivity;
import com.maxst.ar.sample.wearable.WearableDeviceRenderingActivity;

public class MainActivity extends AppCompatActivity {

	private static final int MENU_GROUP_DEFAULT = 0;
	private static final int MENU_SETTING = 0;

	private ListView listView;
	private ArrayAdapter<String> adapter;
	private static final String[] MENUS = App.getContext().getResources().getStringArray(R.array.main_menus);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = findViewById(R.id.list_view);

		adapter = new ArrayAdapter<>(
				this,
				R.layout.view_main_menu,
				R.id.list_item,
				MENUS
		);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onItemClickListener);

	}

	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String menu = adapter.getItem(position);
			if (menu.equals(getString(R.string.image_tracker))) {
				startActivity(new Intent(MainActivity.this, ImageTrackerActivity.class));
			} else if (menu.equals(getString(R.string.instant_tracker))) {
				startActivity(new Intent(MainActivity.this, InstantTrackerActivity.class));
			} else if (menu.equals(getString(R.string.visual_slam))) {
				startActivity(new Intent(MainActivity.this, SlamActivity.class));
			} else if (menu.equals(getString(R.string.object_tracker))) {
				startActivity(new Intent(MainActivity.this, MapSelectActivity.class));
			} else if (menu.equals(getString(R.string.code_scan))) {
				startActivity(new Intent(MainActivity.this, CodeScanActivity.class));
			} else if (menu.equals(getString(R.string.see_through_hmd_rendering))) {
				startActivity(new Intent(MainActivity.this, WearableDeviceRenderingActivity.class));
			} else if (menu.equals(getString(R.string.camera_configuration))) {
				startActivity(new Intent(MainActivity.this, CameraConfigureActivity.class));
			} else if (menu.equals(getString(R.string.screen_recorder))) {

			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem setting = menu.add(MENU_GROUP_DEFAULT, MENU_SETTING, MENU_SETTING, R.string.setting);
		setting.setIcon(R.mipmap.settings);
		setting.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_SETTING:
				startActivity(new Intent(MainActivity.this, SettingsActivity.class));
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
