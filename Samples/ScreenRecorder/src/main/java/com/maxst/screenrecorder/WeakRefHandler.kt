/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.screenrecorder

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

class WeakRefHandler constructor(looper: Looper, listener: MessageListener) : Handler(looper) {

	private val listenerWeakReference = WeakReference(listener)

	override fun handleMessage(msg: Message) {
		super.handleMessage(msg)
		listenerWeakReference.get()?.let {
			it.handleMessage(msg)
		}
	}

	interface MessageListener {
		fun handleMessage(message: Message)
	}

}