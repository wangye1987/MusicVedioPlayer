package com.wk.com.mobileplay.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import com.wk.com.mobileplay.R;

public class WelcomeActivity extends Activity {


    private Handler mHandler = new Handler();
    boolean isOpenMain = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //2秒后启动
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StartMainActivity();
            }
        },2000);
    }

    /*
    *
    * 跳转到主界面
    * */
    private void StartMainActivity(){
        if(!isOpenMain) {
            isOpenMain = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        StartMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(this);
    }
}
