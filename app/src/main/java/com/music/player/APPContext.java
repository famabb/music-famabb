package com.music.player;

import android.app.Application;
import android.content.Intent;
import android.media.MediaPlayer;

import com.music.player.bean.Song;
import com.music.player.musicUtils.MusicUtils;
import com.music.player.server.MusicServer;

import java.util.List;

/**
 * Created by user on 2017/5/1.
 */

public class APPContext extends Application {
    public static APPContext instance;
    public static MediaPlayer mPlayer;
    private List<Song> mSongs;
    public Song mSong;


    public static APPContext getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSongs = MusicUtils.getMusicData(this);
        mPlayer = new MediaPlayer();
        instance = this;
        startService(new Intent(this, MusicServer.class));
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void setSong(Song song) {
        this.mSong = song;
    }

    public Song getSong() {
        return mSong;
    }

    public static MediaPlayer getPlager() {
        return mPlayer;
    }
}
