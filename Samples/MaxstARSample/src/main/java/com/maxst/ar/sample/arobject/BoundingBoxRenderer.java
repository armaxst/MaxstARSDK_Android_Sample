/*
 * Copyright 2016 Maxst, Inc. All Rights Reserved.
 */
package com.maxst.ar.sample.arobject;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.maxst.ar.sample.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

import static android.content.ContentValues.TAG;

public class BoundingBoxRenderer extends BaseRenderer {
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
             // bf
            -0.5f, 0.495f, 0.495f,
            -0.5f, 0.495f, 0.495f,
            0.5f, 0.505f, 0.495f,
            -0.5f, 0.505f, 0.495f,
            -0.5f, 0.495f, 0.505f,
            -0.5f, 0.505f, 0.505f,
            0.5f, 0.505f, 0.505f,
            0.5f, 0.495f, 0.505f,

            // bb
            -0.5f, -0.505f, 0.495f,
            -0.5f, -0.505f, 0.495f,
            0.5f, -0.495f, 0.495f,
            -0.5f, -0.495f, 0.495f,
            -0.5f, -0.505f, 0.505f,
            -0.5f, -0.495f, 0.505f,
            0.5f, -0.495f, 0.505f,
            0.5f, -0.505f, 0.505f,

            // bl
            -0.505f, -0.5f, 0.495f,
            -0.495f, -0.5f, 0.495f,
            -0.505f, 0.5f, 0.495f,
            -0.495f, 0.5f, 0.495f,
            -0.495f, -0.5f, 0.505f,
            -0.495f, 0.5f, 0.505f,
            -0.505f, 0.5f, 0.505f,
            -0.505f, -0.5f, 0.505f,

            // br
            0.495f, -0.5f, 0.495f,
            0.505f, -0.5f, 0.495f,
            0.495f, 0.5f, 0.495f,
            0.505f, 0.5f, 0.495f,
            0.505f, -0.5f, 0.505f,
            0.505f, 0.5f, 0.505f,
            0.495f, 0.5f, 0.505f,
            0.495f, -0.5f, 0.505f,

            // tf
            -0.5f, 0.495f, -0.505f,
            -0.5f, 0.495f, -0.505f,
            0.5f, 0.505f, -0.505f,
            -0.5f, 0.505f, -0.505f,
            -0.5f, 0.495f, -0.495f,
            -0.5f, 0.505f, -0.495f,
            0.5f, 0.505f, -0.495f,
            0.5f, 0.495f, -0.495f,

            // tb
            -0.5f, -0.505f, -0.505f,
            -0.5f, -0.505f, -0.505f,
            0.5f, -0.495f, -0.505f,
            -0.5f, -0.495f, -0.505f,
            -0.5f, -0.505f, -0.495f,
            -0.5f, -0.495f, -0.495f,
            0.5f, -0.495f, -0.495f,
            0.5f, -0.505f, -0.495f,

            // tl
            -0.505f, -0.5f, -0.505f,
            -0.495f, -0.5f, -0.505f,
            -0.505f, 0.5f, -0.505f,
            -0.495f, 0.5f, -0.505f,
            -0.495f, -0.5f, -0.495f,
            -0.495f, 0.5f, -0.495f,
            -0.505f, 0.5f, -0.495f,
            -0.505f, -0.5f, -0.495f,

            // tr
            0.495f, -0.5f, -0.505f,
            0.505f, -0.5f, -0.505f,
            0.495f, 0.5f, -0.505f,
            0.505f, 0.5f, -0.505f,
            0.505f, -0.5f, -0.495f,
            0.505f, 0.5f, -0.495f,
            0.495f, 0.5f, -0.495f,
            0.495f, -0.5f, -0.495f,

            // stl
            -0.505f, 0.495f, -0.5f,
            -0.495f, 0.495f, -0.5f,
            -0.495f, 0.505f, -0.5f,
            -0.505f, 0.505f, -0.5f,
            -0.505f, 0.495f, 0.5f,
            -0.505f, 0.505f, 0.5f,
            -0.495f, 0.505f, 0.5f,
            -0.495f, 0.495f, 0.5f,

