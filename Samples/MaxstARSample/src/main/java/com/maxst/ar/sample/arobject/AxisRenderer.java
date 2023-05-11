/*
 * Copyright 2016 Maxst, Inc. All Rights Reserved.
 */
package com.maxst.ar.sample.arobject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.maxst.ar.sample.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AxisRenderer extends BaseRenderer {
    private static final String VERTEX_SHADER_SRC =
            "attribute vec4 a_position;\n" +
                    "attribute vec4 a_color;\n" +
                    "uniform mat4 u_mvpMatrix;\n" +
                    "varying vec4 v_color;\n" +
                    "void main()\n" +
                    "{\n" +
                    "	gl_Position = u_mvpMatrix  * a_position;\n" +
                    "	v_color = a_color;\n" +
                    "}\n";

    private static final String FRAGMENT_SHADER_SRC =
            "precision mediump float;\n" +
                    "varying vec4 v_color;\n" +
                    "void main()\n" +
                    "{\n" +
                    "	gl_FragColor = v_color;\n" +
                    "}\n";


    private static final float[] VERTEX_BUF = {
            // y (-z)
            -0.02f, -0.02f, -0.5f,
            0.02f, -0.02f, -0.5f,
            0.02f, 0.02f, -0.5f,
            -0.02f, 0.02f, -0.5f,
            -0.02f, -0.02f, -0.02f,
            -0.02f, 0.02f, -0.02f,
            0.02f, 0.02f, -0.02f,
            0.02f, -0.02f, -0.02f,

            // x (x)
            -0.02f, -0.02f, -0.02f,
            0.5f, -0.02f, -0.02f,
            0.5f, 0.02f, -0.02f,
            -0.02f, 0.02f, -0.02f,
            -0.02f, -0.02f, 0.02f,
            -0.02f, 0.02f, 0.02f,
            0.5f, 0.02f, 0.02f,
            0.5f, -0.02f, 0.02f,

            // z (-y)
            -0.02f, -0.5f, -0.02f,
            0.02f, -0.5f, -0.02f,
            0.02f, -0.02f, -0.02f,
            -0.02f, -0.02f, -0.02f,
            -0.02f, -0.5f, 0.02f,
            -0.02f, -0.02f, 0.02f,
            0.02f, -0.02f, 0.02f,
            0.02f, -0.5f, 0.02f,
    };

    private static final short[] INDEX_BUF = {
            0 + (8 * 0), 1 + (8 * 0), 2 + (8 * 0),    // 1
            2 + (8 * 0), 3 + (8 * 0), 0 + (8 * 0),
            4 + (8 * 0), 5 + (8 * 0), 6 + (8 * 0),    // 2
            6 + (8 * 0), 7 + (8 * 0), 4 + (8 * 0),
            0 + (8 * 0), 4 + (8 * 0), 7 + (8 * 0),    // 3
            7 + (8 * 0), 1 + (8 * 0), 0 + (8 * 0),
            1 + (8 * 0), 7 + (8 * 0), 6 + (8 * 0),    // 4
            6 + (8 * 0), 2 + (8 * 0), 1 + (8 * 0),
            2 + (8 * 0), 6 + (8 * 0), 5 + (8 * 0),    // 5
            5 + (8 * 0), 3 + (8 * 0), 2 + (8 * 0),
            3 + (8 * 0), 5 + (8 * 0), 4 + (8 * 0),    // 6
            4 + (8 * 0), 0 + (8 * 0), 3 + (8 * 0),

            0 + (8 * 1), 1 + (8 * 1), 2 + (8 * 1),
            2 + (8 * 1), 3 + (8 * 1), 0 + (8 * 1),
            4 + (8 * 1), 5 + (8 * 1), 6 + (8 * 1),
            6 + (8 * 1), 7 + (8 * 1), 4 + (8 * 1),
            0 + (8 * 1), 4 + (8 * 1), 7 + (8 * 1),
            7 + (8 * 1), 1 + (8 * 1), 0 + (8 * 1),
            1 + (8 * 1), 7 + (8 * 1), 6 + (8 * 1),
            6 + (8 * 1), 2 + (8 * 1), 1 + (8 * 1),
            2 + (8 * 1), 6 + (8 * 1), 5 + (8 * 1),
            5 + (8 * 1), 3 + (8 * 1), 2 + (8 * 1),
            3 + (8 * 1), 5 + (8 * 1), 4 + (8 * 1),
            4 + (8 * 1), 0 + (8 * 1), 3 + (8 * 1),

            0 + (8 * 2), 1 + (8 * 2), 2 + (8 * 2),
            2 + (8 * 2), 3 + (8 * 2), 0 + (8 * 2),
            4 + (8 * 2), 5 + (8 * 2), 6 + (8 * 2),
            6 + (8 * 2), 7 + (8 * 2), 4 + (8 * 2),
            0 + (8 * 2), 4 + (8 * 2), 7 + (8 * 2),
            7 + (8 * 2), 1 + (8 * 2), 0 + (8 * 2),
            1 + (8 * 2), 7 + (8 * 2), 6 + (8 * 2),
            6 + (8 * 2), 2 + (8 * 2), 1 + (8 * 2),
            2 + (8 * 2), 6 + (8 * 2), 5 + (8 * 2),
            5 + (8 * 2), 3 + (8 * 2), 2 + (8 * 2),
            3 + (8 * 2), 5 + (8 * 2), 4 + (8 * 2),
            4 + (8 * 2), 0 + (8 * 2), 3 + (8 * 2),
    };

    private static final float COLOR_BUF[] = {
            // y
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,

            // x
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            // z
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
    };

    public AxisRenderer() {
        super();
        ByteBuffer bb = ByteBuffer.allocateDirect(VERTEX_BUF.length * Float.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(VERTEX_BUF);
        vertexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(INDEX_BUF.length * Short.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        indexBuffer = bb.asShortBuffer();
        indexBuffer.put(INDEX_BUF);
        indexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(COLOR_BUF.length * Float.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        colorBuffer = bb.asFloatBuffer();
        colorBuffer.put(COLOR_BUF);
        colorBuffer.position(0);

        shaderProgramId = ShaderUtil.createProgram(VERTEX_SHADER_SRC, FRAGMENT_SHADER_SRC);

        positionHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_position");
        colorHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_color");
        mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgramId, "u_mvpMatrix");
    }

    @Override
    public void draw() {
        GLES20.glUseProgram(shaderProgramId);

        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
        GLES20.glEnableVertexAttribArray(colorHandle);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(modelMatrix, 0, translation, 0, rotation, 0);
        Matrix.multiplyMM(modelMatrix, 0, modelMatrix, 0, scale, 0);
        Matrix.multiplyMM(modelMatrix, 0, transform, 0, modelMatrix, 0);

        Matrix.multiplyMM(localMvpMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, localMvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, INDEX_BUF.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
    }
}
