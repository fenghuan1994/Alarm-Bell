package com.hct.alarmbell;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private CheckBox mFlashBtn,mAlarmBtn, mScreenBtn;
    private Intent FlashLightServices;
    private Intent RingServices;
    private boolean mClickfirst = true;
    boolean mClick =true;
    private boolean mScreenFlag = false;
    private int mNum =0;
    private Timer timer = null;
    private Handler myHandler = null;
    private TimerTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmbell_main);
        mFlashBtn = (CheckBox) findViewById(R.id.flashlight);
        mFlashBtn.setOnClickListener(this);
        mAlarmBtn = (CheckBox) findViewById(R.id.alarmbell);
        mAlarmBtn.setOnClickListener(this);
        mScreenBtn = (CheckBox) findViewById(R.id.screenflash);
        mScreenBtn.setOnClickListener(this);
        FlashLightServices = new Intent();
        RingServices = new Intent();
        FlashLightServices.setClass(MainActivity.this, FlashLightServices.class);
        RingServices.setClass(MainActivity.this, RingServices.class);
        initialComponment();
        timer.schedule(task, 200, 200);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flashlight:
                if(mClickfirst){
                    startService(FlashLightServices);
                    Toast.makeText(MainActivity.this, "我叫 ： flashlight", Toast.LENGTH_SHORT).show();
                    mClickfirst = false;
                }else{
                    stopService(FlashLightServices);
                    mClickfirst = true;
                    Toast.makeText(MainActivity.this, "我叫 ： CLOSE flashlight", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.alarmbell:
                if(mClick){
                    startService(RingServices);
                    Toast.makeText(MainActivity.this, "我叫 ： alarmbell", Toast.LENGTH_SHORT).show();
                    mClick = false;
                }else{
                    stopService(RingServices);
                    mClick = true;
                    Toast.makeText(MainActivity.this, "我叫 ： Closealarmbell", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.screenflash:
                if (!mScreenFlag) {
                    mScreenFlag = true;
                    Toast.makeText(MainActivity.this, "我叫 ： open screenflash", Toast.LENGTH_SHORT).show();
                }else {
                    mScreenFlag = false;
                    Toast.makeText(MainActivity.this, "我叫 ： CLose screenflash", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(MainActivity.this, "我叫 ： default", Toast.LENGTH_LONG).show();
        }
    }

    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume: ");

    }

    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    protected void onDestroy(){
        super.onDestroy();
        stopService(FlashLightServices);
        stopService(RingServices);
        timer.cancel();
    }

    private void initialComponment() {
        // TODO Auto-generated method stub
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                // TODO Auto-generated method stub
                if (!mScreenFlag) {
                    message.what = 0;
                    myHandler.sendMessage(message);
                    return;
                }
                mNum ++;
                int mMsg = mNum % 4;
                message.what = mMsg;
                myHandler.sendMessage(message);
            }
        };
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch (msg.what) {
                    case 0:
                        MainActivity.this.findViewById(R.id.background).setBackgroundColor(Color.WHITE);
                        break;
                    case 1:
                        MainActivity.this.findViewById(R.id.background).setBackgroundColor(Color.RED);
                        break;
                    case 2:
                        MainActivity.this.findViewById(R.id.background).setBackgroundColor(Color.BLUE);
                        break;
                    case 3:
                        MainActivity.this.findViewById(R.id.background).setBackgroundColor(Color.GREEN);
                        break;
                    default:
                        MainActivity.this.findViewById(R.id.background).setBackgroundColor(Color.WHITE);
                }
                super.handleMessage(msg);
            }

        };
    }
    private void modeflash() {
        // TODO Auto-generated method stub


    }


}
