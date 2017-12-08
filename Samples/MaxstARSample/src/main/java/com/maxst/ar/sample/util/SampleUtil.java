package com.maxst.ar.sample.util;

public class SampleUtil {

	public static final String PREF_NAME = "pref";
	public static final String PREF_KEY_CAM_RESOLUTION = "cam_resolution";

	public static float[] getNormalizedTouchPoint(float touchX, float touchY, int screenWidth, int screenHeight) {
		float [] newPoint = new float[2];
		float normalizedX = 2 * touchX / screenWidth - 1;
		float normalizedY = 1 - 2 * touchY / screenHeight;
		newPoint[0] = normalizedX;
		newPoint[1] = normalizedY;
		return newPoint;
	}
}
