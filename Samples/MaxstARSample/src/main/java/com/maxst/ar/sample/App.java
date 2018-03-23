package com.maxst.ar.sample;

import android.app.Application;
import android.content.Context;

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by charles on 2018. 3. 23..
 */

public class App extends Application {
	private static Context context;

	public static Context getContext() {
		return context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
	}
}
