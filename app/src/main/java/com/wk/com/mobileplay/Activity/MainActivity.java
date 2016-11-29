package com.wk.com.mobileplay.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wk.com.mobileplay.R;
import com.wk.com.mobileplay.page.BasePager;
import com.wk.com.mobileplay.page.MusicNetPager;
import com.wk.com.mobileplay.page.MusicSdPager;
import com.wk.com.mobileplay.page.VedioNetPager;
import com.wk.com.mobileplay.page.VedioSdPager;

import java.util.ArrayList;

/**
 * Created by wangkui on 2016/11/18.
 */

public class MainActivity extends FragmentActivity {
    private RadioButton tab_main;
    private RadioButton tab_sd_music;
    private RadioButton tab_net_music;
    private RadioButton tab_net_vedio;
    private RadioGroup main_buttom;
    private TextView line;
    private ArrayList<BasePager> list;
    //当前选中的页面下标
    private int position;
    private FrameLayout fragment_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        list = new ArrayList<BasePager>();

        list.add(new MusicSdPager(this));
        list.add(new VedioSdPager(this));
        list.add(new MusicNetPager(this));
        list.add(new VedioNetPager(this));
        main_buttom.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        getFragment();
    }


    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    switch (checkedId){
                        case R.id.tab_main:
                            position = 0;
                            break;
                        case R.id.tab_sd_music:
                            position = 1;
                            break;
                        case R.id.tab_net_music:
                            position = 2;
                            break;
                        case R.id.tab_net_vedio:
                            position = 3;
                            break;
                        default:
                            position = 0;
                            break;
                    }
            getFragment();
        }
    }


    private void getFragment() {
        //获取FragmentManager实例
        FragmentManager mfragment = getFragmentManager();
        //开始fragment事物
        FragmentTransaction mTransaction =  mfragment.beginTransaction();
        mTransaction.replace(R.id.fragment_layout,new Fragment(){
            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
                BasePager mBasePager = getBasePager();
                if(mBasePager != null){
                    return mBasePager.baseView;
                }
                return null;
            }
        });
        //提交fragment
        mTransaction.commit();
    }
    private void initView() {
        fragment_layout = (FrameLayout) findViewById(R.id.fragment_layout);
        tab_main = (RadioButton) findViewById(R.id.tab_main);
        tab_sd_music = (RadioButton) findViewById(R.id.tab_sd_music);
        tab_net_music = (RadioButton) findViewById(R.id.tab_net_music);
        tab_net_vedio = (RadioButton) findViewById(R.id.tab_net_vedio);
        main_buttom = (RadioGroup) findViewById(R.id.main_buttom);
        line = (TextView) findViewById(R.id.line);
    }


    private BasePager getBasePager(){
        BasePager mBasePager = list.get(position);
        if(mBasePager != null && !mBasePager.isInitData){
            mBasePager.initData();
            mBasePager.isInitData = true;
        }
        return mBasePager;
    }
}
