/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;

public class PermissionCheckActivity extends AppCompatActivity {

	private static final int REQ_REQUEST_PERMISSION = 0;
	private static String[] REQUIRED_PERMISSIONS = new String[]{
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.CAMERA
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestPermissions(getDeniedPermissions());
	}

	private ArrayList<String> getDeniedPermissions() {
		ArrayList<String> deniedPermission = new ArrayList<>();
		for (String permission : REQUIRED_PERMISSIONS) {
			if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
				deniedPermission.add(permission);
			}
		}
		return deniedPermission;
	}

	private void requestPermissions(ArrayList<String> permissions) {
		if (permissions.size() == 0) {
			startMainActivity();
			return;
		}
		String[] deniedPermissionArray = new String[permissions.size()];
		permissions.toArray(deniedPermissionArray);
		ActivityCompat.requestPermissions(this, deniedPermissionArray, REQ_REQUEST_PERMISSION);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQ_REQUEST_PERMISSION:
				StringBuilder stringBuilder = new StringBuilder();
				for (int index = 0; index < grantResults.length; index++) {
					if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
						stringBuilder.append(String.format("%s\n", permissions[index]));
					}
				}
				String message = stringBuilder.toString();
				if (TextUtils.isEmpty(message)) {
					startMainActivity();
				} else {
					Toast.makeText(PermissionCheckActivity.this, String.format("Permission Denied:\n%s", message), Toast.LENGTH_SHORT).show();
					finish();
				}
				break;
		}
	}

	private void startMainActivity() {
		Intent intent = new Intent(PermissionCheckActivity.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}
}
