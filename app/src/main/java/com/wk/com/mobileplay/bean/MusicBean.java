package com.wk.com.mobileplay.bean;

import java.io.Serializable;

/**
 * Created by wangkui on 2016/11/21.
 */

public class MusicBean implements Serializable {

    /*音乐名称*/
    private String MusicName;
    /*音乐大小*/
    private int MusicSize;
    /*音乐艺术家*/
    private String MusicArtise;
     /*音乐路径*/
    private String MusicPath;
     /*音乐时长*/
    private float MusicDurtion;

    public float getMusicDurtion() {
        return MusicDurtion;
    }

    public void setMusicDurtion(float musicDurtion) {
        MusicDurtion = musicDurtion;
    }

    public String getMusicName() {
        return MusicName;
    }

    public void setMusicName(String musicName) {
        MusicName = musicName;
    }

    public int getMusicSize() {
        return MusicSize;
    }

    public void setMusicSize(int musicSize) {
        MusicSize = musicSize;
    }

    public String getMusicArtise() {
        return MusicArtise;
    }

    public void setMusicArtise(String musicArtise) {
        MusicArtise = musicArtise;
    }

    public String getMusicPath() {
        return MusicPath;
    }

    public void setMusicPath(String musicPath) {
        MusicPath = musicPath;
    }
}
