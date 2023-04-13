package com.samsung.cameraxandzxing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.common.util.concurrent.ListenableFuture;
import com.samsung.cameraxandzxing.databinding.ActivityMainBinding;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    private static final int PERMISSION_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        if (allPermissionGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE
            );
        }


    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> providerListenableFuture
                = ProcessCameraProvider.getInstance(this);

        providerListenableFuture.addListener(() -> {
                    try {
                        ProcessCameraProvider cameraProvider = providerListenableFuture.get();

                        Preview preview = new Preview.Builder().build();
                        preview.setSurfaceProvider(activityMainBinding.pvCamera.getSurfaceProvider());

                        CameraSelector cameraSelector = new CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

                        Camera camera = cameraProvider.bindToLifecycle(
                                this,
                                cameraSelector,
                                preview
                        );

                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                ContextCompat.getMainExecutor(this));

    }

    private boolean allPermissionGranted() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (allPermissionGranted()) {
                startCamera();
            } else {
                finish();
            }
        }
    }
}