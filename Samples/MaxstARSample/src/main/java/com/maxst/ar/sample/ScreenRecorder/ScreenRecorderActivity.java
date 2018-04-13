package com.maxst.ar.sample.ScreenRecorder;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.maxst.ar.sample.R;
import com.maxst.screenrecorder.ScreenRecorder;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
			recorder.setDstPath(Environment.getExternalStoragePublicDirectory("Movies").getAbsolutePath() + "/test.mp4");
			recorder.record();
		} else if (requestCode == REQ_RECORD) {
			setRecordButton(true);
		}
	}


	private void setRecordButton(boolean isStopped) {
		if (isStopped) {
			record.setImageResource(R.drawable.stop_to_record);
			startAnimatedVectorDrawable(record.getDrawable());
			record.setTag(null);
		} else {
			record.setImageResource(R.drawable.record_to_stop);
			startAnimatedVectorDrawable(record.getDrawable());
			record.setTag(0);
		}


	}

	private void startAnimatedVectorDrawable(Drawable drawable) {
		if (drawable instanceof AnimatedVectorDrawable) {
			((AnimatedVectorDrawable) record.getDrawable()).start();
		} else if (drawable instanceof AnimatedVectorDrawableCompat) {
			((AnimatedVectorDrawableCompat) record.getDrawable()).start();
		}
	}
}
