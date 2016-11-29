package com.wk.com.mobileplay.Activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wk.com.mobileplay.R;
import com.wk.com.mobileplay.Util.MusicPlayerUtil;
import com.wk.com.mobileplay.bean.MusicBean;

import java.util.ArrayList;

import static com.wk.com.mobileplay.R.id.imageView;

/**
 * Created by wangkui on 2016/11/22.
 */

public class MusicPlayActivity extends Activity implements View.OnClickListener{


    private TextView tv_currt_time;
    private SeekBar seekbar,seekbar_voice;
    private TextView tv_total;
    private TextView music_name;
    public static ImageView iv_preview,imageView_record;
    private ImageView iv_play;
    private ImageView iv_next;
    private ArrayList<MusicBean> objects;
    private int position;
    //当前播放的位置
    private int currtIndex;
    private boolean playing;
    float degress = 0;

    private ImageView iv_record;
    private int currtVoice;
    private int maxvoice;
    private AudioManager audioManager;
    private boolean isMute = false;
    private Animation animation;
    private MusicPlayerUtil musicPlayerUtil;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play_activity);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if(intent != null){
            objects = (ArrayList<MusicBean>) intent.getSerializableExtra("objects");
            position = intent.getIntExtra("position",0);
            currtIndex = position;
            playing = true;
            musicPlayerUtil = new MusicPlayerUtil(objects,tv_currt_time,this,seekbar,seekbar_voice,tv_total,music_name);
            musicPlayerUtil.PlayMusicBySd(position);
        }
    }

    private void initView() {
        tv_currt_time = (TextView) findViewById(R.id.tv_currt_time);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        seekbar_voice = (SeekBar) findViewById(R.id.seekbar_voice);
        music_name = (TextView) findViewById(R.id.music_name);
        tv_total = (TextView) findViewById(R.id.tv_total);
        imageView_record = (ImageView) findViewById(imageView);
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_record = (ImageView) findViewById(R.id.iv_record);
        iv_preview.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        imageView_record.setOnClickListener(this);
        setRota();

    }


    private void setRota(){
        animation = AnimationUtils.loadAnimation(this, R.anim.animation_music_record);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        iv_record.startAnimation(animation);

    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_preview:
                if(currtIndex > 0 && objects != null && objects.size()>0){
                    currtIndex--;
                    musicPlayerUtil.PlayMusicBySd(currtIndex);
                }else{
                    Toast.makeText(this,"已经是第一首歌曲",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_next:
                if( objects != null && objects.size()>0 && currtIndex < objects.size()-1){
                    currtIndex++;
                    musicPlayerUtil.PlayMusicBySd(currtIndex);
                }else{
                    Toast.makeText(this,"已经是最后一首歌曲",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_play:
                if(playing){
//                    if(animation != null ) {
//                        iv_record.clearAnimation();
//                    }
                    playing = false;
                    iv_play.setImageResource(R.drawable.media_stop);
                }else{
//                    if(animation != null ) {
//                        animation.reset();
//                        iv_record.setAnimation(animation);
//                    }
                    playing = true;
                    iv_play.setImageResource(R.drawable.media_playing);
                }
                musicPlayerUtil.isPlay();
                break;
            case imageView:
                if(!isMute){
                    //当前音乐播放声音
                    currtVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,AudioManager.FLAG_PLAY_SOUND);
                    imageView_record.setImageResource(R.drawable.audio_player_mute);
                    seekbar_voice.setProgress(0);
                    isMute = true;
                }else{
                    isMute = false;
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currtVoice,AudioManager.FLAG_PLAY_SOUND);
                    imageView_record.setImageResource(R.drawable.audio_player);
                }
                break;
            default:

                break;

        }
    }

}
