/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include <string>
#include "Types.h"

namespace maxstAR
{
	/**
	* @brief List of eye id.
	* 	EYE_LEFT : Left eye id
	*	EYE_RIGHT : Right eye id
	* 	EYE_NUM : Number of ids
	*/
	enum EyeType
	{
		EYE_LEFT = 0,
		EYE_RIGHT = 1,
		EYE_NUM = 2,
	};

	/**
	* @brief List of calibration distance.
	* 	DISTANCE_NEAR : Calibration near distance
	*	DISTANCE_MIDDLE : Calibration middle distance
	* 	DISTANCE_FAR : Calibration far distance
	* 	DISTANCE_NUM : Number of ids
	*/
	enum DistanceType
	{
		DISTANCE_NEAR = 0,
		DISTANCE_MIDDLE = 1,
		DISTANCE_FAR = 2,
		DISTANCE_NUM = 3,
	};

	/**
	* @brief This class is for optical see-through wearable calibration
	*/
	class MAXSTAR_API WearableCalibration
	{
	public:
		static WearableCalibration * getInstance();

		/**
		* @brief Get wearable calibration activate state
		* @return true if wearable calibration is activated
		*/
		virtual bool isActivated() = 0;

		/**
		* @brief Init wearable calibration process
		* @param deviceName Supported devices name
		*/
		virtual bool init(std::string deviceName) = 0;

		/**
		* @brief Deinit wearable calibration process
		*/
		virtual void deinit() = 0;

		/**
		* @brief Set screen size of target device
		* @param width width of target device
		* @param height height of target device
		*/
		virtual void setSurfaceSize(int width, int height) = 0;

		/**
		* @brief Set camera to eye 4x4 pose matrix for calibration
		* @param eyeType Id of eye for calibration
		* @param distanceType Type of distance for calibration
		* @param pose Tracking pose for calibration
		*/
		virtual void setCameraToEyePose(int eyeType, int distanceType, float *pose) = 0;

		/**
		* @brief Get calibrated extrinsic parameter
		* @return 4x4 extrinsic camera matrix
		*/
		virtual float* getRt4x4(int eyeType) = 0;

		/**
		* @brief Get calibrated intrinsic parameter
		* @return 3x3 intrinsic camera matrix
		*/
		virtual float* getK3x3(int eyeType) = 0;

		/**
		* @brief Get calibrated viewport for opengl
		* @param eyeType Id of eye
		* @return viewport (x, y, width, height)
		*/
		virtual float* getViewport(int eyeType) = 0;

		/**
		* @brief Get calibrated projection matrix for opengl
		* @param eyeType Id of eye
		* @return projection matrix 4x4
		*/
		virtual float* getProjectionMatrix(int eyeType) = 0;

		/**
		* @brief Get calibration guide target position according to distance type
		* @param distanceType Type of distance
		* @param x Position x of guide target
		* @param y Position y of guide target
		* @param z Position z of guide target
		*/
		virtual void getDistancePos(int distanceType, float &x, float &y, float &z) = 0;

		/**
		* @brief Get orthogonal screen projection matrix
		* @return projection matrix 4x4
		*/
		virtual float* getScreenCoordinate() = 0;

		/**
		* @brief Get calibration guide target scale according to distance type for opengl
		* @param distanceType Type of distance
		* @param scale target scale (x, y, z)
		*/
		virtual void getTargetGLScale(int distanceType, float *scale) = 0;

		/**
		* @brief Get calibration guide target position according to distance type for opengl
		* @param distanceType Type of distance
		* @param position target position (x, y, z)
		*/
		virtual void getTargetGLPosition(int distanceType, float *position) = 0;

		/**
		* @brief Load default calibration profile
		* @param modelName Model name
		*/
		virtual void loadDefaultProfile(std::string modelName) = 0;

		/**
		* @brief Set calibrated info array
		* @param profile calibrated info array
		* @return True if setting is successed
		*/
		virtual bool setProfile(char *profile) = 0;

		/**
		* @brief Write calibrated info to file
		* @param fileName file path for write
		* @return True if file is writed
		*/
		virtual bool writeProfile(std::string fileName) = 0;

		/**
		* @brief Read calibrated info to file
		* @param fileName file path for read
		* @return True if file is read
		*/
		virtual bool readProfile(std::string filePath) = 0;

		///**
		//* @brief Set target scale
		//* @param scale Target scale
		//*/
		//virtual void setTargetScale(float scale) = 0;

		///**
		//* @brief Get target scale
		//* @return scale Target scale
		//*/
		//virtual float getTargetScale() = 0;
	};
}
