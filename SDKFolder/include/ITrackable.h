/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include <string>

using namespace std;

namespace maxstAR
{
	/**
	* @brief Container for individual tracking information
	*/
	class ITrackable
	{
	public:
		ITrackable() { }

		virtual ~ITrackable() { }

		/**
		* @return 4x4 gl matrix for tracking pose
		*/
		virtual float * getPose() = 0;

		/**
		* @return tracking target name name (file name without extension)
		*/
		virtual const char* getName() = 0;

		/**
		* @return tracking target id
		*/
		virtual const char* getId() = 0;

		/**
		* @return width of trackable
		*/
		virtual float getWidth() = 0;

		/**
		* @return height trackable
		*/
		virtual float getHeight() = 0;
        
        virtual const char* getCloudName() = 0;
        
        virtual const char* getCloudMetaData() = 0;
	};
}
