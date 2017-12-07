/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.util;

import com.maxst.ar.BackgroundRenderer;
import com.maxst.ar.BackgroundTexture;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.TrackerManager;

public class SampleRenderer {

	private BackgroundQuad backgroundQuad;
	private BackgroundRenderer backgroundRenderer;
	private int trackerType;

	public SampleRenderer() {}

	public SampleRenderer(int trackerType) {
		this.trackerType = trackerType;
	}

	public void onSurfaceCreated() {
		backgroundQuad = new BackgroundQuad();
		backgroundRenderer = BackgroundRenderer.getInstance();

		if (trackerType == TrackerManager.TRACKER_TYPE_SLAM) {
			backgroundRenderer.setRenderingOption(BackgroundRenderer.RenderingOption.FEATURE_RENDERER,
					BackgroundRenderer.RenderingOption.PROGRESS_RENDERER,
					BackgroundRenderer.RenderingOption.SURFACE_MESH_RENDERER,
					BackgroundRenderer.RenderingOption.AXIS_RENDERER);
		} else if (trackerType == TrackerManager.TRACKER_TYPE_CODE_SCANNER) {
			backgroundRenderer.setRenderingOption(BackgroundRenderer.RenderingOption.VIEW_FINDER_RENDERER);
		}
	}

	public void onDrawFrame() {
		BackgroundTexture  backgroundTexture = backgroundRenderer.getBackgroundTexture();
		if (backgroundTexture == null) {
			return;
		}

		backgroundRenderer.begin(backgroundTexture);
		backgroundRenderer.renderBackgroundToTexture();
		backgroundRenderer.end();

		backgroundQuad.draw(backgroundTexture, CameraDevice.getInstance().getBackgroundPlaneProjectionMatrix());
	}
}
