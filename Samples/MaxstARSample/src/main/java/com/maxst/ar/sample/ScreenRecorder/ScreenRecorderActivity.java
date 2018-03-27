package com.maxst.ar.sample.ScreenRecorder;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.maxst.ar.sample.R;
import com.maxst.screenrecorder.ScreenRecorder;

public class ScreenRecorderActivity extends AppCompatActivity {

	private static final String TAG = ScreenRecorderActivity.class.getSimpleName();

	private static final int REQ_RECORD = 0;

	private ScreenRecorder recorder;
	private ImageView record;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_recorder);

		record = findViewById(R.id.record);
		record.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					if (record.getTag() == null) {
						/**
						 * First of all, request capture-intent to prepare recording
						 */
						ScreenRecorder.Companion.requestCaptureIntent(ScreenRecorderActivity.this, REQ_RECORD);
						setRecordButton(false);
					} else {
						setRecordButton(true);
						if (recorder != null) {
							recorder.stop();
						}
					}
				} else {
					setRecordButton(true);
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_RECORD && resultCode == RESULT_OK) {
			/**
			 * and then, start to record!
			 */
			recorder = new ScreenRecorder(ScreenRecorderActivity.this, data);
			recorder.record();
		} else if (requestCode == REQ_RECORD) {
			setRecordButton(true);
		}
	}

	private void setRecordButton(boolean isStopped) {
		if (isStopped) {
			record.setImageResource(R.drawable.stop_to_record);
			((AnimatedVectorDrawable) record.getDrawable()).start();
			record.setTag(null);
		} else {
			record.setImageResource(R.drawable.record_to_stop);
			((AnimatedVectorDrawable) record.getDrawable()).start();
			record.setTag(0);
		}
	}
}
