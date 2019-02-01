/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include "Renderer.h"
#include "ColoredCube.h"

class ImageTrackerRenderer : public Renderer
{
public:
	ImageTrackerRenderer();
	~ImageTrackerRenderer();

	void onSizeChanged(int width, int height);
	void onDraw();
	void onTimer(int value);
	void onKeyPressed(unsigned char key, int x, int y);
	void onMouseClicked(int button, int state, int x, int y);

private:
	ColoredCube * coloredCube = nullptr;
	float worldPosition[3];

	unsigned char* descriptData;
};