            // str
            0.495f, 0.495f, -0.5f,
            0.505f, 0.495f, -0.5f,
            0.505f, 0.505f, -0.5f,
            0.495f, 0.505f, -0.5f,
            0.495f, 0.495f, 0.5f,
            0.495f, 0.505f, 0.5f,
            0.505f, 0.505f, 0.5f,
            0.505f, 0.495f, 0.5f,

            // sbl
            -0.505f, -0.505f, -0.5f,
            -0.495f, -0.505f, -0.5f,
            -0.495f, -0.495f, -0.5f,
            -0.505f, -0.495f, -0.5f,
            -0.505f, -0.505f, 0.5f,
            -0.505f, -0.495f, 0.5f,
            -0.495f, -0.495f, 0.5f,
            -0.495f, -0.505f, 0.5f,

            // sbr
            0.495f, -0.505f, -0.5f,
            0.505f, -0.505f, -0.5f,
            0.505f, -0.495f, -0.5f,
            0.495f, -0.495f, -0.5f,
            0.495f, -0.505f, 0.5f,
            0.495f, -0.495f, 0.5f,
            0.505f, -0.495f, 0.5f,
            0.505f, -0.505f, 0.5f,
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

            0 + (8 * 3), 1 + (8 * 3), 2 + (8 * 3),
            2 + (8 * 3), 3 + (8 * 3), 0 + (8 * 3),
            4 + (8 * 3), 5 + (8 * 3), 6 + (8 * 3),
            6 + (8 * 3), 7 + (8 * 3), 4 + (8 * 3),
            0 + (8 * 3), 4 + (8 * 3), 7 + (8 * 3),
            7 + (8 * 3), 1 + (8 * 3), 0 + (8 * 3),
            1 + (8 * 3), 7 + (8 * 3), 6 + (8 * 3),
            6 + (8 * 3), 2 + (8 * 3), 1 + (8 * 3),
            2 + (8 * 3), 6 + (8 * 3), 5 + (8 * 3),
            5 + (8 * 3), 3 + (8 * 3), 2 + (8 * 3),
            3 + (8 * 3), 5 + (8 * 3), 4 + (8 * 3),
            4 + (8 * 3), 0 + (8 * 3), 3 + (8 * 3),

            0 + (8 * 4), 1 + (8 * 4), 2 + (8 * 4),    // 1
            2 + (8 * 4), 3 + (8 * 4), 0 + (8 * 4),
            4 + (8 * 4), 5 + (8 * 4), 6 + (8 * 4),    // 2
            6 + (8 * 4), 7 + (8 * 4), 4 + (8 * 4),
            0 + (8 * 4), 4 + (8 * 4), 7 + (8 * 4),    // 3
            7 + (8 * 4), 1 + (8 * 4), 0 + (8 * 4),
            1 + (8 * 4), 7 + (8 * 4), 6 + (8 * 4),    // 4
            6 + (8 * 4), 2 + (8 * 4), 1 + (8 * 4),
            2 + (8 * 4), 6 + (8 * 4), 5 + (8 * 4),    // 5
            5 + (8 * 4), 3 + (8 * 4), 2 + (8 * 4),
            3 + (8 * 4), 5 + (8 * 4), 4 + (8 * 4),    // 6
            4 + (8 * 4), 0 + (8 * 4), 3 + (8 * 4),

            0 + (8 * 5), 1 + (8 * 5), 2 + (8 * 5),
            2 + (8 * 5), 3 + (8 * 5), 0 + (8 * 5),
            4 + (8 * 5), 5 + (8 * 5), 6 + (8 * 5),
            6 + (8 * 5), 7 + (8 * 5), 4 + (8 * 5),
            0 + (8 * 5), 4 + (8 * 5), 7 + (8 * 5),
            7 + (8 * 5), 1 + (8 * 5), 0 + (8 * 5),
            1 + (8 * 5), 7 + (8 * 5), 6 + (8 * 5),
            6 + (8 * 5), 2 + (8 * 5), 1 + (8 * 5),
            2 + (8 * 5), 6 + (8 * 5), 5 + (8 * 5),
            5 + (8 * 5), 3 + (8 * 5), 2 + (8 * 5),
            3 + (8 * 5), 5 + (8 * 5), 4 + (8 * 5),
            4 + (8 * 5), 0 + (8 * 5), 3 + (8 * 5),

