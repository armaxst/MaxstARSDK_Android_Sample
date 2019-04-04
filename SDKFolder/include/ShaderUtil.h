/*==============================================================================
Copyright 2017 Maxst, Inc. All Rights Reserved.
==============================================================================*/


#pragma once

#ifdef __ANDROID__
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#endif

#ifdef __IOS__
#include <OpenGLES/ES2/gl.h>
#include <OpenGLES/ES2/glext.h>
#endif

#ifdef WIN32
#include <GL/glew.h>
#include <GL/glut.h>
#endif

#ifdef __MacOS__
#include <OpenGL/gl3.h>
#endif

#include <stdlib.h>
#include <string>
#include <stdlib.h>

/**
* @brief Shader compile utility
*/
class ShaderUtil
{
public:
	static unsigned int createProgram(const char* vertexString, const char* fragmentString)
	{
		GLuint vertexShader = loadShader(GL_VERTEX_SHADER, vertexString);
		if (!vertexShader)
		{
			return 0;
		}

		GLuint pixelShader = loadShader(GL_FRAGMENT_SHADER, fragmentString);
		if (!pixelShader)
		{
			return 0;
		}

		GLuint program = glCreateProgram();
		if (program)
		{
			glAttachShader(program, vertexShader);
			checkGlError("glAttachShader");
			glAttachShader(program, pixelShader);
			checkGlError("glAttachShader");
			glLinkProgram(program);
			GLint linkStatus = GL_FALSE;
			glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
			if (linkStatus != GL_TRUE)
			{
				GLint bufLength = 0;
				glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
				if (bufLength)
				{
					char *buf = (char *)malloc(bufLength);
					if (buf)
					{
						glGetProgramInfoLog(program, bufLength, NULL, buf);
						printf("Could not link program:\n%s\n", buf);
						free(buf);
					}
				}
				glDeleteProgram(program);
				program = 0;
			}
		}
		return program;
	}

	static unsigned int loadShader(unsigned int shaderType, const char* pSource)
	{
		GLuint shader = glCreateShader((GLenum)shaderType);
		if (shader)
		{
			glShaderSource(shader, 1, &pSource, NULL);
			glCompileShader(shader);
			GLint compiled = 0;
			glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
			if (!compiled)
			{
				GLint infoLen = 0;
				glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
				if (infoLen)
				{
					char *buf = (char *)malloc(infoLen);
					if (buf)
					{
						glGetShaderInfoLog(shader, infoLen, NULL, buf);
						printf("Could not compile shader %d:\n%s\n", shaderType, buf);
						free(buf);
					}
					glDeleteShader(shader);
					shader = 0;
				}
			}
		}
		return shader;
	}

	static void checkGlError(const char* op)
	{
		for (GLint error = glGetError(); error; error = glGetError())
		{
			printf("after %s() glError (0x%x)\n", op, error);
		}
	}
};
