//
// Created by Jack on 2019-02-01.
//

#include <jni.h>
#include <stdlib.h>
#include <fstream>
#include <string>
#include <MaxstAR.h>
#include <CameraDevice.h>
#include <TrackerManager.h>
#include "ImageTrackerRenderer.h"

static int s_glViewWidth = 0;
static int s_glViewHeight = 0;
static ImageTrackerRenderer *imageTrackerRenderer = nullptr;

extern "C"
{

JNIEXPORT void JNICALL Java_com_maxst_ar_sample_imageTracker_ImageTrackerJni_onCreate
		(JNIEnv *env, jclass obj) {
	maxstAR::TrackerManager::getInstance()->addTrackerData("ImageTarget/Blocks.2dmap", true);
	maxstAR::TrackerManager::getInstance()->addTrackerData("ImageTarget/Glacier.2dmap", true);
	maxstAR::TrackerManager::getInstance()->addTrackerData("ImageTarget/Lego.2dmap", true);
	maxstAR::TrackerManager::getInstance()->loadTrackerData();
}

JNIEXPORT void JNICALL Java_com_maxst_ar_sample_imageTracker_ImageTrackerJni_onResume
		(JNIEnv *env, jclass obj) {
	maxstAR::CameraDevice::getInstance()->start(0, 1280, 720);
	maxstAR::TrackerManager::getInstance()->startTracker(maxstAR::TRACKER_TYPE_IMAGE);
}

JNIEXPORT void JNICALL Java_com_maxst_ar_sample_imageTracker_ImageTrackerJni_onPause
		(JNIEnv *env, jclass obj) {
	maxstAR::TrackerManager::getInstance()->stopTracker();
	maxstAR::CameraDevice::getInstance()->stop();
}

JNIEXPORT void JNICALL Java_com_maxst_ar_sample_imageTracker_ImageTrackerJni_onDestroy
		(JNIEnv *env, jclass obj) {
	maxstAR::TrackerManager::getInstance()->destroyTracker();
	maxstAR::deinit();
}

JNIEXPORT void JNICALL Java_com_maxst_ar_sample_imageTracker_ImageTrackerJni_setScreenOrientation
		(JNIEnv *env, jclass obj, jint orientation) {
	maxstAR::setScreenOrientation((maxstAR::ScreenOrientation) orientation);
}

JNIEXPORT void JNICALL Java_com_maxst_ar_sample_imageTracker_ImageTrackerJni_onSurfaceCreated
		(JNIEnv *env, jclass obj) {
	imageTrackerRenderer = new ImageTrackerRenderer();
}

JNIEXPORT void JNICALL Java_com_maxst_ar_sample_imageTracker_ImageTrackerJni_onSurfaceChanged
		(JNIEnv *env, jclass obj, jint width, jint height) {
	s_glViewWidth = width;
	s_glViewHeight = height;
	imageTrackerRenderer->onSizeChanged(s_glViewWidth, s_glViewHeight);
}

JNIEXPORT void JNICALL Java_com_maxst_ar_sample_imageTracker_ImageTrackerJni_onDrawFrame
		(JNIEnv *env, jclass obj) {
	imageTrackerRenderer->onDraw();
}
}