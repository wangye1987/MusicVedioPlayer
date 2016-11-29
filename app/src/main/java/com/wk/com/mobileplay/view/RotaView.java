package com.wk.com.mobileplay.view;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wangkui on 2016/11/24.
 */

public class RotaView extends ImageView implements  Runnable{

    private float degress;

    public RotaView(Context context) {
        this(context,null);
    }

    public RotaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RotaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setPivotX(getWidth()/2);
        setPivotY(getHeight()/2);
    }

    @Override
    public void run() {
        while(true){
            degress+=5;
            this.setRotation(degress);
            postInvalidate();
            SystemClock.sleep(50);
        }
    }
}
