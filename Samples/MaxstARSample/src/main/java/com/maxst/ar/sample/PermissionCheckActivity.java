/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PermissionCheckActivity extends Activity {
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkVerify();
    }

    private void checkVerify() {

        // 사용자가 이미 앱에 권한을 부여했는지 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2 || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||ContextCompat.checkSelfPermission(this, Manifest.permission.HIGH_SAMPLING_RATE_SENSORS) == PackageManager.PERMISSION_GRANTED)) {
            performAction();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.HIGH_SAMPLING_RATE_SENSORS)) {

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        } else {
            // You can directly ask for the permission.
            String[] permissions;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
                permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.HIGH_SAMPLING_RATE_SENSORS};
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                permissions = new String[]{Manifest.permission.CAMERA};
            } else {
                permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            }

            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            Log.i("PermissionCheckActivity", "requestPermissions");
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        Log.i("PermissionCheckActivity", "hasPermission");

        if (context == null || permissions == null || permissions.length == 0) {
            return false;
        }

        Log.i("PermissionCheckActivity", Integer.toString(permissions.length));

        for (String permission : permissions) {
            Log.i("PermissionCheckActivity", permission);

            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.i("PermissionCheckActivity", permission + " false");
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (String permission : permissions) {
            Log.i("PermissionCheckActivity", permission);
        }

        for (int grantResult : grantResults) {
            Log.i("PermissionCheckActivity", Integer.toString(grantResult));
        }

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.

                boolean permissionGrant = hasPermissions(this, permissions);

                if (permissionGrant) {
                    performAction();
                } else {

                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    private void performAction() {
        Log.i("PermissionCheckActivity", "performAction");
        startActivity(new Intent(PermissionCheckActivity.this, MainActivity.class));
        finish();
    }
}
