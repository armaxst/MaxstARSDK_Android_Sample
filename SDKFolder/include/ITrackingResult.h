/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include <string>
#include "ITrackable.h"

using namespace std;

namespace maxstAR
{
	/**
	* @brief Contains tracked targets informations
	*/
	class ITrackingResult
	{
	public:
		ITrackingResult() { }

		virtual ~ITrackingResult() { }

		/**
		* @brief Get tracking target information
		* @param index target index
		* @return Trackable class instance
		*/
		virtual ITrackable* getTrackable(int index) = 0;

		/**
		* @brief Get tracking target count. Current version ar engine could not track multi target. 
		*  That feature will be implemented not so far future.
		* @return tracking target count
		*/
		virtual int getCount() = 0;

		///**
		//* @brief Clear tracking informations
		//*/
		//virtual void clear() = 0;
	};
}
