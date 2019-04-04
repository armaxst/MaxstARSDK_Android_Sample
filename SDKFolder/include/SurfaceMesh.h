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
	class MAXSTAR_API SurfaceMesh
	{
	public:	
		virtual ~SurfaceMesh() {}

		/**
		* @return surface mesh vertex count
		*/
		virtual int getVertexCount() = 0;

		/**
		* @return surface mesh index count
		*/
		virtual int getIndexCount() = 0;

		/**
		* @return surface mesh vertex buffer (Always returns same address so vertex count must be considered)
		*/
		virtual float* getVertexBuffer() = 0;

		/**
		* @return surface mesh index buffer (Always returns same address so index count must be considered )
		*/
		virtual unsigned short* getIndexBuffer() = 0;
	};
}
