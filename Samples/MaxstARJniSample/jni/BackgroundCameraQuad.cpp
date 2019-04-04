/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#include "BackgroundCameraQuad.h"
#include <CameraDevice.h>
#include <vecmath.h>
#include <string.h>

static float VERTEX_BUF[] =
{
	-0.5f, -0.5f, 0.0f,   // top left
	-0.5f, 0.5f, 0.0f,   // bottom left
	0.5f, 0.5f, 0.0f,   // bottom right
	0.5f, -0.5f, 0.0f  // top right
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

static const char * VERTEX_SHADER_SRC =
"attribute vec4 a_position;\n"								\
"uniform mat4 u_mvpMatrix;\n"								\
"attribute vec2 a_vertexTexCoord;\n"					\
"varying vec2 v_texCoord;\n"						\
"void main()\n"													\
"{\n"															\
"	gl_Position = u_mvpMatrix  * a_position;\n"					\
"	v_texCoord = a_vertexTexCoord; 			\n"		\
"}\n";

static const char * FRAGMENT_SHADER_SRC =
"precision mediump float;\n"
"uniform sampler2D u_texture_1;\n"							\
"uniform sampler2D u_texture_2;\n"							\
"varying vec2 v_texCoord;\n"								\
"void main()\n"									\
"{\n"											\
	"    float y = texture2D(u_texture_1, v_texCoord).r;\n" \
	"    float u = texture2D(u_texture_2, v_texCoord).a;\n" \
	"    float v = texture2D(u_texture_2, v_texCoord).r;\n" \
	"    y = 1.1643 * (y - 0.0625);\n" \
	"    u = u - 0.5;\n" \
	"    v = v - 0.5;\n" \
	"    float r = y + 1.5958 * v;\n" \
	"    float g = y - 0.39173 * u - 0.81290 * v;\n" \
	"    float b = y + 2.017 * u;\n" \
	"    gl_FragColor = vec4(r, g, b, 1.0);\n" \
"}\n";

BackgroundCameraQuad::BackgroundCameraQuad()
{
	vertices = &VERTEX_BUF[0];
	textureCoords = &TEX_COORD_BUF[0];
	indices = &INDEX_BUF[0];

	vertexCount = sizeof(VERTEX_BUF) / sizeof(float);
	texCoordCount = sizeof(TEX_COORD_BUF) / sizeof(float);
	indexCount = sizeof(INDEX_BUF) / sizeof(unsigned char);

	program = ShaderUtil::createProgram(VERTEX_SHADER_SRC, FRAGMENT_SHADER_SRC);
	positionHandle = glGetAttribLocation(program, "a_position");
	mvpMatrixHandle = glGetUniformLocation(program, "u_mvpMatrix");
	textureCoordHandle = glGetAttribLocation(program, "a_vertexTexCoord");

	char buf[100];
	glGenTextures(2, textureNames);
	for (int i = 0; i < 2; i++) {
		glBindTexture(GL_TEXTURE_2D, textureNames[i]);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		memset(buf, 0x00, sizeof(buf));

		int tempId = i + 1;
		sprintf(buf, "u_texture_%d", tempId);
		textureHandles[i] = glGetUniformLocation(program, buf);
	}
}

BackgroundCameraQuad::~BackgroundCameraQuad()
{
}

void BackgroundCameraQuad::draw(maxstAR::TrackedImage *image)
{
	if (image == nullptr)
	{
		return;
	}

	glUseProgram(program);
	ShaderUtil::checkGlError("glUseProgram");

	glVertexAttribPointer(positionHandle, 3, GL_FLOAT, GL_FALSE, 0, vertices);
	glEnableVertexAttribArray(positionHandle);

	glVertexAttribPointer(textureCoordHandle, 2, GL_FLOAT, GL_FALSE, 0, textureCoords);
	glEnableVertexAttribArray(textureCoordHandle);

	glUniformMatrix4fv(mvpMatrixHandle, 1, GL_FALSE, maxstAR::CameraDevice::getInstance()->getBackgroundPlaneProjectionMatrix());

	glActiveTexture(GL_TEXTURE0);
	glUniform1i(textureHandles[0], 0);
	glBindTexture(GL_TEXTURE_2D, textureNames[0]);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, image->getWidth(), image->getHeight(), 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, image->getData());

	glActiveTexture(GL_TEXTURE1);
	glUniform1i(textureHandles[1], 1);
	glBindTexture(GL_TEXTURE_2D, textureNames[1]);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE_ALPHA, image->getWidth() / 2, image->getHeight() / 2, 0, GL_LUMINANCE_ALPHA, GL_UNSIGNED_BYTE,
		image->getData() + image->getWidth() * image->getHeight());

	glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_BYTE, indices);

	glDisableVertexAttribArray(positionHandle);
	glDisableVertexAttribArray(textureCoordHandle);
	glBindTexture(GL_TEXTURE_2D, 0);
	glUseProgram(0);
}
