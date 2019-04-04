/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include "Types.h"

namespace maxstAR
{
	/**
	* @brief Control device sensor
	*/
	class MAXSTAR_API SensorDevice
	{
	public:
		static SensorDevice* getInstance();

		SensorDevice() {}
		virtual ~SensorDevice() {}

		/**
		* @brief Start device sensor
		*/
		virtual void start() = 0;

		/**
		* @brief Stop device sensor
		*/
		virtual void stop() = 0;

		/**
		* @brief Set new sensor data
		* @param data rotation data float[9]
		*/
		virtual void setNewSensorData(float* data) = 0;
	};
}
