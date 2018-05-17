package com.maxst.ar.sample.ScreenRecorder.ui;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 2018. 5. 17..
 */
public class BindingAdapters {
	@BindingAdapter("visibleGone")
	public static void showHide(View view, boolean show) {
		view.setVisibility(show ? View.VISIBLE : View.GONE);
	}
}

