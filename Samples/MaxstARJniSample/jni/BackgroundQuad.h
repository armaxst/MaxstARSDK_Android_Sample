#pragma once

#include <BackgroundTexture.h>
#include <ShaderUtil.h>

class BackgroundQuad
{
public:
	BackgroundQuad();
	~BackgroundQuad();
	void draw(maxstAR::BackgroundTexture * texture, const float * projectionMatrix);

private:
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
	GLuint textureHandle;
};