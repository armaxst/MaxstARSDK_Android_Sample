/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.util;

import com.maxst.ar.BackgroundRenderer;
import com.maxst.ar.BackgroundTexture;
import com.maxst.ar.CameraDevice;

public class BackgroundRenderHelper {

	private BackgroundQuad backgroundQuad;
	private BackgroundRenderer backgroundRenderer;
	public BackgroundRenderHelper() {}

	/**
	 * Should be called GL thread
	 */
	public void init() {
		backgroundQuad = new BackgroundQuad();
		backgroundRenderer = BackgroundRenderer.getInstance();
	}

	public void setRenderingOption(BackgroundRenderer.RenderingOption...options) {
		backgroundRenderer.setRenderingOption(options);
	}

	public void drawBackground() {
		BackgroundTexture  backgroundTexture = backgroundRenderer.getBackgroundTexture();
		if (backgroundTexture == null) {
			return;
		}

		backgroundRenderer.begin(backgroundTexture);
		backgroundRenderer.renderBackgroundToTexture();
		backgroundRenderer.end();

		backgroundQuad.draw(backgroundTexture, CameraDevice.getInstance().getBackgroundPlaneProjectionMatrix());
	}

	public void drawBackground(BackgroundTexture  backgroundTexture) {
		if (backgroundTexture == null) {
			return;
		}
		backgroundQuad.draw(backgroundTexture, CameraDevice.getInstance().getBackgroundPlaneProjectionMatrix());
	}

	public BackgroundTexture drawBackgroundToTexture() {
		BackgroundTexture  backgroundTexture = backgroundRenderer.getBackgroundTexture();
		if (backgroundTexture == null) {
			return null;
		}

		backgroundRenderer.begin(backgroundTexture);
		backgroundRenderer.renderBackgroundToTexture();
		backgroundRenderer.end();

		return backgroundTexture;
	}
}
