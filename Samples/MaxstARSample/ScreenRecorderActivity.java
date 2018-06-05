package com.maxst.ar.sample.ScreenRecorder.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.maxst.ar.sample.R;
import com.maxst.ar.sample.databinding.ActivityScreenRecorderBinding;
import com.maxst.screenrecorder.ScreenRecorder;
import com.maxst.screenrecorder.State;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ScreenRecorderActivity extends AppCompatActivity {

	private static final String TAG = ScreenRecorderActivity.class.getSimpleName();

	private static final int REQ_RECORD = 0;
	private ScreenRecorder recorder;
	private ActivityScreenRecorderBinding binding;

	private FileListAdapter fileListAdapter;

	private String rootPath;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
				.getAbsolutePath() + "/ScreenRecorder/";
		binding = DataBindingUtil.setContentView(this, R.layout.activity_screen_recorder);

		fileListAdapter = new FileListAdapter(file -> {
			Log.e(TAG, "onClick: " + file.getName());
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(file.getAbsolutePath()));
			intent.setDataAndType(Uri.parse(file.getAbsolutePath()), "video/mp4");
			startActivity(intent);
		});
		refresh();
		binding.recyclerView.setAdapter(fileListAdapter);
		binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
		binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
		binding.startSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				ScreenRecorder.Companion.requestCaptureIntent(this, REQ_RECORD);
			} else {
				if (recorder != null) {
					recorder.stop();
					refresh();
					binding.recyclerView.scrollToPosition(fileListAdapter.getItemCount() - 1);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (recorder != null) {
			if (recorder.getState() != State.STOPPED) {
				try {
					recorder.stop();
					synchronized (this) {
						wait(1000); //sorry
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_RECORD && resultCode == RESULT_OK) {
			Log.e(TAG, "granted");
			recorder = new ScreenRecorder(ScreenRecorderActivity.this, data);
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_HHmmss");
			recorder.setDstPath(rootPath + sdf.format(new Date()) + ".mp4");
			recorder.record();
		} else if (requestCode == REQ_RECORD) {
			Log.e(TAG, "denied");
			binding.startSwitch.setChecked(false);
		}
	}

	public void refresh() {
		List<File> files = Arrays.asList(new File(rootPath).listFiles());
		fileListAdapter.setFiles(files);
	}

}
