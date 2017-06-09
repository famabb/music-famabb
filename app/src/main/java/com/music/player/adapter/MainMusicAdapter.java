package com.music.player.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.music.player.APPContext;
import com.music.player.R;
import com.music.player.bean.Song;
import com.music.player.db.CollectDBHelper;
import com.music.player.musicUtils.MusicUtils;

import java.util.List;

/**
 * Created by user on 2017/5/1.
 */

public class MainMusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnMusicItemListener mListener;
    private List<Song> mSongs;

    public MainMusicAdapter(Context context, List<Song> songs, OnMusicItemListener listener) {
        this.mContext = context;
        this.mSongs = songs;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_music, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Song song = mSongs.get(position);
        Song curSong = APPContext.getInstance().getSong();
        if (curSong != null && curSong.path.equals(song.path)) {
            ((MyViewHolder) holder).numTv.setBackgroundResource(R.drawable.icon_playing);
            ((MyViewHolder) holder).numTv.setText("");
        } else {
            ((MyViewHolder) holder).numTv.setBackgroundResource(0);
            ((MyViewHolder) holder).numTv.setText(String.valueOf(position+1));
        }
        ((MyViewHolder) holder).collectCb.setChecked(CollectDBHelper.getInstance(mContext).isExist(song));
        ((MyViewHolder) holder).nameTv.setText(song.song);
        ((MyViewHolder) holder).authorTv.setText(song.singer);
        ((MyViewHolder) holder).timeTv.setText(MusicUtils.formatTime(song.duration));
        ((MyViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMusicClick(position, song);
            }
        });
        ((MyViewHolder) holder).collectCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onCollectClick(position, song, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView numTv;
        private TextView nameTv;
        private TextView authorTv;
        private TextView timeTv;
        private CheckBox collectCb;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            numTv = (TextView) itemView.findViewById(R.id.item_music_num_tv);
            nameTv = (TextView) itemView.findViewById(R.id.item_music_center_name);
            timeTv = (TextView) itemView.findViewById(R.id.item_music_time);
            authorTv = (TextView) itemView.findViewById(R.id.item_music_center_author);
            collectCb = (CheckBox) itemView.findViewById(R.id.item_music_collect);
        }

    }

    public interface OnMusicItemListener {
        void onMusicClick(int position, Song song);

        void onCollectClick(int position, Song song, boolean isCheck);
    }
}
