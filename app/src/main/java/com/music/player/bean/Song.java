package com.music.player.bean;

import java.io.Serializable;

/**
 * Created by user on 2016/6/24. * 放置音乐
 */
public class Song implements Serializable{
    /**
     * 歌手
     */
    public String singer;
    /**
     * 歌曲名
     */
    public String song;
    /**
     * 歌曲的地址
     */
    public String path;
    /**
     * 歌曲长度
     */
    public int duration;
    /**
     * 歌曲的大小
     */
    public long size;
}