            0 + (8 * 6), 1 + (8 * 6), 2 + (8 * 6),
            2 + (8 * 6), 3 + (8 * 6), 0 + (8 * 6),
            4 + (8 * 6), 5 + (8 * 6), 6 + (8 * 6),
            6 + (8 * 6), 7 + (8 * 6), 4 + (8 * 6),
            0 + (8 * 6), 4 + (8 * 6), 7 + (8 * 6),
            7 + (8 * 6), 1 + (8 * 6), 0 + (8 * 6),
            1 + (8 * 6), 7 + (8 * 6), 6 + (8 * 6),
            6 + (8 * 6), 2 + (8 * 6), 1 + (8 * 6),
            2 + (8 * 6), 6 + (8 * 6), 5 + (8 * 6),
            5 + (8 * 6), 3 + (8 * 6), 2 + (8 * 6),
            3 + (8 * 6), 5 + (8 * 6), 4 + (8 * 6),
            4 + (8 * 6), 0 + (8 * 6), 3 + (8 * 6),

            0 + (8 * 7), 1 + (8 * 7), 2 + (8 * 7),
            2 + (8 * 7), 3 + (8 * 7), 0 + (8 * 7),
            4 + (8 * 7), 5 + (8 * 7), 6 + (8 * 7),
            6 + (8 * 7), 7 + (8 * 7), 4 + (8 * 7),
            0 + (8 * 7), 4 + (8 * 7), 7 + (8 * 7),
            7 + (8 * 7), 1 + (8 * 7), 0 + (8 * 7),
            1 + (8 * 7), 7 + (8 * 7), 6 + (8 * 7),
            6 + (8 * 7), 2 + (8 * 7), 1 + (8 * 7),
            2 + (8 * 7), 6 + (8 * 7), 5 + (8 * 7),
            5 + (8 * 7), 3 + (8 * 7), 2 + (8 * 7),
            3 + (8 * 7), 5 + (8 * 7), 4 + (8 * 7),
            4 + (8 * 7), 0 + (8 * 7), 3 + (8 * 7),

            0 + (8 * 8), 1 + (8 * 8), 2 + (8 * 8),    // 1
            2 + (8 * 8), 3 + (8 * 8), 0 + (8 * 8),
            4 + (8 * 8), 5 + (8 * 8), 6 + (8 * 8),    // 2
            6 + (8 * 8), 7 + (8 * 8), 4 + (8 * 8),
            0 + (8 * 8), 4 + (8 * 8), 7 + (8 * 8),    // 3
            7 + (8 * 8), 1 + (8 * 8), 0 + (8 * 8),
            1 + (8 * 8), 7 + (8 * 8), 6 + (8 * 8),    // 4
            6 + (8 * 8), 2 + (8 * 8), 1 + (8 * 8),
            2 + (8 * 8), 6 + (8 * 8), 5 + (8 * 8),    // 5
            5 + (8 * 8), 3 + (8 * 8), 2 + (8 * 8),
            3 + (8 * 8), 5 + (8 * 8), 4 + (8 * 8),    // 6
            4 + (8 * 8), 0 + (8 * 8), 3 + (8 * 8),

            0 + (8 * 9), 1 + (8 * 9), 2 + (8 * 9),
            2 + (8 * 9), 3 + (8 * 9), 0 + (8 * 9),
            4 + (8 * 9), 5 + (8 * 9), 6 + (8 * 9),
            6 + (8 * 9), 7 + (8 * 9), 4 + (8 * 9),
            0 + (8 * 9), 4 + (8 * 9), 7 + (8 * 9),
            7 + (8 * 9), 1 + (8 * 9), 0 + (8 * 9),
            1 + (8 * 9), 7 + (8 * 9), 6 + (8 * 9),
            6 + (8 * 9), 2 + (8 * 9), 1 + (8 * 9),
            2 + (8 * 9), 6 + (8 * 9), 5 + (8 * 9),
            5 + (8 * 9), 3 + (8 * 9), 2 + (8 * 9),
            3 + (8 * 9), 5 + (8 * 9), 4 + (8 * 9),
            4 + (8 * 9), 0 + (8 * 9), 3 + (8 * 9),

