/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include "Types.h"

namespace maxstAR
{
	/**
	* @brief Contains surface thumbnail image information of first keyframe
	*/
	class MAXSTAR_API SurfaceThumbnail
	{
	public:
		virtual ~SurfaceThumbnail() {}

		/**
		* @return image width
		*/
		virtual int getWidth() = 0;

		/**
		* @return image height
		*/
		virtual int getHeight() = 0;

		/**
		* @return image bytes per pixel
		*/
		virtual int getBpp() = 0;

		/**
		* @return image color format
		*/
		virtual ColorFormat getFormat() = 0;

		/**
		* @return image data length
		*/
		virtual int getLength() = 0;

		/**
		* @return thumbnail image data pointer
		*/
		virtual unsigned char* getData() = 0;
	};
}

