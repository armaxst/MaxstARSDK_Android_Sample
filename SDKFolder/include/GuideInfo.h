/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include "Types.h"
#include <mutex>

namespace maxstAR
{
	/**
	* @brief Contains surface's mesh data generated from slam tracking
	*/
    struct TagAnchor;
	class MAXSTAR_API GuideInfo
	{
	public:	
		virtual ~GuideInfo() {}

		/**
		* @brief Get a percentage of progress during an initialization step of SLAM
		* @return Slam initializing progress
		*/
		virtual float getInitializingProgress() = 0;

		/**
		* @return keyframe count
		*/
		virtual int getKeyframeCount() = 0;

		/**
		* @return feature count for guide
		*/
		virtual int getFeatureCount() = 0;

		/**
		* @return Get 2d screen positions of features for guide
		*/
		virtual float* getFeatureBuffer() = 0;

		/**
		* @return Get a bounding box of a scanned object
		*/
		virtual float* getBoundingBox() = 0;
        
        virtual char* getTagAnchors() = 0;
        
        virtual int getTagAnchorsLength() = 0;
	};
}