            0 + (8 * 10), 1 + (8 * 10), 2 + (8 * 10),
            2 + (8 * 10), 3 + (8 * 10), 0 + (8 * 10),
            4 + (8 * 10), 5 + (8 * 10), 6 + (8 * 10),
            6 + (8 * 10), 7 + (8 * 10), 4 + (8 * 10),
            0 + (8 * 10), 4 + (8 * 10), 7 + (8 * 10),
            7 + (8 * 10), 1 + (8 * 10), 0 + (8 * 10),
            1 + (8 * 10), 7 + (8 * 10), 6 + (8 * 10),
            6 + (8 * 10), 2 + (8 * 10), 1 + (8 * 10),
            2 + (8 * 10), 6 + (8 * 10), 5 + (8 * 10),
            5 + (8 * 10), 3 + (8 * 10), 2 + (8 * 10),
            3 + (8 * 10), 5 + (8 * 10), 4 + (8 * 10),
            4 + (8 * 10), 0 + (8 * 10), 3 + (8 * 10),

            0 + (8 * 11), 1 + (8 * 11), 2 + (8 * 11),
            2 + (8 * 11), 3 + (8 * 11), 0 + (8 * 11),
            4 + (8 * 11), 5 + (8 * 11), 6 + (8 * 11),
            6 + (8 * 11), 7 + (8 * 11), 4 + (8 * 11),
            0 + (8 * 11), 4 + (8 * 11), 7 + (8 * 11),
            7 + (8 * 11), 1 + (8 * 11), 0 + (8 * 11),
            1 + (8 * 11), 7 + (8 * 11), 6 + (8 * 11),
            6 + (8 * 11), 2 + (8 * 11), 1 + (8 * 11),
            2 + (8 * 11), 6 + (8 * 11), 5 + (8 * 11),
            5 + (8 * 11), 3 + (8 * 11), 2 + (8 * 11),
            3 + (8 * 11), 5 + (8 * 11), 4 + (8 * 11),
            4 + (8 * 11), 0 + (8 * 11), 3 + (8 * 11),
    };

    private static final float COLOR_BUF[] = {
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
    };

    protected static final int maxFaceCount = 125;
    protected int sliceX, sliceY, sliceZ;
    protected Vector<Integer> faces;
    private static float[] FACE_VERTEX_BUF  = new float[maxFaceCount*3*4];
    private static short[] FACE_INDEX_BUF = new short[maxFaceCount*6];
    private static float[] FACE_COLOR_BUF = new float[maxFaceCount*3*4];

    protected FloatBuffer faceVertexBuffer;
    protected ShortBuffer faceIndexBuffer;
    protected FloatBuffer faceColorBuffer;

    public BoundingBoxRenderer() {
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


        faces = new Vector<Integer>();

        bb = ByteBuffer.allocateDirect(maxFaceCount*3*4*Float.SIZE/8);
        bb.order(ByteOrder.nativeOrder());
        faceVertexBuffer = bb.asFloatBuffer();
        //vertexBuffer.put(VERTEX_BUF);
        faceVertexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(maxFaceCount*6*Short.SIZE/8);
        bb.order(ByteOrder.nativeOrder());
        faceIndexBuffer = bb.asShortBuffer();
        //indexBuffer.put(INDEX_BUF);
        faceIndexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(maxFaceCount*3*4*Float.SIZE/8);
        bb.order(ByteOrder.nativeOrder());
        faceColorBuffer = bb.asFloatBuffer();
        //colorBuffer.put(COLOR_BUF);
        faceColorBuffer.position(0);

    }

    @Override
    public void draw() {
        // draw frame
        GLES20.glUseProgram(shaderProgramId);

        GLES20.glDisable(GLES20.GL_BLEND);
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

        // draw face
        String msg = "";
        int faceCount = faces.size();
        msg += "count " + faceCount + " : ";
        int vindex = 0;
        int iindex = 0;
        int cindex = 0;
        for(int i=0; i<faceCount; i++)
        {
            int face = faces.elementAt(i);
            msg += face + ",";
            FACE_INDEX_BUF[iindex++] = (short)(i*4+0);   FACE_INDEX_BUF[iindex++] = (short)(i*4+1);   FACE_INDEX_BUF[iindex++] = (short)(i*4+2);
            FACE_INDEX_BUF[iindex++] = (short)(i*4+0);   FACE_INDEX_BUF[iindex++] = (short)(i*4+2);   FACE_INDEX_BUF[iindex++] = (short)(i*4+3);

            if(face < 100)  // top
            {
                float cellWidth = 1.0f / sliceX;
                float cellHeight = 1.0f / sliceY;
                int row = (face-0) / 10;
                int col = face % 10;
                float left = -0.5f + col*cellWidth;
                float up = -0.5f + row*cellHeight;
                FACE_VERTEX_BUF[vindex++] = left;           FACE_VERTEX_BUF[vindex++] = up;             FACE_VERTEX_BUF[vindex++] = -0.5f;
                FACE_VERTEX_BUF[vindex++] = left;           FACE_VERTEX_BUF[vindex++] = up+cellHeight;  FACE_VERTEX_BUF[vindex++] = -0.5f;
                FACE_VERTEX_BUF[vindex++] = left+cellWidth; FACE_VERTEX_BUF[vindex++] = up+cellHeight;  FACE_VERTEX_BUF[vindex++] = -0.5f;
                FACE_VERTEX_BUF[vindex++] = left+cellWidth; FACE_VERTEX_BUF[vindex++] = up;             FACE_VERTEX_BUF[vindex++] = -0.5f;
            }
            else if(face < 200) // front
            {
                float cellWidth = 1.0f / sliceX;
                float cellHeight = 1.0f / sliceZ;
                int row = (face-100) / 10;
                int col = face % 10;
                float left = -0.5f + col*cellWidth;
                float up = -0.5f + row*cellHeight;
                //Log.i(TAG, "" + row + "," + col + "," + left + "," + up);
                FACE_VERTEX_BUF[vindex++] = left;           FACE_VERTEX_BUF[vindex++] = 0.5f;           FACE_VERTEX_BUF[vindex++] = up;
                FACE_VERTEX_BUF[vindex++] = left;           FACE_VERTEX_BUF[vindex++] = 0.5f;           FACE_VERTEX_BUF[vindex++] = up+cellHeight;
                FACE_VERTEX_BUF[vindex++] = left+cellWidth; FACE_VERTEX_BUF[vindex++] = 0.5f;           FACE_VERTEX_BUF[vindex++] = up+cellHeight;
                FACE_VERTEX_BUF[vindex++] = left+cellWidth; FACE_VERTEX_BUF[vindex++] = 0.5f;           FACE_VERTEX_BUF[vindex++] = up;
            }
            else if(face < 300) // left
            {
                float cellWidth = 1.0f / sliceY;
                float cellHeight = 1.0f / sliceZ;
                int row = (face-200) / 10;
                int col = face % 10;
                float left = -0.5f + col*cellWidth;
                float up = -0.5f + row*cellHeight;
                FACE_VERTEX_BUF[vindex++] = -0.5f;          FACE_VERTEX_BUF[vindex++] = left;           FACE_VERTEX_BUF[vindex++] = up;
                FACE_VERTEX_BUF[vindex++] = -0.5f;          FACE_VERTEX_BUF[vindex++] = left;           FACE_VERTEX_BUF[vindex++] = up+cellHeight;
                FACE_VERTEX_BUF[vindex++] = -0.5f;          FACE_VERTEX_BUF[vindex++] = left+cellWidth; FACE_VERTEX_BUF[vindex++] = up+cellHeight;
                FACE_VERTEX_BUF[vindex++] = -0.5f;          FACE_VERTEX_BUF[vindex++] = left+cellWidth; FACE_VERTEX_BUF[vindex++] = up;
            }
            else if(face < 400) // back
            {
                float cellWidth = 1.0f / sliceX;
                float cellHeight = 1.0f / sliceZ;
                int row = (face-300) / 10;
                int col = face % 10;
                float left = -0.5f + col*cellWidth;
                float up = -0.5f + row*cellHeight;
                FACE_VERTEX_BUF[vindex++] = left;           FACE_VERTEX_BUF[vindex++] = -0.5f;          FACE_VERTEX_BUF[vindex++] = up;
                FACE_VERTEX_BUF[vindex++] = left;           FACE_VERTEX_BUF[vindex++] = -0.5f;          FACE_VERTEX_BUF[vindex++] = up+cellHeight;
                FACE_VERTEX_BUF[vindex++] = left+cellWidth; FACE_VERTEX_BUF[vindex++] = -0.5f;          FACE_VERTEX_BUF[vindex++] = up+cellHeight;
                FACE_VERTEX_BUF[vindex++] = left+cellWidth; FACE_VERTEX_BUF[vindex++] = -0.5f;          FACE_VERTEX_BUF[vindex++] = up;
            }
            else {
                float cellWidth = 1.0f / sliceY;
                float cellHeight = 1.0f / sliceZ;
                int row = (face-400) / 10;
                int col = face % 10;
                float left = -0.5f + col*cellWidth;
                float up = -0.5f + row*cellHeight;
                FACE_VERTEX_BUF[vindex++] = 0.5f;          FACE_VERTEX_BUF[vindex++] = left;           FACE_VERTEX_BUF[vindex++] = up;
                FACE_VERTEX_BUF[vindex++] = 0.5f;          FACE_VERTEX_BUF[vindex++] = left;           FACE_VERTEX_BUF[vindex++] = up+cellHeight;
                FACE_VERTEX_BUF[vindex++] = 0.5f;          FACE_VERTEX_BUF[vindex++] = left+cellWidth; FACE_VERTEX_BUF[vindex++] = up+cellHeight;
                FACE_VERTEX_BUF[vindex++] = 0.5f;          FACE_VERTEX_BUF[vindex++] = left+cellWidth; FACE_VERTEX_BUF[vindex++] = up;
            }
            FACE_COLOR_BUF[cindex++] = 1.0f;    FACE_COLOR_BUF[cindex++] = 1.0f;    FACE_COLOR_BUF[cindex++] = 0.0f;    FACE_COLOR_BUF[cindex++] = 0.5f;
            FACE_COLOR_BUF[cindex++] = 1.0f;    FACE_COLOR_BUF[cindex++] = 1.0f;    FACE_COLOR_BUF[cindex++] = 0.0f;    FACE_COLOR_BUF[cindex++] = 0.5f;
            FACE_COLOR_BUF[cindex++] = 1.0f;    FACE_COLOR_BUF[cindex++] = 1.0f;    FACE_COLOR_BUF[cindex++] = 0.0f;    FACE_COLOR_BUF[cindex++] = 0.5f;
            FACE_COLOR_BUF[cindex++] = 1.0f;    FACE_COLOR_BUF[cindex++] = 1.0f;    FACE_COLOR_BUF[cindex++] = 0.0f;    FACE_COLOR_BUF[cindex++] = 0.5f;
        }

        if(faceCount > 0)
        {
            //Log.i(TAG, msg);
            faceVertexBuffer.put(FACE_VERTEX_BUF);
            faceVertexBuffer.position(0);
            faceIndexBuffer.put(FACE_INDEX_BUF);
            faceIndexBuffer.position(0);
            faceColorBuffer.put(FACE_COLOR_BUF);
            faceColorBuffer.position(0);

            GLES20.glUseProgram(shaderProgramId);

            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, faceVertexBuffer);
            GLES20.glEnableVertexAttribArray(positionHandle);

            GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, faceColorBuffer);
            GLES20.glEnableVertexAttribArray(colorHandle);

            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.multiplyMM(modelMatrix, 0, translation, 0, rotation, 0);
            Matrix.multiplyMM(modelMatrix, 0, modelMatrix, 0, scale, 0);
            Matrix.multiplyMM(modelMatrix, 0, transform, 0, modelMatrix, 0);

            Matrix.multiplyMM(localMvpMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
            GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, localMvpMatrix, 0);

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, faceCount*6, GLES20.GL_UNSIGNED_SHORT, faceIndexBuffer);

            GLES20.glDisableVertexAttribArray(positionHandle);
            GLES20.glDisableVertexAttribArray(colorHandle);
        }
    }

    public void setSlice(int x, int y, int z) {
        sliceX = x;
        sliceY = y;
        sliceZ = z;
    }

    public void clearFace() {
        faces.clear();
    }

    public void addFace(int face) {
        faces.add(face);
    }
}
