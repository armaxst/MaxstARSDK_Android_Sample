/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#include "ColoredCube.h"
#include <ShaderUtil.h>

static float VERTEX_BUF[] =
{	//Vertices according to faces
	-0.5f, -0.5f, -0.5f, // 0
	0.5f, -0.5f, -0.5f, // 1
	0.5f, 0.5f, -0.5f, // 2
	-0.5f, 0.5f, -0.5f, // 3
	-0.5f, -0.5f, 0.5f, // 4
	0.5f, -0.5f, 0.5f, // 5
	0.5f, 0.5f, 0.5f, // 6
	-0.5f, 0.5f, 0.5f, // 7
};

static unsigned char INDEX_BUF[] =
{
	0, 2, 3, 2, 0, 1, // back face
	0, 7, 4, 7, 0, 3, // left face
	1, 6, 2, 6, 1, 5, // right face
	0, 5, 1, 5, 0, 4, // bottom face
	3, 6, 7, 6, 3, 2, // up face
	4, 6, 5, 6, 4, 7, // front face
};

static float TEX_COORD_BUF[] =
{
	//Mapping coordinates for the vertices
	0.0f, 0.0f,
	0.0f, 1.0f,
	1.0f, 0.0f,
	1.0f, 1.0f,

	0.0f, 0.0f,
	0.0f, 1.0f,
	1.0f, 0.0f,
	1.0f, 1.0f,

	0.0f, 0.0f,
	0.0f, 1.0f,
	1.0f, 0.0f,
	1.0f, 1.0f,

	0.0f, 0.0f,
	0.0f, 1.0f,
	1.0f, 0.0f,
	1.0f, 1.0f,

	0.0f, 0.0f,
	0.0f, 1.0f,
	1.0f, 0.0f,
	1.0f, 1.0f,

	0.0f, 0.0f,
	0.0f, 1.0f,
	1.0f, 0.0f,
	1.0f, 1.0f,
};

static const float COLOR_BUF[] =
{
	1.0f, 1.0f, 1.0f, 1.0f,
	1.0f, 1.0f, 1.0f, 1.0f,
	1.0f, 1.0f, 1.0f, 1.0f,
	1.0f, 1.0f, 1.0f, 1.0f,
	0.0f, 0.0f, 0.0f, 1.0f,
	0.0f, 0.0f, 0.0f, 1.0f,
	0.0f, 0.0f, 0.0f, 1.0f,
	0.0f, 0.0f, 0.0f, 1.0f,
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

ColoredCube::ColoredCube()
{
	vertices = &VERTEX_BUF[0];
	indices = &INDEX_BUF[0];
	textureCoords = &TEX_COORD_BUF[0];

	vertexCount = sizeof(VERTEX_BUF) / sizeof(float);
	indexCount = sizeof(INDEX_BUF) / sizeof(unsigned char);
	texCoordCount = sizeof(TEX_COORD_BUF) / sizeof(float);

	localMVPMatrix = gl_helper::Mat4::Identity();
	modelMatrix = gl_helper::Mat4::Identity();
}

ColoredCube::~ColoredCube()
{
}

void ColoredCube::draw()
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