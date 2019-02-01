package com.maxst.ar.sample.imageTracker;

public class ImageTrackerJni {

	public static native void onCreate();
	public static native void onResume();
	public static native void onPause();
	public static native void onDestroy();
	public static native void setScreenOrientation(int orientation);

	public static native void onSurfaceCreated();
	public static native void onSurfaceChanged(int width, int height);
	public static native void onDrawFrame();
}
