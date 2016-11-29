package com.wk.com.mobileplay.bean;

import java.io.Serializable;

/**
 * Created by wangkui on 2016/11/22.
 */

public class VedioBean implements Serializable {
    /*视频名称*/
    private String VedioName;
    /*视频大小*/
    private int VedioSize;
    /*视频艺术家*/
    private String VedioArtise;
    /*视频路径*/
    private String VedioPath;
    /*视频时长*/
    private long VedioDurtion;

    public String getVedioName() {
        return VedioName;
    }

    public void setVedioName(String vedioName) {
        VedioName = vedioName;
    }

    public int getVedioSize() {
        return VedioSize;
    }

    public void setVedioSize(int vedioSize) {
        VedioSize = vedioSize;
    }

    public String getVedioArtise() {
        return VedioArtise;
    }

    public void setVedioArtise(String vedioArtise) {
        VedioArtise = vedioArtise;
    }

    public String getVedioPath() {
        return VedioPath;
    }

    public void setVedioPath(String vedioPath) {
        VedioPath = vedioPath;
    }

    public long getVedioDurtion() {
        return VedioDurtion;
    }

    public void setVedioDurtion(long vedioDurtion) {
        VedioDurtion = vedioDurtion;
    }
}
