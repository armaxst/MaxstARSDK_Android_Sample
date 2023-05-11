/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.maxst.ar.sample.camera_config.CameraConfigureActivity;
import com.maxst.ar.sample.cloudRecognizer.CloudRecognizerActivity;
import com.maxst.ar.sample.code.CodeScanActivity;
import com.maxst.ar.sample.imageFusionTracker.ImageFusionTrackerActivity;
import com.maxst.ar.sample.imageTracker.ImageTrackerActivity;
import com.maxst.ar.sample.instantFusionTracker.InstantFusionTrackerActivity;
import com.maxst.ar.sample.instantTracker.InstantTrackerActivity;
import com.maxst.ar.sample.markerFusionTracker.MarkerFusionTrackerActivity;
import com.maxst.ar.sample.markerTracker.MarkerTrackerActivity;
import com.maxst.ar.sample.objectFusionTracker.ObjectFusionTrackerActivity;
import com.maxst.ar.sample.qrcodeFusionTracker.QrCodeFusionTrackerActivity;
import com.maxst.ar.sample.qrcodeTracker.QrCodeTrackerActivity;
import com.maxst.ar.sample.objectTracker.ObjectTrackerActivity;
import com.maxst.ar.sample.spaceTracker.SpaceTrackerActivity;
import com.maxst.ar.sample.wearable.WearableDeviceRenderingActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.image_target).setOnClickListener(this);
        findViewById(R.id.instant_image_target).setOnClickListener(this);
        findViewById(R.id.marker_target).setOnClickListener(this);
        findViewById(R.id.object_target).setOnClickListener(this);
        findViewById(R.id.code_target).setOnClickListener(this);
        findViewById(R.id.see_through_hmd_rendering).setOnClickListener(this);
        findViewById(R.id.camera_configuration).setOnClickListener(this);
        findViewById(R.id.cloud_tracker).setOnClickListener(this);
        findViewById(R.id.qr_code_tracker).setOnClickListener(this);
        findViewById(R.id.image_fusion_target).setOnClickListener(this);
        findViewById(R.id.object_fusion_target).setOnClickListener(this);
        findViewById(R.id.marker_fusion_target).setOnClickListener(this);
        findViewById(R.id.qrcode_fusion_target).setOnClickListener(this);
        findViewById(R.id.instant_fusion_target).setOnClickListener(this);
        findViewById(R.id.space_target).setOnClickListener(this);

        findViewById(R.id.settings).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_target:
                startActivity(new Intent(MainActivity.this, ImageTrackerActivity.class));
                break;

            case R.id.marker_target:
                startActivity(new Intent(MainActivity.this, MarkerTrackerActivity.class));
                break;

            case R.id.instant_image_target:
                startActivity(new Intent(MainActivity.this, InstantTrackerActivity.class));
                break;

            case R.id.object_target:
                startActivity(new Intent(MainActivity.this, ObjectTrackerActivity.class));
                break;

            case R.id.code_target:
                startActivity(new Intent(MainActivity.this, CodeScanActivity.class));
                break;

            case R.id.see_through_hmd_rendering:
                startActivity(new Intent(MainActivity.this, WearableDeviceRenderingActivity.class));
                break;

            case R.id.camera_configuration:
                startActivity(new Intent(MainActivity.this, CameraConfigureActivity.class));
                break;

            case R.id.cloud_tracker:
                startActivity(new Intent(MainActivity.this, CloudRecognizerActivity.class));
                break;

            case R.id.qr_code_tracker:
                startActivity(new Intent(MainActivity.this, QrCodeTrackerActivity.class));
                break;

            case R.id.image_fusion_target:
                startActivity(new Intent(MainActivity.this, ImageFusionTrackerActivity.class));
                break;

            case R.id.object_fusion_target:
                startActivity(new Intent(MainActivity.this, ObjectFusionTrackerActivity.class));
                break;

            case R.id.marker_fusion_target:
                startActivity(new Intent(MainActivity.this, MarkerFusionTrackerActivity.class));
                break;

            case R.id.qrcode_fusion_target:
                startActivity(new Intent(MainActivity.this, QrCodeFusionTrackerActivity.class));
                break;

            case R.id.instant_fusion_target:
                startActivity(new Intent(MainActivity.this, InstantFusionTrackerActivity.class));
                break;

            case R.id.space_target:
                startActivity(new Intent(MainActivity.this, SpaceTrackerActivity.class));
                break;

            case R.id.settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
    }
}
