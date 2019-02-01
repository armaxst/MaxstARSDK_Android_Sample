/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#include "Quad.h"
#include <ShaderUtil.h>

static float VERTEX_BUF[] =
{
	-0.5f, 0.5f, 0.0f,   // top left
	-0.5f, -0.5f, 0.0f,   // bottom left
	0.5f, -0.5f, 0.0f,   // bottom right
	0.5f, 0.5f, 0.0f  // top right
};

static unsigned char INDEX_BUF[] =
{
	0, 1, 2, 2, 3, 0
};

static float TEX_COORD_BUF[] =
{
	0.0f, 0.0f,
	0.0f, 1.0f,
	1.0f, 1.0f,
	1.0f, 0.0f,
};

static const float COLOR_BUF[] =
{
	0.6f, 1.0f, 0.6f, 1.0f,
	0.6f, 1.0f, 0.6f, 1.0f,
	0.6f, 1.0f, 0.6f, 1.0f,
	0.6f, 1.0f, 0.6f, 1.0f
};

static const char * VERTEX_SHADER_SRC =
"attribute vec4 a_position;\n"								\
"attribute vec4 a_color;\n"									\
"uniform mat4 u_mvpMatrix;\n"								\
"varying vec4 v_color;\n"					\
"void main()\n"													\
"{\n"															\
"	gl_Position = u_mvpMatrix  * a_position;\n"					\
"	v_color = a_color;\n"										\
"}\n";

static const char * FRAGMENT_SHADER_SRC =
"precision mediump float;\n"
"varying vec4 v_color;\n"					\
"void main()\n"									\
"{\n"											\
"	gl_FragColor = v_color;\n"					\
"}\n";

Quad::Quad()
{
	vertices = &VERTEX_BUF[0];
	indices = &INDEX_BUF[0];
	textureCoords = &TEX_COORD_BUF[0];

	vertexCount = sizeof(VERTEX_BUF) / sizeof(float);
	indexCount = sizeof(INDEX_BUF) / sizeof(unsigned char);
	texCoordCount = sizeof(TEX_COORD_BUF) / sizeof(float);
}

Quad::~Quad()
{
}

void Quad::draw()
{
	if (program == 0)
	{
		program = ShaderUtil::createProgram(VERTEX_SHADER_SRC, FRAGMENT_SHADER_SRC);
		positionHandle = glGetAttribLocation(program, "a_position");
		colorHandle = glGetAttribLocation(program, "a_color");
		mvpMatrixHandle = glGetUniformLocation(program, "u_mvpMatrix");
	}

	glUseProgram(program);
	ShaderUtil::checkGlError("glUseProgram");

	glVertexAttribPointer(positionHandle, 3, GL_FLOAT, GL_FALSE, 0, vertices);
	glEnableVertexAttribArray(positionHandle);

	glVertexAttribPointer(colorHandle, 4, GL_FLOAT, GL_FALSE, 0, COLOR_BUF);
	glEnableVertexAttribArray(colorHandle);

	localMVPMatrix = projectionMatrix * modelMatrix;
	glUniformMatrix4fv(mvpMatrixHandle, 1, GL_FALSE, localMVPMatrix.Ptr());

	glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_BYTE, indices);

	glDisableVertexAttribArray(positionHandle);
	glDisableVertexAttribArray(colorHandle);
	glUseProgram(0);
}
