package com.wk.com.mobileplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * Created by wangkui on 2016/11/28.
 */

public class ViewVedio extends VideoView {
    public ViewVedio(Context context) {
        this(context,null);
    }

    public ViewVedio(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ViewVedio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }



    public void SetVideoViewSize(int viewWidth,int viewHeight){
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = viewWidth;
        lp.height = viewHeight;
        setLayoutParams(lp);
    }
}
