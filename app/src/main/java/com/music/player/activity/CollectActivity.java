package com.music.player.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.music.player.APPContext;
import com.music.player.Config.Constants;
import com.music.player.R;
import com.music.player.adapter.MainMusicAdapter;
import com.music.player.base.BaseActivity;
import com.music.player.bean.Song;
import com.music.player.db.CollectDBHelper;

import java.util.List;

/**
 * Created by user on 2017/5/1.
 */

public class CollectActivity extends BaseActivity implements MainMusicAdapter.OnMusicItemListener {
    private ImageView mBackIv;
    private CheckBox mStateCb;
    private RecyclerView mRecycler;
    private MainMusicAdapter mAdapter;
    private List<Song> mSongs;
    private MediaPlayer mPlayer;
    private MainReceiver mMainReceiver;

    @Override
    public void setContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collect);
    }

    @Override
    public void findView() {
        mBackIv = (ImageView) findViewById(R.id.activity_collect_back);
        mStateCb = (CheckBox) findViewById(R.id.activity_collect_state);
        mRecycler = (RecyclerView) findViewById(R.id.activity_collect_recycler);
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        mPlayer = APPContext.getPlager();
        mSongs = CollectDBHelper.getInstance(mContext).getCollectSong(mContext);
        mAdapter = new MainMusicAdapter(mContext, mSongs, this);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));


        mMainReceiver = new MainReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CHANGE_MUSIC_UP_MUSIC_DATA);
        filter.addAction(Constants.CHANGE_PLAYED);
        registerReceiver(mMainReceiver, filter);
    }

    @Override
    public void initListener() {
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mStateCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendBroadcast(new Intent(Constants.CHANGE_COLLECT_LIST));
                if (isChecked) {
                    if (mSongs.size() > 0) {
                        Song song = APPContext.getInstance().getSong();
                        if (song != null) {
                            boolean isCollectList = false;
                            for (Song so : mSongs) {
                                if (so.path.equals(song.path)) {
                                    isCollectList = true;
                                    break;
                                }
                            }
                            if (isCollectList) {
                                mPlayer.start();
                            } else {
                                song = mSongs.get(0);
                                sendBroadcastSong(song);
                            }
                        } else {
                            song = mSongs.get(0);
                            sendBroadcastSong(song);
                        }
                    }
                } else {
                    mPlayer.pause();
                }
            }
        });
    }

    @Override
    public void onMusicClick(int position, Song song) {
        Song oldSong = APPContext.getInstance().getSong();
        sendBroadcast(new Intent(Constants.CHANGE_COLLECT_LIST));
        if (oldSong == null || !oldSong.path.equals(song.path)) {
            sendBroadcastSong(song);
        }
    }

    private void sendBroadcastSong(Song song) {
        sendBroadcast(new Intent(Constants.CHANGE_COLLECT_LIST));
        Intent intent = new Intent(Constants.CHANGE_MUSIC_SONG);
        intent.putExtra("song", song);
        sendBroadcast(intent);
        APPContext.getInstance().setSong(song);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCollectClick(int position, Song song, boolean isCheck) {
        if (!isCheck) {
            CollectDBHelper.getInstance(mContext).deleteSong(song);
            mAdapter.notifyItemRemoved(position);
            mSongs.clear();
            mSongs.addAll(CollectDBHelper.getInstance(mContext).getCollectSong(mContext));
        }

    }

    private class MainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.CHANGE_MUSIC_UP_MUSIC_DATA.equals(intent.getAction())) {
                mStateCb.setChecked(true);
                mAdapter.notifyDataSetChanged();
            } else if (Constants.CHANGE_PLAYED.equals(intent.getAction())) {
                mStateCb.setChecked(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMainReceiver);
        setResult(1, null);
    }
}
