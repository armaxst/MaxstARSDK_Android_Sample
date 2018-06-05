package com.maxst.ar.sample.recorder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.projection.MediaProjectionManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.SensorDevice;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.instantTracker.InstantTrackerRenderer;
import com.maxst.screenrecorder.ScreenRecorder;
import com.maxst.screenrecorder.State;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ScreenRecorderActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

	private static final String TAG = ScreenRecorderActivity.class.getSimpleName();

	private static final String START_RECORD = "Start record";
	private static final String STOP_RECORD = "Stop record";

	private static final int REQ_RECORD = 0;
	private ScreenRecorder recorder;
	private InstantTrackerRenderer instantTargetRenderer;
	private Button startTrackingButton;
	private Button startRecordButton;
	private GLSurfaceView glSurfaceView;
	private MediaProjectionManager mediaProjectionManager;

	private String rootPath;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_screen_recorder);

		MaxstAR.init(getApplicationContext(), getResources().getString(R.string.app_key));
		MaxstAR.setScreenOrientation(getResources().getConfiguration().orientation);

		rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
				.getAbsolutePath() + "/ScreenRecorder/";
		File rootDir = new File(rootPath);
		if (!rootDir.exists()) {
			boolean result = rootDir.mkdirs();
			Log.d(TAG, "Make directory result : " + result);
		}

		startTrackingButton = findViewById(R.id.start_tracking);
		startTrackingButton.setOnClickListener(this);

		startRecordButton = findViewById(R.id.start_recording);
		startRecordButton.setOnClickListener(this);
		startRecordButton.setText(START_RECORD);

		instantTargetRenderer = new InstantTrackerRenderer(this);
		glSurfaceView = findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(instantTargetRenderer);
		glSurfaceView.setOnTouchListener(this);

		mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
		if (mediaProjectionManager != null) {
			startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQ_RECORD);
		} else {
			Toast.makeText(this, "Media projection service is not supported", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		glSurfaceView.onResume();

		SensorDevice.getInstance().start();
		TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_INSTANT);

		ResultCode resultCode = CameraDevice.getInstance().start(0, 1280, 720);
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

		recordScreen(false);

		recorder = null;

		TrackerManager.getInstance().destroyTracker();
		MaxstAR.deinit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_RECORD && resultCode == RESULT_OK) {
			Log.e(TAG, "granted");
			recorder = new ScreenRecorder(ScreenRecorderActivity.this, mediaProjectionManager, data);
		} else if (requestCode == REQ_RECORD) {
			Log.e(TAG, "denied");
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}

		MaxstAR.setScreenOrientation(newConfig.orientation);
	}

	private static final float TOUCH_TOLERANCE = 5;
	private float touchStartX;
	private float touchStartY;
	private float translationX;
	private float translationY;

	@Override
	public boolean onTouch(View v, final MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				touchStartX = x;
				touchStartY = y;

				final float[] screen = new float[2];
				screen[0] = x;
				screen[1] = y;

				final float[] world = new float[3];

				TrackerManager.getInstance().getWorldPositionFromScreenCoordinate(screen, world);
				translationX = world[0];
				translationY = world[1];
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				float dx = Math.abs(x - touchStartX);
				float dy = Math.abs(y - touchStartY);
				if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
					touchStartX = x;
					touchStartY = y;

					final float[] screen = new float[2];
					screen[0] = x;
					screen[1] = y;

					final float[] world = new float[3];

					TrackerManager.getInstance().getWorldPositionFromScreenCoordinate(screen, world);
					float posX = world[0];
					float posY = world[1];

					instantTargetRenderer.setTranslate(posX - translationX, posY - translationY);
					translationX = posX;
					translationY = posY;
				}
				break;
			}

			case MotionEvent.ACTION_UP:
				break;
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.start_tracking:
				String text = startTrackingButton.getText().toString();
				if (text.equals(getResources().getString(R.string.start_tracking))) {
					TrackerManager.getInstance().findSurface();
					instantTargetRenderer.resetPosition();
					startTrackingButton.setText(getResources().getString(R.string.stop_tracking));
				} else {
					TrackerManager.getInstance().quitFindingSurface();
					startTrackingButton.setText(getResources().getString(R.string.start_tracking));
				}
				break;

			case R.id.start_recording:
				if (startRecordButton.getText().toString().equalsIgnoreCase(START_RECORD)) {
					recordScreen(true);
					startRecordButton.setText(STOP_RECORD);
				} else {
					String videoName = recorder.getDstPath();
					recordScreen(false);
					startRecordButton.setText(START_RECORD);
					Intent intent = new Intent(ScreenRecorderActivity.this, VideoPlayerActivity.class);
					intent.putExtra(VideoPlayerActivity.VIDEO_FILE_NAME, videoName);
					startActivity(intent);
				}
				break;
		}
	}

	private void recordScreen(boolean toggle) {
		if (toggle) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_HHmmss");
			recorder.setDstPath(rootPath + sdf.format(new Date()) + ".mp4");
			recorder.record();
		} else {
			if (recorder != null) {
				if (recorder.getState() == State.RECORDING) {
					recorder.stop();
				}
			}
		}
	}
}
