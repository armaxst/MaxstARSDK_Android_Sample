/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include <string>
#include "Types.h"

#include "ITrackingResult.h"
#include "SurfaceThumbnail.h"
#include "SurfaceMesh.h"
#include "TrackingState.h"
#include "GuideInfo.h"

using namespace std;

namespace maxstAR
{
	/**
	* @brief Code scanner
	*/
	static const int TRACKER_TYPE_CODE_SCANNER = 0x01;

	/**
	* @brief Planar image tracker
	*/
	static const int TRACKER_TYPE_IMAGE = 0X02;

	/**
	* @brief Marker tracker
	*/
	static const int TRACKER_TYPE_MARKER = 0X04;

	/**
	* @brief Object tracker (Object data should be created via SLAM tracker)
	*/
	static const int TRACKER_TYPE_OBJECT = 0X08;

	/**
	* @brief Instant tracker
	*/
	static const int TRACKER_TYPE_INSTANT = 0x20;
	
    /**
	* @brief QR-Code tracker
	*/
	static const int TRACKER_TYPE_QR_TRACKER = 0x40;

	/**
	* @brief Control AR Engine
	*/
	class MAXSTAR_API TrackerManager
	{
	public:
		/**
		* @brief Additional tracking option.
		* 	0x01 : Normal Tracking (Image Tracker and Marker Tracker, Default Option for Image Tracker and Marker Tracker)
		*	0x02 : Extended Tracking (Image Tracker Only)
		* 	0x04 : Multi Target Tracking (Image Tracker Only)
		* 	0x80 : Enhanced Target Tracking (Marker Tracker Only)
		* 	0x08 : Jitter Reduction Activation (Marker and Image Tracker)
		* 	0x04 : Jitter Reduction Deactivation (Marker and Image Tracker, Default Option for Jitter Reduction)
		*/
		enum TrackingOption
		{
			NORMAL_TRACKING = 0x01,
			EXTENDED_TRACKING = 0x02,
			MULTI_TRACKING = 0x04,
			ENHANCED_TRACKING = 0x80,
			JITTER_REDUCTION_ACTIVATION = 0x08,
			JITTER_REDUCTION_DEACTIVATION = 0x10,
		};

	public:
		static TrackerManager* getInstance();

		/**
		* @brief Start AR engine. Only one tracking engine could be run at one time
		* @param trackerMask tracking engine type (Refer Types.h)
		*/
		virtual void startTracker(int trackerMask) = 0;

		/**
		* @brief Stop tracking engine
		*/
		virtual void stopTracker() = 0;

		/**
		* @brief Remove all tracking data (Map data and tracking result)
		*/
		virtual void destroyTracker() = 0;

		/**
		* @brief Add map file to candidate list.
		* @param trackingFileName absolute file path
		* @param isAndroidAssetFile flag for notify map file is located in assets folder (Android only)
		*/
		virtual void addTrackerData(const char* trackingFileName, bool isAndroidAssetFile = false) = 0;

		/**
		* @brief Remove map file from candidate list.
		* @param trackingFileName map file name. This name should be same which added.
		* If set "" (empty) file list will be cleared.
		*/
		virtual void removeTrackerData(const char* trackingFileName = "") = 0;

		/**
		* @brief Load map files in candidate list to memory. This method don't block main(UI) thread
		*/
		virtual void loadTrackerData() = 0;

		/**
		* @brief Get map files loading state. This is for UI expression.
		* @return true if map loading is completed
		*/
		virtual bool isTrackerDataLoadCompleted() = 0;

		/**
		* @brief Load vocabulary file.
		* @param filepath vocabulary file path
		*/
		virtual void setVocabulary(const char* filepath, bool assetFile = false) = 0;

		/**
		* @briedf Update tracking state. This function should be called before getTrackingResult and background rendering
		*/
		virtual TrackingState* updateTrackingState() = 0;
        
		/**
		* @brief Start to find the surface of an environment from a camera image
		*/
		virtual void findSurface() = 0;

		/**
		* @brief Stop to find the surface
		*/
		virtual void quitFindingSurface() = 0;

		/**
		* @brief Save the surface data to file
		* @param outputFileName file path (should be absolute path)
		* @return SurfaceThumbnail instance if true else null
		*/
		virtual SurfaceThumbnail* saveSurfaceData(const char* outputFileName) = 0;

		/**
		* @brief Get 3d world coordinate corresponding to given 2d screen position
		* @param screen screen touch x, y position
		* @param world world position x, y, z
		*/
		virtual void getWorldPositionFromScreenCoordinate(float* screen, float* world) = 0;

		///**
		//* @brief Get the number of keyframes included in surface data
		//* @return key frame count
		//*/
		//virtual int getKeyframeCount() = 0;

		///**
		//* @brief Get the number of features included in surface data
		//* @return feature point count
		//*/
		//virtual int getFeatureCount() = 0;

		/**
		* @brief Get guide information of SLAM after the findSurface method has been called
		* @return GuideInfo instance
		*/
		virtual GuideInfo* getGuideInfo() = 0;

		/**
		* @brief Get surface mesh information of the found surface after the findSurface method has been called
		* @return SurfaceMesh instance
		*/
		virtual SurfaceMesh* getSurfaceMesh() = 0;

		/**
		* @brief Set tracking options.
		* @param option
		*		1 : Normal Tracking (Image Tracker Only, Default Option for Image Tracker)
		*		2 : Extended Tracking (Image Tracker Only)
		*		4 : Multiple Target Tracking (Image Tracker Only)
		*		8 : Jitter Reduction Activation (Marker and Image Tracker)
		*		16 : Jitter Reduction Deactivation (Marker and Image Tracker, Default Option for Jitter Reduction)
		*/
		virtual void setTrackingOption(int option) = 0;

		virtual void saveFrames() = 0;
	};
}
