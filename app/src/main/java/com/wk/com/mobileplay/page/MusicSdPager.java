package com.wk.com.mobileplay.page;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.ActivityChooserView;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wk.com.mobileplay.Adapter.MusicSdAdapter;
import com.wk.com.mobileplay.R;
import com.wk.com.mobileplay.bean.MusicBean;

import java.util.ArrayList;

/**
 * Created by wangkui on 2016/11/21.
 */

public class MusicSdPager extends BasePager {

    private TextView tv;
    private ListView lv_music_sd;
    private ProgressBar pb_loading;
    private ArrayList<MusicBean> listMusic;

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(listMusic != null && listMusic.size() > 0){
                lv_music_sd.setAdapter(new MusicSdAdapter(mContext,0,listMusic));
                tv_empty.setVisibility(View.GONE);
            }else{
                tv_empty.setVisibility(View.VISIBLE);
            }
            pb_loading.setVisibility(View.GONE);
        }
    };
    private TextView tv_empty;

    public MusicSdPager(Context mContext) {
        super(mContext);
    }

    @Override
    public View initView() {
        View view = ActivityChooserView.InnerLayout.inflate(mContext, R.layout.music_sd_page,null);
        lv_music_sd = (ListView) view.findViewById(R.id.lv_music_sd);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        listMusic = new ArrayList<MusicBean>();
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //获取音乐列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                 listMusic =  getSdMusic();
            }
        }).start();
    }



    /**
     *
     * 获取音乐列表
     * */
    public ArrayList<MusicBean> getSdMusic() {
        ContentResolver resolve = mContext.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String args[] = new String[]{
                MediaStore.Audio.Media.DISPLAY_NAME,//名字
                MediaStore.Audio.Media.SIZE,//大小
                MediaStore.Audio.Media.DURATION,//时长
                MediaStore.Audio.Media.DATA,//路径
                MediaStore.Audio.Media.ARTIST,//艺术家
                MediaStore.Audio.Media.TITLE,

        };
        Cursor cursor = resolve.query(uri,args,null,null,null);
        if(cursor != null){
         while(cursor.moveToNext()){
             MusicBean musicBean = new MusicBean();
             musicBean.setMusicSize(cursor.getInt(1));
             musicBean.setMusicDurtion(cursor.getFloat(2));
             musicBean.setMusicPath(cursor.getString(3));
             musicBean.setMusicArtise(cursor.getString(4));
             musicBean.setMusicName(cursor.getString(5));
             listMusic.add(musicBean);
           }
           cursor.close();

            mhandler.sendEmptyMessage(0);
        }
        return listMusic;
    }
}
