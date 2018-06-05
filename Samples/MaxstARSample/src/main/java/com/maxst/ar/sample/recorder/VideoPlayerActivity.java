package com.maxst.ar.sample.recorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import com.maxst.ar.sample.R;

public class VideoPlayerActivity extends AppCompatActivity {

	public static final String VIDEO_FILE_NAME = "video_file_name";

	private VideoView videoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_player);

		String fileName = getIntent().getStringExtra(VIDEO_FILE_NAME);
		videoView = findViewById(R.id.video_view);
		videoView.setVideoPath(fileName);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!videoView.isPlaying()) {
			videoView.start();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (videoView.isPlaying()) {
			videoView.stopPlayback();
		}
	}
}
