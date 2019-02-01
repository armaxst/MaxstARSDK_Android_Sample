LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := MaxstAR-prebuilt
LOCAL_SRC_FILES := ../../../SDKFolder/Build/$(TARGET_ARCH_ABI)/libMaxstAR.so
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../../SDKFolder/include
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := ImageTrackerSample
LOCAL_SRC_FILES := \
	BackgroundCameraQuad.cpp \
	BaseModel.cpp \
	ImageTrackerJni.cpp \
	ImageTrackerRenderer.cpp \
	Quad.cpp \
	ColoredCube.cpp \

LOCAL_CFLAGS := -std=c++11
LOCAL_LDLIBS := -llog -landroid -lz -lGLESv2
LOCAL_SHARED_LIBRARIES := MaxstAR-prebuilt
include $(BUILD_SHARED_LIBRARY)


