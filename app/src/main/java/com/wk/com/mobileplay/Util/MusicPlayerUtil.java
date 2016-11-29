package com.wk.com.mobileplay.Util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wk.com.mobileplay.R;
import com.wk.com.mobileplay.bean.MusicBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import static android.content.ContentValues.TAG;
import static com.wk.com.mobileplay.Activity.MusicPlayActivity.imageView_record;
import static com.wk.com.mobileplay.Util.MessageWhatUtil.MESSAGE_UPDATE_SECONDS;

/**
 * Created by wangkui on 2016/11/22.
 * <p>
 * 音频播放类
 */

public class MusicPlayerUtil {
    public MusicPlayerUtil musicPlayerUtil;
    public static MediaPlayer mediaPlayer;
    private ArrayList<MusicBean> objects;
    //    private int position;
    private String musicPath;
    //当前播放的音乐下标
    private int playPosition;
    //当前音乐总时长 单位毫秒
    private float musicDurtion;
    private Timer timer;
    private Context mContext;
    TextView tv_currt_time;
    private int maxvoice;
    private AudioManager audioManager;
    private SeekBar seekBar_voice;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //获取音乐播放进度条秒显示

                case MESSAGE_UPDATE_SECONDS:
                    if(mediaPlayer != null) {
                        tv_currt_time.setText(ToHourUtils.getInstance().SecondXp(mediaPlayer.getCurrentPosition()));
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());

                        removeMessages(MESSAGE_UPDATE_SECONDS);

                        sendEmptyMessageDelayed(MESSAGE_UPDATE_SECONDS, 1000);
                    }else{
                        removeMessages(MESSAGE_UPDATE_SECONDS);
                    }
                    break;
                case MessageWhatUtil.MESSAGE_UPDATE_POSITION:

                    break;
            }
        }
    };


    public MusicPlayerUtil() {

    }

    public MusicPlayerUtil(ArrayList<MusicBean> objects,TextView tv_currt_time, Context mContext, SeekBar seekBar,SeekBar seekBar_voice, TextView tv_totalTime, TextView tv_showName) {
        this.objects = objects;
        this.mContext = mContext;
        this.seekBar = seekBar;
        this.tv_currt_time = tv_currt_time;
        this.tv_totalTime = tv_totalTime;
        this.tv_showName = tv_showName;
        this.seekBar_voice = seekBar_voice;
        getSystemVoice();
    }

    /*
    *
    * SD卡播放音乐
    *
    * MusicPath 音乐播放路径
    * */
    public void PlayMusicBySd(int position) {
        this.playPosition = position;
        mediaPlayer = getInstance();
        PlayMusic();
    }

    public static MediaPlayer getInstance(){
        if (mediaPlayer == null) {
            synchronized (MediaPlayer.class){
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }
            }
        }
            return mediaPlayer;
        }

    //初始化SeekBar
    private void initSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);
    }

    /*
    * 初始化歌曲名字和总时长
    * */
    private void initNameAndTime() {
        tv_showName.setText(objects.get(playPosition).getMusicName());
        tv_totalTime.setText(ToHourUtils.getInstance().SecondXp(objects.get(playPosition).getMusicDurtion()));
    }

    /*
    *
    * 播放音乐
    * */
    private SeekBar seekBar;
    private TextView tv_totalTime;
    private TextView tv_showName;

    private void PlayMusic() {
        if (objects.size() > 0 && playPosition < objects.size()) {
            musicPath = objects.get(playPosition).getMusicPath();
            musicDurtion = objects.get(playPosition).getMusicDurtion();
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicPath);
                mediaPlayer.prepare();
                SetListener();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, "播放列表为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void SetListener() {
        mediaPlayer.setOnCompletionListener(new MusicCompletionListener());
        mediaPlayer.setOnPreparedListener(new MyPerparedLister());
        seekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
        seekBar_voice.setOnSeekBarChangeListener(new MyVoiceOnSeekBarChangeListener());
    }


    /*
   * 获取系统声音
   * */
    private void getSystemVoice(){
        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        //视频播放最大声音
        maxvoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    class  MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              if(fromUser){
                  mediaPlayer.seekTo(progress);
                  tv_currt_time.setText(ToHourUtils.getInstance().SecondXp(progress));
              }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
    /*
        * 声音
        * */
    class  MyVoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(progress * 0.01 * maxvoice),AudioManager.FLAG_PLAY_SOUND);
            if(progress > 0){
                imageView_record.setImageResource(R.drawable.audio_player);
            }else{
                imageView_record.setImageResource(R.drawable.audio_player_mute);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    class MyPerparedLister implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();
            //1：设置音乐最大播放刻度
            //初始化名字和歌曲总时长
            initNameAndTime();

            //初始化seekbar
            initSeekBar();
            //2 ：发送消息 更新seekbar

            mHandler.sendEmptyMessage(MESSAGE_UPDATE_SECONDS);


            seekBar_voice.setProgress(seekBar_voice.getMax()/2);
        }
    }

    /*
    * 判断是否播放
    * */

    boolean playing;

    public void isPlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    /*
    * 暂停
    * */
    public void StopPlay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    /*
    * 播放
    * */
    public void startPlay() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    public class MusicCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.d(TAG, "onCompletion: ");
            if (mp != null) {
                if (objects != null && playPosition - 1 < objects.size()) {
                    Log.d(TAG, "onCompletion: ");
                    playPosition++;
                    PlayMusic();
                }
            }
        }
    }


    private void UpdateData() {
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = MessageWhatUtil.MESSAGE_UPDATE_POSITION;
            msg.arg1 = playPosition;
            mHandler.sendMessage(msg);
        }
    }



    /*
    * 转化为小时分钟秒
    * */
    public String TwoPoint(float number) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
//        Integer.toString((int)number).split(0,2);
        if (Integer.toString((int) number).length() >= 2) {
            return Integer.toString((int) number).substring(0, 2);
        } else {
            return Integer.toString((int) number).substring(0, 1) + "0";
        }
    }

    // a integer to xx:xx:xx
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
