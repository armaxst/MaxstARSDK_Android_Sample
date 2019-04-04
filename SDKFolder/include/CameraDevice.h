/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include "Types.h"
#include <string>
#include <list>

namespace maxstAR
{
	/**
	* @brief class for camera device handling
	*/
	class MAXSTAR_API CameraDevice
	{
	public:
		/**
		* @brief Camera focus mode
		*/
		enum FocusMode
		{
			// Continuous focus mode. This focus mode is proper for AR
			FOCUS_MODE_CONTINUOUS_AUTO = 1,

			// Single auto focus mode
			FOCUS_MODE_AUTO = 2
		};

		/**
		* @brief Video data flip direction
		*/
		enum FlipDirection
		{
			// Flip video horizontally
			HORIZONTAL = 0,

			// Flip video vertically
			VERTICAL = 1
		};

		/**
		* @brief get class instance
		*/
		static CameraDevice* getInstance();

		CameraDevice() {}
		virtual ~CameraDevice() {}

		/**
		* @brief Start camera preview
		* @param cameraId 0 is rear camera, 1 is face camera. camera index may depends on device.
		* @param width prefer camera width
		* @param height prefer camera height
		* @return 0 : camera open success 
		*		others : camera open fails
		*/
		virtual ResultCode start(int cameraId, int width, int height) = 0;

		/**
		* @brief Stop camera preview
		* @return 0 : camera stop success
		*		others : camera stop fails
		*/
		virtual ResultCode stop() = 0;

		/**
		* @return camera preview width
		*/
		virtual int getWidth() = 0;

		/**
		* @return camera preview height
		*/
		virtual int getHeight() = 0;

		/**
		* @return true if focus setting success
		*/
		virtual bool setFocusMode(FocusMode mode) = 0;
        
        /**
         * @brief Set Camera Zoom Scale
         * @param zoomScale Zoom value
         * @return result Zoom.
         */
        virtual bool setZoom(float zoomScale) = 0;
        
        /**
         * @brief Get Camera Device Max Zoom value.
         * @return Max Zoom value.
         */
        virtual float getMaxZoomValue() = 0;

		/**
		* @brief Turn on/off flash light
		*/
		virtual bool setFlashLightMode(bool toggle) = 0;

		/**
		* @brief Turn on/off auto white balance lock
		*/
		virtual bool setAutoWhiteBalanceLock(bool toggle) = 0;

		/**
		* @brief Flip video
		* @param direction Flip direction
		* @param toggle true for set, false for reset
		*/
		virtual void flipVideo(CameraDevice::FlipDirection direction, bool toggle) = 0;

        /**
         * @brief Get supported parameter key
         * @param key parameter key
         * @return Parameter value
         */
        virtual const char* getParam(const char* key) = 0;

		/**
		* @brief Get supported parameter key list
		* @return Parameter key list
		*/
		virtual std::list<std::string> getParamList() = 0;

		/**
		* @brief Set camera parameter  (Android only supported now)
		* @param key Parameter key 
		* @param toggle Parameter value
		* @return True if setting success
		*/
		virtual bool setParam(const char* key, bool toggle) = 0;

		/**
		* @brief Set camera parameter (Android only supported now)
		* @param key Parameter key
		* @param value Parameter value
		* @return True if setting success
		*/
		virtual bool setParam(const char* key, int value) = 0;

		/**
		* @brief Set camera parameter (Android only supported now)
		* @param key Parameter key
		* @param min Parameter min value
		* @param max Parameter max value
		* @return True if setting success
		*/
		virtual bool setParam(const char* key, int min, int max) = 0;

		/**
		* @brief Set camera parameter (Android only supported now)
		* @param key Parameter key
		* @param value Parameter value
		* @return True if setting success
		*/
		virtual bool setParam(const char* key, const char* value) = 0;

		/**
		* @brief Set camera calibration data
		* @param filename calibartion file path
		* @return True if setting success
		*/
		virtual bool setCalibrationData(const char* filename) = 0;

		/**
		* @brief Set new image data for tracking and background rendering (Free, Enterprise license key can activate this interface)
		* @param data image data bytes.
		* @param length image length
		* @param width image width
		* @param height image height
		* @param format image format
		* @return 
		*/
		virtual bool setNewFrame(Byte* data, int length, int width, int height, ColorFormat format) = 0;

		/**
		* @brief Set new image data for tracking and background rendering (Free, Enterprise license key can activate this interface)
		* @param data image data bytes.
		* @param length image length
		* @param width image width
		* @param height image height
		* @param format image format
		* @param timestamp image timestamp
		* @return 
		*/
		virtual bool setNewFrameAndTimestamp(Byte* data, int length, int width, int height, ColorFormat format, unsigned long long int timestamp) = 0;

		/**
		* @brief Get projection matrix. This is used for augmented objects projection and background rendering
		* @return 4x4 gl matrix (Column major)
		*/
		virtual float* getProjectionMatrix() = 0;

		/**
		* @brief Get projection matrix for background plane rendering
		* @return 4x4 gl matrix (Column major)
		*/
		virtual const float* getBackgroundPlaneProjectionMatrix() = 0;
	};
}
