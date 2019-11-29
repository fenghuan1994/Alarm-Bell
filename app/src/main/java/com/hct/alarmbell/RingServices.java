package com.hct.alarmbell;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.hct.alarmbell.R;

public class RingServices extends Service {
    private static final String TAG = "RingServices";
    private boolean isRing = false;
    private MediaPlayer mMediaPlayer;
    private int rings = R.raw.warn;

    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            startRing();
        }
    });
    public RingServices() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mMediaPlayer = MediaPlayer.create(this, rings);
        mMediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRing) {
            isRing = true;
            thread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw null;
    }

    private void startRing() {
        Log.d(TAG, "startRing: ");
        try {
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        isRing = false;
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
