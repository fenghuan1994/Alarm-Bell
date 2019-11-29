package com.hct.alarmbell;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

public class ScreenFlashActivity extends Activity {

    private int mNum =0;



    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mNum++;
                changeColor(mNum);
            }
        }
    };

    private void changeColor(int num) {
        switch (num % 4) {
            case 0:
                this.findViewById(R.id.background).setBackgroundColor(Color.RED);
                break;
            case 1:
                this.findViewById(R.id.background).setBackgroundColor(Color.GREEN);
                break;
            case 2:
                this.findViewById(R.id.background).setBackgroundColor(Color.BLUE);
                break;
            case 3:
                this.findViewById(R.id.background).setBackgroundColor(Color.WHITE);
                break;
        }
        mNum++;
    }

}
