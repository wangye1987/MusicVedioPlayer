package com.wk.com.mobileplay.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class ToHourUtils {


    private StringBuilder mFormatBuilder;
    private Formatter     mFormatter;
    private static ToHourUtils mToHourUtils;
    /*
    * 修改成单例模式
    * */
    public static ToHourUtils getInstance(){
        if(mToHourUtils == null){
                synchronized (ToHourUtils.class) {
                    if(mToHourUtils == null) {
                        mToHourUtils = new ToHourUtils();
                    }
                }
            }
            return mToHourUtils;
        }

    public ToHourUtils() {
        // 转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    }


    /**
     * 把毫秒转换成：1:20:30这里形式
     * @param timeMs
     * @return
     */
    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;


        int minutes = (totalSeconds / 60) % 60;


        int hours = totalSeconds / 3600;


        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
    /*
       * 秒转化为分钟的形式
       * */
    public String SecondXp(float seco) {
        Date date = new Date((int) seco);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return sdf.format(date);
    }

}