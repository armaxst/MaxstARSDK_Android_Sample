/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.slam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maxst.ar.sample.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapSelectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_select);

		List<File> mapFileList = new ArrayList<>();
		File[] mapFiles = new File(getExternalCacheDir().getAbsolutePath() + "/MaxstAR/3dmap").listFiles();

		if (mapFiles == null) {
			Toast.makeText(this, getResources().getString(R.string.no_map_files_exist), Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		mapFileList.addAll(Arrays.asList(mapFiles));

		ListView mapFileListView = (ListView) findViewById(R.id.map_file_list);
		mapFileListView.setOnItemClickListener(onItemClickListener);
		ListViewAdapter adapter = new ListViewAdapter(this, mapFileList);
		mapFileListView.setAdapter(adapter);
	}

	AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			File mapFile = (File) view.getTag();
			Intent intent = new Intent(MapSelectActivity.this, ObjectTrackerActivity.class);
			intent.putExtra(ObjectTrackerActivity.MAP_FILE_NAME_KEY, mapFile.getAbsolutePath());
			startActivity(intent);
		}
	};

	private static class ListViewAdapter extends BaseAdapter {

		private List<File> listViewItemList = new ArrayList<>();
		private Context context;

		ListViewAdapter(Context context, List<File> list) {
			this.context = context;
			listViewItemList = list;
		}

		@Override
		public int getCount() {
			return listViewItemList.size();
		}

		@Override
		public File getItem(int position) {
			return listViewItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.map_file_name_list_item, null);
			}

			File mapFile = getItem(position);
			convertView.setTag(mapFile);
			TextView previewSizeText = (TextView) convertView.findViewById(R.id.map_file_name);
			previewSizeText.setText(mapFile.getName());
			return convertView;
		}
	}
}
