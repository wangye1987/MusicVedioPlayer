package com.wk.com.mobileplay.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wk.com.mobileplay.Activity.AllPlayerActivity;
import com.wk.com.mobileplay.R;
import com.wk.com.mobileplay.Util.MusicPlayerUtil;
import com.wk.com.mobileplay.Util.ToHourUtils;
import com.wk.com.mobileplay.bean.VedioBean;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by wangkui on 2016/11/22.
 */

public class VedioSdAdapter extends ArrayAdapter<VedioBean> {
    private ArrayList<VedioBean> objects;
    private Context mContext;
    private View view;
    private MusicPlayerUtil musicPlayerUtil;

    public VedioSdAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public VedioSdAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<VedioBean> objects) {
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
        VedioBean vediobean = objects.get(position);
        Holder holder = null;
        if(convertView == null){
            holder = new Holder();
            convertView = LinearLayout.inflate(mContext, R.layout.vedio_item,null);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.tv_Vedio_name = (TextView) convertView.findViewById(R.id.tv_vedio_name);
        holder.tv_vedio_size = (TextView) convertView.findViewById(R.id.tv_vedio_size);
        holder.tv_vedio_duration = (TextView) convertView.findViewById(R.id.tv_vedio_duration);

        holder.tv_Vedio_name.setText(vediobean.getVedioName());
        holder.tv_vedio_duration.setText(ToHourUtils.getInstance().stringForTime((int)vediobean.getVedioDurtion()));
        holder.tv_vedio_size.setText(vediobean.getVedioSize()/(1024 * 1024) +"."+ musicPlayerUtil.TwoPoint(vediobean.getVedioSize()
                %(1024 * 1024))+"MB");
        convertView.setOnClickListener(new ItemtClick(objects,position));
        return convertView;
    }


    public class ItemtClick implements View.OnClickListener {

        private ArrayList<VedioBean> objects;
        private int position;
        ItemtClick(ArrayList<VedioBean> objects,int position){
            this.objects = objects;
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            //播放视频
//            Intent intent  = new Intent();
//            Uri uri = Uri.fromFile(new File(objects.get(position).getVedioPath()));
//            intent.setDataAndType(uri,"video/*");
//            mContext.startActivity(intent);

            //自己复写播放视频代码
            Intent intent  = new Intent(mContext,AllPlayerActivity.class);
            Uri uri = Uri.fromFile(new File(objects.get(position).getVedioPath()));
            intent.setDataAndType(uri,"video/*");
            intent.putExtra("position",position);
            intent.putExtra("objects",objects);
            mContext.startActivity(intent);
        }
    }

    public class Holder{
        private TextView tv_Vedio_name;
        private TextView tv_vedio_size;
        private TextView tv_vedio_duration;
    }
}
