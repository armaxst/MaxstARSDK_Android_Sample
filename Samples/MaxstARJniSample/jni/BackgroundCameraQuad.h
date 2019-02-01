#pragma once

#include <ShaderUtil.h>
#include <TrackedImage.h>

class BackgroundCameraQuad
{
public:
	BackgroundCameraQuad();
	~BackgroundCameraQuad();

	void draw(maxstAR::TrackedImage * image);

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
	GLuint textureHandles[2];
	GLuint textureNames[2];
};