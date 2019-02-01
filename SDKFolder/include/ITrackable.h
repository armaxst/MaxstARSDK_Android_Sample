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
		virtual string getName() = 0;

		/**
		* @return tracking target id
		*/
		virtual string getId() = 0;

		/**
		* @return width of trackable
		*/
		virtual float getWidth() = 0;

		/**
		* @return height trackable
		*/
		virtual float getHeight() = 0;
        
        virtual string getCloudName() = 0;
        
        virtual string getCloudMetaData() = 0;
	};
}
