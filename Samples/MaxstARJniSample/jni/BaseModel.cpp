/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#include "BaseModel.h"

BaseModel::BaseModel()
{
	for (int i = 0; i < MAX_TEXTURE_COUNT; i++)
	{
		textureIds[i] = 0;
		textureHandles[i] = 0;
	}

	localMVPMatrix = gl_helper::Mat4::Identity();
	modelMatrix = gl_helper::Mat4::Identity();
	projectionMatrix = gl_helper::Mat4::Identity();
}

BaseModel::~BaseModel()
{
}

void BaseModel::setProjectionMatrix(gl_helper::Mat4 & projection)
{
	projectionMatrix = projection;
}

void BaseModel::setTransform(gl_helper::Mat4 & transform)
{
	modelMatrix = transform;
}

void BaseModel::setPosition(float x, float y, float z)
{
	modelMatrix.PostTranslate(x, y, z);
}

void BaseModel::setRotation(float angle, float x, float y, float z)
{
	modelMatrix.PostRotate(angle, x, y, z);
}

void BaseModel::setScale(float x, float y, float z)
{
	modelMatrix.PostScale(x, y, z);
}