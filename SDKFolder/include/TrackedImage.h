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
	* @brief image data which is used for tracker and rendering
	*/
	class MAXSTAR_API TrackedImage
	{
	public:
		/**
		* @brief Get image width
		*/
		virtual const int getWidth() = 0;

		/**
		* @brief Get image height
		*/
		virtual const int getHeight() = 0;

		/**
		* @brief Get image length
		*/
		virtual const int getLength() = 0;

		/**
		* @brief Get image timestamp
		*/
		virtual const unsigned long long int getTimestamp() = 0;

		/**
		* @brief Get image format
		*/
		virtual const ColorFormat getFormat() = 0;

		/**
		* @brief Get image data
		*/
		virtual const unsigned char* getData() = 0;

		/**
		* @brief Get image index
		*/
		virtual const int getIndex() = 0;
	};
}
