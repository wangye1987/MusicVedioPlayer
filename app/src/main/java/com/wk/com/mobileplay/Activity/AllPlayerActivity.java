package com.wk.com.mobileplay.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wk.com.mobileplay.R;
import com.wk.com.mobileplay.Util.MessageWhatUtil;
import com.wk.com.mobileplay.Util.ToHourUtils;
import com.wk.com.mobileplay.bean.VedioBean;
import com.wk.com.mobileplay.view.ViewVedio;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.wk.com.mobileplay.Util.MessageWhatUtil.MESSAGE_UPDATE_SECONDS;

/**
 * Created by wangkui on 2016/11/23.
 */

public class AllPlayerActivity extends Activity implements View.OnClickListener {

    private ViewVedio videoView;
    private TextView media_name;
    private ImageView im_battery;
    private TextView tv_time;
    private ImageView media_playing;
    private ImageView iv_media_preview;
    private ImageView iv_media_next;
    private ArrayList<VedioBean> objects;
    private int position;
    //当前播放的视频位置
    private int currtPosition;
    private BatteryReceiver batteryReceiver;
    private SeekBar seekbar;

    private MediaPlayer mps;

    /*
    *
    * 获取手势识别器
    * */

    private GestureDetector gestureDetector;
    private RelativeLayout media_control;
    private TextView tv_currt_time;
    private TextView tv_total;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //获取视频播放进度条秒显示

                case MESSAGE_UPDATE_SECONDS:
                        tv_currt_time.setText(ToHourUtils.getInstance().SecondXp(videoView.getCurrentPosition()));
                        seekbar.setProgress(videoView.getCurrentPosition());

                            tv_time.setText(getSystemTime());
                        removeMessages(MESSAGE_UPDATE_SECONDS);

                        sendEmptyMessageDelayed(MESSAGE_UPDATE_SECONDS, 1000);

                    break;
                case MessageWhatUtil.MESSAGE_HIND_MEDIA:
                    SetViewVisible();

