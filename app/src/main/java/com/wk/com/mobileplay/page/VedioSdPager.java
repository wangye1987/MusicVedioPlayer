package com.wk.com.mobileplay.page;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wk.com.mobileplay.Adapter.VedioSdAdapter;
import com.wk.com.mobileplay.R;
import com.wk.com.mobileplay.bean.VedioBean;

import java.util.ArrayList;
/**
 * Created by wangkui on 2016/11/21.
 */

public class VedioSdPager extends BasePager {

    private TextView tv;
    private ListView lv_vedio_sd;
    private ArrayList<VedioBean> listVedio;
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(listVedio != null && listVedio.size() > 0){
                lv_vedio_sd.setAdapter(new VedioSdAdapter(mContext,0,listVedio));
                tv_empty.setVisibility(View.GONE);
            }else{
                tv_empty.setVisibility(View.VISIBLE);
            }
            pb_loading.setVisibility(View.GONE);
        }
    };
    private TextView tv_empty;
    private ProgressBar pb_loading;

    public VedioSdPager(Context mContext) {
        super(mContext);
    }

    @Override
    public View initView() {
        View view = LinearLayout.inflate(mContext, R.layout.vedio_sd_page,null);
        lv_vedio_sd = (ListView) view.findViewById(R.id.lv_vedio_sd);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        return view;
    }


    @Override
    public void initData() {
        super.initData();
        listVedio = new ArrayList<VedioBean>();
        //获取视频列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                listVedio =  getSdVedio();
            }
        }).start();
    }


    /**
     *
     * 获取视频列表
     * */
    public ArrayList<VedioBean> getSdVedio() {
        ContentResolver resolve = mContext.getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String args[] = new String[]{
                MediaStore.Video.Media.DISPLAY_NAME,//名字
                MediaStore.Video.Media.SIZE,//大小
                MediaStore.Video.Media.DURATION,//时长
                MediaStore.Video.Media.DATA,//路径
                MediaStore.Video.Media.ARTIST,//艺术家
                MediaStore.Video.Media.TITLE,

        };
        Cursor cursor = resolve.query(uri,args,null,null,null);
        if(cursor != null){
            while(cursor.moveToNext()){
                VedioBean vedioBean = new VedioBean();
                vedioBean.setVedioName(cursor.getString(0));
                vedioBean.setVedioSize(cursor.getInt(1));
                vedioBean.setVedioDurtion(Long.parseLong(cursor.getString(2)));
                vedioBean.setVedioPath(cursor.getString(3));
                vedioBean.setVedioArtise(cursor.getString(4));
                listVedio.add(vedioBean);
            }
            cursor.close();
            mhandler.sendEmptyMessage(0);
        }
        return listVedio;
    }
}
