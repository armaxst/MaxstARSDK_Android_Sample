/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#include <ShaderUtil.h>
#include <vecmath.h>

#ifdef __ANDROID__
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#endif

#ifdef __IOS__
#include <OpenGLES/ES2/gl.h>
#include <OpenGLES/ES2/glext.h>
#endif

#ifdef __MacOS__
#include <GL/glew.h>
#endif

#ifdef WIN32
#include <GL/glew.h>
#include <GL/glut.h>
#endif

#include <memory>
#include <string>

#define MAX_TEXTURE_COUNT 8

using namespace std;

#define GET_SHADER_SRC(SHADER, VERSION) SHADER##VERSION

class BaseModel
{
public:
	BaseModel();
	virtual ~BaseModel();

	virtual void draw() = 0;
	virtual void setProjectionMatrix(gl_helper::Mat4 & projection);
	virtual void setTransform(gl_helper::Mat4 & transform);
	virtual void setPosition(float x, float y, float z);
	virtual void setRotation(float angle, float x, float y, float z);
	virtual void setScale(float x, float y, float z);

protected:
	void * vertices = nullptr;
	void * indices = nullptr;
	void * textureCoords = nullptr;
	void * colors = nullptr;

	int vertexCount = 0;
	int indexCount = 0;
	int texCoordCount = 0;

	GLuint program = 0;
	GLuint positionHandle = 0;
	GLuint textureCoordHandle = 0;
	GLuint colorHandle = 0;
	GLuint mvpMatrixHandle = 0;
	int textureCount = 0;
	GLuint textureIds[MAX_TEXTURE_COUNT];
	GLuint textureHandles[MAX_TEXTURE_COUNT];

	gl_helper::Mat4 modelMatrix;
	gl_helper::Mat4 localMVPMatrix;
	gl_helper::Mat4 projectionMatrix;
};
