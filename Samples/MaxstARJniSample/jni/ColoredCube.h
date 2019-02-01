/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma  once

#include "BaseModel.h"

class ColoredCube : public BaseModel
{
public:
	ColoredCube();
	virtual ~ColoredCube();

	virtual void draw();
};