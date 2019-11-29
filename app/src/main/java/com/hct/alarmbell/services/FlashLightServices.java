package com.hct.alarmbell.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FlashLightServices extends Service {
    private boolean isFlash = false;
    private Camera mCamera;
    private String mFlashPath = "/sys/devices/virtual/flashlight_core/flashlight/flashlight_contrl";
    private boolean isHaveFlashFile = false;
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            flickFlash();
        }
    });

    public FlashLightServices() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if ((new File("/sys/devices/virtual/flashlight_core/flashlight/flashlight_contrl")).exists()) {
            isHaveFlashFile = true;
        } else if ((new File("/sys/bus/platform/drivers/kd_camera_flashlight/flashContrl")).exists()) {
            mFlashPath = "/sys/bus/platform/drivers/kd_camera_flashlight/flashContrl";
            isHaveFlashFile = true;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isFlash) {
            isFlash = true;
            thread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        isFlash = false;
        releaseCamera();
        super.onDestroy();
    }

    private void flickFlash() {
        while (isFlash) {
            int i;
            for (i=0; i<3; i++) {
                if (isFlash) {
                    if (isHaveFlashFile) {
                        writeFile(mFlashPath, "on");
                    } else {
                        openFlash();
                    }
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isHaveFlashFile) {
                        writeFile(mFlashPath, "off");
                    } else {
                        closeFlash();
                    }
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (isFlash) {
                try {
                    Thread.sleep(800);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //lining add 1018 systemui close flashlight
        if(!isFlash){
            Intent i = new Intent();
            i.setAction("hct_action_longpress_home_close_flashlight");
            sendBroadcast(i);
        }
        //lining add 1018 systemui close flashlight
    }

    private void openFlash() {
        try {
            if (mCamera == null) {
                mCamera = getCamera();
            }
            mCamera.startPreview();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Camera getCamera() {
        return android.hardware.Camera.open();
    }

    private void closeFlash() {
        try {
            if (mCamera == null)
                getCamera();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void writeFile(String path, String value) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            outputStream.write(value.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("FlashService", "file not found, error = " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FlashService", "io error, error = " + e.toString());
        }
    }
}
