package com.maxst.ar.sample.webview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.maxst.ar.sample.arobject.WebViewRenderer;

public class WebViewContainer extends LinearLayout {

	private WebViewRenderer webViewRenderer;

	public WebViewContainer(Context context) {
		super(context);
	}

	public WebViewContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void draw(Canvas canvas) {
		if (webViewRenderer == null) {
			return;
		}

		Canvas glAttachedCanvas = webViewRenderer.lockCanvas();
		if(glAttachedCanvas != null) {
			//prescale canvas to make sure content fits
			float xScale = glAttachedCanvas.getWidth() / (float)canvas.getWidth();
			glAttachedCanvas.scale(xScale, xScale);
			//draw the view to provided canvas
			super.draw(glAttachedCanvas);
		}
		// notify the canvas is updated
		webViewRenderer.unlockCanvas();
	}

	public void setRenderer(WebViewRenderer webViewRenderer) {
		this.webViewRenderer = webViewRenderer;
	}
}
