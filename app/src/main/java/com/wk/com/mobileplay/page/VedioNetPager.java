package com.wk.com.mobileplay.page;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.wk.com.mobileplay.Log.LogUtil;

/**
 * Created by wangkui on 2016/11/21.
 */

public class VedioNetPager extends BasePager {

    private TextView tv;

    public VedioNetPager(Context mContext) {
        super(mContext);
    }

    @Override
    public View initView() {
        tv = new TextView(mContext);
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    @Override
    public void initData() {
        super.initData();
        tv.setText("网络视频");
        LogUtil.d("网络视频初始化了");
    }
}
