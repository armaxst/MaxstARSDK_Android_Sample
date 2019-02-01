/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include "BackgroundCameraQuad.h"
#include <CameraDevice.h>

class Renderer
{
public:
	Renderer()
	{
	}

	virtual ~Renderer()
	{
	}

	virtual void onSizeChanged(int width, int height) = 0;
	virtual void onDraw() = 0;
	virtual void onTimer(int value) = 0;
	virtual void onKeyPressed(unsigned char key, int x, int y) = 0;
	virtual void onMouseClicked(int button, int state, int x, int y) = 0;

protected:
	BackgroundCameraQuad * backgroundRenderer;

	bool initRenderingDone = false;
	int viewWidth;
	int viewHeight;

	maxstAR::CameraDevice * cameraDevice;
};

