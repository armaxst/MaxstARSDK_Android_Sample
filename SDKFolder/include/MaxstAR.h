/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include <string>
#include "Types.h"

using namespace std;

namespace maxstAR
{
	/**
	* @brief Get sdk version
	*/
	MAXSTAR_API const char* getVersion();

	/**
	* @brief Initialize AR engine
	* @param appKey app key for this app generated from "developer.maxst.com" (Mobile only)
	*/
	MAXSTAR_API void init(string appKey);

	/**
	* @brief Deinitialize AR Engine
	*/
	MAXSTAR_API void deinit();

	/**
	* @brief Check AR engine has been initialized.
	*/
	MAXSTAR_API bool isInitialized();

	/**
	* @brief  Called when rendering surface's size changed (i.e. orientation change, resizing rendering surface)
	* @param viewWidth width size (pixel unit)
	* @param viewHeight height size (pixel unit)
	*/
	MAXSTAR_API void onSurfaceChanged(int viewWidth, int viewHeight);

	/**
	* @brief Set device orientation
	* @param orientation Screen orientation.
	* 2 is Configuration.ORIENTATION_LANDSCAPE, 1 is Configuration.ORIENTATION_PORTRAIT
	*/
	MAXSTAR_API void setScreenOrientation(ScreenOrientation orientation);
}
