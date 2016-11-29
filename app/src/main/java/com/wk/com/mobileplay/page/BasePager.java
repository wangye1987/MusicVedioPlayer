package com.wk.com.mobileplay.page;

import android.content.Context;
import android.view.View;

/**
 * Created by wangkui on 2016/11/21.
 *
 * 所有视图页面的基类
 */

public abstract class BasePager {
    public Context mContext;
    public View baseView;
    public boolean isInitData;

    public BasePager(Context mContext) {
        this.mContext = mContext;
        baseView = initView();
    }

    /*
    *
    * 初始化视图
    * */

    public abstract View initView();

    /*
    *
    * 初始化数据
    * */
    public  void initData(){

    };
}

