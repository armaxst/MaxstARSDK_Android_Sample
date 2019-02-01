/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#include "ImageTrackerRenderer.h"
#include <ShaderUtil.h>
#include <TrackerManager.h>
#include <MaxstAR.h>
#include <TrackedImage.h>

using namespace maxstAR;

ImageTrackerRenderer::ImageTrackerRenderer()
	: Renderer()
{
	for (int i = 0; i < 3; i++)
	{
		worldPosition[i] = 0.0f;
	}

	descriptData = (unsigned char*)malloc(1024 * 1024);
	
}

ImageTrackerRenderer::~ImageTrackerRenderer()
{
	if (backgroundRenderer != nullptr)
	{
		delete backgroundRenderer;
		backgroundRenderer = nullptr;
	}

	if (coloredCube != nullptr)
	{
		delete coloredCube;
		coloredCube = nullptr;
	}
}

void ImageTrackerRenderer::onSizeChanged(int width, int height)
{
	viewWidth = width;
	viewHeight = height;

	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	if (!initRenderingDone)
	{
		initRenderingDone = true;
		backgroundRenderer = new BackgroundCameraQuad();

		if (coloredCube == nullptr)
		{
			coloredCube = new ColoredCube();
		}

		if (width < height)
		{
			maxstAR::setScreenOrientation(ScreenOrientation::PORTRAIT);
		}
		else
		{
			maxstAR::setScreenOrientation(ScreenOrientation::LANDSCAPE);
		}
	}

	maxstAR::onSurfaceChanged(width, height);
}

void ImageTrackerRenderer::onDraw()
{
	glViewport(0, 0, viewWidth, viewHeight);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	TrackingState * trackingState = TrackerManager::getInstance()->updateTrackingState();
	ITrackingResult *result = trackingState->getTrackingResult();

	TrackedImage * image = trackingState->getImage();
	backgroundRenderer->draw(image);

	glEnable(GL_DEPTH_TEST);
	gl_helper::Mat4 projectionMatrix = CameraDevice::getInstance()->getProjectionMatrix();

	for (int i = 0; i < (int)result->getCount(); i++)
	{
		maxstAR::ITrackable *trackable = result->getTrackable(i);

		gl_helper::Mat4 pose = trackable->getPose();
		pose = projectionMatrix * pose;
		pose *= gl_helper::Mat4::Translation(worldPosition[0], worldPosition[1], -0.05f);
		pose *= gl_helper::Mat4::Scale(0.1f, 0.1f, 0.1f);

		coloredCube->setTransform(pose);
		coloredCube->draw();
	}
	glDisable(GL_DEPTH_TEST);
}

void ImageTrackerRenderer::onTimer(int value)
{

}

void ImageTrackerRenderer::onKeyPressed(unsigned char key, int x, int y)
{
}

void ImageTrackerRenderer::onMouseClicked(int button, int state, int x, int y)
{
}