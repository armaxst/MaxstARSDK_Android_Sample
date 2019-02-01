/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#if (defined WIN32) && defined MAXSTAR_EXPORTS
#  define MAXSTAR_API __declspec(dllexport)
#else
#  define MAXSTAR_API
#endif

typedef unsigned char Byte;

namespace maxstAR
{
/**
* @brief Image Color format.
* 	RGB888 :
* 	YUV420sp :
*	YUV420 :
*	YUV420_888 :
*	GRAY8 :
*   RGBA8888 :
*/
    enum ColorFormat
    {
        RGB888 = 1,
        YUV420sp = 2,
        YUV420 = 3,
        YUV420_888 = 4,
        GRAY8 = 5,
        RGBA8888 = 6
    };

/**
* @brief Screen orientation. (unity screen orientation order)
* 	UNKNOWN :
* 	PORTRAIT :
*	PORTRAIT_UP :
*	PORTRAIT_DOWN :
*	LANDSCAPE :
*	LANDSCAPE_LEFT :
*	LANDSCAPE_RIGHT :
*/
    enum ScreenOrientation //
    {
        UNKNOWN = 0,
        PORTRAIT = 1,
        PORTRAIT_UP = 1,
        PORTRAIT_DOWN = 2,
        LANDSCAPE = 3,
        LANDSCAPE_LEFT = 3,
        LANDSCAPE_RIGHT = 4,
    };

    enum ResultCode
    {
        Success = 0,

		// Camera session
        CameraPermissionIsNotResolved = 100,
        CameraDevicedRestriced = 101,
        CameraPermissionIsNotGranted = 102,
        CameraAlreadyOpened = 103,
		CameraAccessException = 104,
		CameraNotExist = 105,
		CameraOpenTimeOut = 106,
		FlashLightUnsupported = 107,

		// Tracker session
        TrackerAlreadyStarted = 200,

        UnknownError = 1000,
    };
}
