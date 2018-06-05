/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.arobject;

import com.maxst.ar.ColorFormat;
import com.maxst.ar.TrackedImage;

public class BackgroundRenderHelper {

	private BackgroundRenderer backgroundRenderer;

	public void drawBackground(TrackedImage trackedImage, float[] matrix) {
		if (backgroundRenderer == null) {
			if (trackedImage.getFormat() == ColorFormat.YUV420sp) {
				backgroundRenderer = new Yuv420spRenderer();
			} else if (trackedImage.getFormat() == ColorFormat.YUV420_888) {
				backgroundRenderer = new Yuv420_888Renderer();
			} else {
				return;
			}
		}

		backgroundRenderer.setProjectionMatrix(matrix);
		backgroundRenderer.draw(trackedImage);
	}
}
