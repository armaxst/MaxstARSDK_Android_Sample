/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.arobject;

import com.maxst.ar.ColorFormat;
import com.maxst.ar.Matrix;
import com.maxst.ar.TrackedImage;

public class BackgroundRenderHelper {

	private BackgroundRenderer backgroundRenderer;

	public void drawBackground(TrackedImage trackedImage, float[] projectionMatrix, float[] backgroundPlaneInfo) {
		drawBackground(trackedImage, projectionMatrix, backgroundPlaneInfo, false, false);
	}

	public void drawBackground(TrackedImage trackedImage, float[] projectionMatrix, float[] backgroundPlaneInfo,
							   boolean flipHorizontal, boolean flipVertical) {
		if (backgroundRenderer == null) {
			if (trackedImage.getFormat() == ColorFormat.YUV420sp) {
				backgroundRenderer = new Yuv420spRenderer();
			} else if (trackedImage.getFormat() == ColorFormat.YUV420_888) {
				backgroundRenderer = new Yuv420_888Renderer();
			} else {
				return;
			}
		}

		backgroundRenderer.setProjectionMatrix(projectionMatrix);
		backgroundRenderer.setTransform(calculateBackgroundPlaneMatrix(backgroundPlaneInfo, flipHorizontal, flipVertical));
		backgroundRenderer.draw(trackedImage);
	}

	private float [] calculateBackgroundPlaneMatrix(float[] backgroundPlaneInfo, boolean flipHorizontal, boolean flipVertical) {
		float [] matrix = new float[16];
		Matrix.setIdentityM(matrix, 0);

		if (backgroundPlaneInfo[0] >= 0) {
			float maxX = backgroundPlaneInfo[0];
			float maxY = backgroundPlaneInfo[1];
			float minX = backgroundPlaneInfo[2];
			float minY = backgroundPlaneInfo[3];
			float z = backgroundPlaneInfo[4];

			float x = maxX - minX;
			float y = maxY - minY;

			if(flipVertical) {
				x = -1 * x;
			}

			if(flipHorizontal) {
				y = -1 * y;
			}

			float [] position = new float[3];
			position[0] = (maxX + minX) / 2.0f;
			position[1] = (maxY + minY) / 2.0f;
			position[2] = z;

			Matrix.translateM(matrix, 0, position[0], position[1], position[2]);
			Matrix.scaleM(matrix, 0, x, y, 1.0f);
		}

		return matrix;
	}
}
