package com.wk.com.mobileplay.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wk.com.mobileplay.Activity.MusicPlayActivity;
import com.wk.com.mobileplay.R;
import com.wk.com.mobileplay.Util.MusicPlayerUtil;
import com.wk.com.mobileplay.Util.ToHourUtils;
import com.wk.com.mobileplay.bean.MusicBean;

import java.util.ArrayList;

/**
 * Created by wangkui on 2016/11/21.
 */

public class MusicSdAdapter extends ArrayAdapter<MusicBean> {
    private ArrayList<MusicBean> objects;
    private Context mContext;
    private View view;
    private MusicPlayerUtil musicPlayerUtil;

    public MusicSdAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public MusicSdAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MusicBean> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.mContext = context;
        musicPlayerUtil = new MusicPlayerUtil();
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MusicBean musicBean = objects.get(position);
        Holder holder = null;
        if(convertView == null){
            holder = new Holder();
            convertView = LinearLayout.inflate(mContext, R.layout.music_item,null);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.iv_music = (ImageView) convertView.findViewById(R.id.iv_music);
        holder.tv_music_name = (TextView) convertView.findViewById(R.id.tv_music_name);
        holder.tv_music_duration = (TextView) convertView.findViewById(R.id.tv_music_duration);
        holder.iv_music_artise = (TextView) convertView.findViewById(R.id.tv_music_artise);
        holder.iv_music_size = (TextView) convertView.findViewById(R.id.tv_music_size);

        holder.tv_music_name.setText(musicBean.getMusicName());
        holder.tv_music_duration.setText(ToHourUtils.getInstance().SecondXp(musicBean.getMusicDurtion()));
        holder.iv_music_artise.setText(musicBean.getMusicArtise());
        holder.iv_music_size.setText(musicBean.getMusicSize()/(1024 * 1024) +"."+musicPlayerUtil.TwoPoint(musicBean.getMusicSize()%(1024 * 1024))+"MB");
        convertView.setOnClickListener(new ItemtClick(objects,position));
        return convertView;
    }


    public class ItemtClick implements View.OnClickListener {

        private ArrayList<MusicBean> objects;
        private int position;
        ItemtClick(ArrayList<MusicBean> objects,int position){
            this.objects = objects;
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MusicPlayActivity.class);
            intent.putExtra("position",position);
            intent.putExtra("objects",objects);
            mContext.startActivity(intent);
        }
    }

    public class Holder{
        private ImageView iv_music;
        private TextView tv_music_name;
        private TextView tv_music_duration;
        private TextView iv_music_artise;
        private TextView iv_music_size;

    }
}