                    break;
                case MessageWhatUtil.MESSAGE_HIND_VOICE:
                    la_voice.setVisibility(View.GONE);
                    removeMessages(MessageWhatUtil.MESSAGE_HIND_VOICE);
                    break;
                case MessageWhatUtil.MESSAGE_HIND_VOICE_PREGRESS:
                    //更新显示音量
                    break;
            }
        }
    };
    private ImageView iv_media_scale;
    private AudioManager audioManager;
    private int maxvoice;
    private int width;
    private int height;
    private LinearLayout la_voice;
    private TextView tv_voice;
    //系统亮度
    private int light_brigh;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_player);
        initView();
        initData();
    }

    /*
    * 初始化视图
    * */
    private void initView() {
        videoView = (ViewVedio) findViewById(R.id.videoView);
        videoView.setOnClickListener(this);
        media_name = (TextView) findViewById(R.id.media_name);
        media_name.setOnClickListener(this);
        im_battery = (ImageView) findViewById(R.id.im_battery);
        im_battery.setOnClickListener(this);
        //系统时间
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time.setOnClickListener(this);
        media_playing = (ImageView) findViewById(R.id.media_playing);
        media_playing.setOnClickListener(this);
        iv_media_preview = (ImageView) findViewById(R.id.iv_media_preview);
        iv_media_preview.setOnClickListener(this);
        iv_media_next = (ImageView) findViewById(R.id.iv_media_next);
        iv_media_next.setOnClickListener(this);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        iv_media_scale = (ImageView) findViewById(R.id.iv_media_scale);
        la_voice = (LinearLayout) findViewById(R.id.la_voice);
        tv_voice = (TextView) findViewById(R.id.tv_voice);
        iv_media_scale.setOnClickListener(this);

        //视频当前播放的时间
        tv_currt_time = (TextView) findViewById(R.id.tv_currt_time);
        //视频总时间
        tv_total = (TextView) findViewById(R.id.tv_total);

        media_control = (RelativeLayout) findViewById(R.id.media_control);
        
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        videoView.seekTo(progress);
                        tv_currt_time.setText(ToHourUtils.getInstance().SecondXp(progress));
                    }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    //初始化SeekBar
    private void initSeekBar() {
        seekbar.setMax(videoView.getDuration());
        seekbar.setProgress(0);
    }

    /*
    * 初始化歌曲名字和总时长
    * */
    private void initNameAndTime() {
        media_name.setText(objects.get(currtPosition).getVedioName());
        tv_total.setText(ToHourUtils.getInstance().SecondXp(objects.get(currtPosition).getVedioDurtion()));
    }

    /*
    * 获取系统亮度对比
    * */


    private void getSystemLight(){
        ContentResolver contentResolver = getContentResolver();
        try {
            light_brigh = Settings.System.getInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }
    /*
    * 获取数据播放视频
    * */
    private void initData() {
        getSystemBattery();

        getSystemVoice();

        tv_time.setText(getSystemTime());
        Intent intent  = getIntent();
        if(intent != null) {

            position = intent.getIntExtra("position",0);
            objects = (ArrayList<VedioBean>) intent.getSerializableExtra("objects");
            currtPosition = position;
            Uri uri = intent.getData();
            videoView.setVideoURI(uri);
            videoView.start();
            videoView.setOnCompletionListener(new MyOnCompletionListener());
            videoView.setOnErrorListener(new MyOnErrorLister());
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mps = mp;
                    initNameAndTime();

                    //初始化seekbar
                    initSeekBar();
                    //2 ：发送消息 更新seekbar

                    mHandler.sendEmptyMessage(MESSAGE_UPDATE_SECONDS);


                    mHandler.sendEmptyMessageDelayed(MessageWhatUtil.MESSAGE_HIND_MEDIA,3000);


//                    videoView.SetVideoViewSize(width,height);


                }
            });
            if(objects != null && objects.size() > 0) {
                media_name.setText(objects.get(position).getVedioName());
            }
        }

        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            //长按事件
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                PauseOrStart();
            }
            //双击事件
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }
            //单击事件
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                SetViewVisible();
                return super.onSingleTapConfirmed(e);
            }


            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                la_voice.setVisibility(View.VISIBLE);
                int currentVoice =  audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                getDisplay();
                tv_voice.setText((currentVoice/maxvoice * 100) + "%");
                float currentVoiceFilp =  (int)((e2.getY() - e1.getY())/height * maxvoice);
                if(e2.getY() - e1.getY() > 0){
                    if(currentVoice > 0) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) ((currentVoice - currentVoiceFilp)), AudioManager.FLAG_PLAY_SOUND);
                        tv_voice.setText((int)(Math.ceil((currentVoice - currentVoiceFilp)/maxvoice * 100)) + "%");
                    }
                }else{
                    currentVoiceFilp = Math.abs(currentVoiceFilp);
                    if(currentVoice < maxvoice) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) ((currentVoiceFilp + currentVoice)), AudioManager.FLAG_PLAY_SOUND);
                        tv_voice.setText((int)(Math.ceil((currentVoice + currentVoiceFilp)/maxvoice * 100)) + "%");
                    }
                }
                mHandler.sendEmptyMessageDelayed(MessageWhatUtil.MESSAGE_HIND_VOICE,3000);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            /*
                                    * 监听屏幕声音
                                    * */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });



        //控制视频播放暂停
    }
    /*
      * 获取系统声音
      * */
    private void getSystemVoice(){
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        //视频播放最大声音
        maxvoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }
  /*
  *  是否切换过
  * */
    private boolean isScal = false;
    /**
     *设置当前显示的视频大小
     */
    private int ViewWidth ;
    private int ViewHeight ;

    //获取屏幕大小
    private void getDisplay(){
        WindowManager manager = getWindowManager();
        Point mPoint = new Point();
        manager.getDefaultDisplay().getSize(mPoint);
        //屏幕宽高
        width = mPoint.x;
        height = mPoint.y;
    }
    /*
    * 设置屏幕大小切换
    * */
    private void steScace(MediaPlayer mp){

        getDisplay();

        if(!isScal){
            isScal = true;
            ViewWidth = width;
            ViewHeight = height;
        }else{
            isScal = false;
            ViewWidth = mp.getVideoWidth();
            ViewHeight = mp.getVideoHeight();
        }
        videoView.SetVideoViewSize(ViewWidth,ViewHeight);

    }

    private void SetViewVisible() {
        if(media_control.getVisibility() == View.GONE){
            media_control.setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessageDelayed(MessageWhatUtil.MESSAGE_HIND_MEDIA,3000);
        }else{
            media_control.setVisibility(View.GONE);
            mHandler.removeMessages(MessageWhatUtil.MESSAGE_HIND_MEDIA);
        }
    }
    //获取系统电池电量
    private void getSystemBattery(){
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryReceiver = new BatteryReceiver();
        registerReceiver(batteryReceiver,intentFilter);
    }


    class BatteryReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int current=intent.getExtras().getInt("level");//获得当前电量
            int total=intent.getExtras().getInt("scale");//获得总电量
            int percent=current/total;
            if( current <= 30){
                im_battery.setImageResource(R.drawable.low_battery);
            }else if( percent <= 60 && percent > 30){
                im_battery.setImageResource(R.drawable.half_battery);
            }else if( percent <= 90 && percent > 60){
                im_battery.setImageResource(R.drawable.high_battery);
            }else{
                im_battery.setImageResource(R.drawable.full_battery);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
    }

    /*
        * 获取系统时间
        * */
    private String getSystemTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        //获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }
    class MyOnErrorLister implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {

            PlayNext();
        }
    }


    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.media_playing:
                    PauseOrStart();
                    break;
                case R.id.iv_media_preview:
                    //播放上一个视频
                     if(currtPosition !=0 && objects != null){
                         currtPosition--;
                         videoView.setVideoURI(Uri.fromFile(new File(objects.get(currtPosition).getVedioPath())));
                         videoView.start();
                     }else{
                         Toast.makeText(this,"已经是第一条视频了",Toast.LENGTH_SHORT).show();
                     }
                    break;
                case R.id.iv_media_next:
                    PlayNext();
                    break;
                case R.id.iv_media_scale:
                    steScace(mps);
                    break;

            }
    }

    private void PlayNext() {
        //播放下一个视频
        if( objects != null && currtPosition < objects.size()-1){
            currtPosition++;
            videoView.setVideoURI(Uri.fromFile(new File(objects.get(currtPosition).getVedioPath())));
            videoView.start();
        }else{
            Toast.makeText(this,"已经是最后一条视频了",Toast.LENGTH_SHORT).show();
        }
    }

    private void PauseOrStart() {
        //播放按钮
        if(videoView.isPlaying()){
            videoView.pause();
            media_playing.setImageResource(R.drawable.media_stop);
        }else{
            videoView.start();
            media_playing.setImageResource(R.drawable.media_playing);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
