/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include "Types.h"
#include "TrackedImage.h"
#include "ITrackingResult.h"

namespace maxstAR
{
	/**
	* @brief Tracking state container
	*/
	class MAXSTAR_API TrackingState
	{
	public:
		TrackingState() {}
		virtual ~TrackingState() {}

		/**
		* @brief Get tracking result
		* @return Tracking result
		*/
		virtual ITrackingResult * getTrackingResult() = 0;

		/**
		 * @brief Get QRCode / Barcode recognition result
		 * @return QRCode or barcode text
		 */
		virtual const char* getCodeScanResult() = 0;

		/**
		* @brief Get image used for tracking
		* @return TrackedImage
		*/
		virtual TrackedImage * getImage() = 0;
	};
}
