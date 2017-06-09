package com.music.player.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

import com.music.player.bean.Song;
import com.music.player.musicUtils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

public class CollectDBHelper extends SQLiteOpenHelper {
    private static CollectDBHelper mDbHelper;
    public static final String DBNAME = "collect_db";
    public static final int VERSION = 1;

    public static CollectDBHelper getInstance(Context context) {
        if (mDbHelper == null) {
            synchronized (CollectDBHelper.class) {
                if (mDbHelper == null) {
                    mDbHelper = new CollectDBHelper(context);
                }
            }
        }
        return mDbHelper;
    }

    public CollectDBHelper(Context context) {
        super(context, CollectDBHelper.DBNAME, null, CollectDBHelper.VERSION);
    }

    public CollectDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table collect_db(singer text,song text,path text primary key,duration int,size long)";
        db.execSQL(sql);
    }

    //有升级再做操作
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertSong(Song song) {
        ContentValues values = new ContentValues();
        values.put("singer", song.singer);
        values.put("song", song.song);
        values.put("path", song.path);
        values.put("duration", song.duration);
        values.put("size", song.size);
        getWritableDatabase().insert(DBNAME, null, values);
    }

    public void deleteSong(Song song) {
        getWritableDatabase().delete(DBNAME, "path = ?", new String[]{song.path});
    }

    public boolean isExist(Song song) {
        Cursor cursor = getWritableDatabase().query(DBNAME, null, "path=?", new String[]{song.path}, null, null, null);
        boolean exist = false;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                exist = true;
            }
            cursor.close();
        }
        return exist;
    }

    public List<Song> getCollectSong(Context context) {
        List<Song> allSong = MusicUtils.getMusicData(context);
        List<Song> lists = new ArrayList<>();
        Cursor cursor = getWritableDatabase().query(DBNAME, null, null, null, null, null, null, null);
        if (cursor != null) {
            Song song;
            while (cursor.moveToNext()) {
                song = new Song();
                song.song = cursor.getString(cursor.getColumnIndexOrThrow("song"));
                song.singer = cursor.getString(cursor.getColumnIndexOrThrow("singer"));
                song.path = cursor.getString(cursor.getColumnIndexOrThrow("path"));
                song.duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                song.size = cursor.getLong(cursor.getColumnIndexOrThrow("size"));
                for (Song s : allSong) {
                    if (s.path.equals(song.path)) {
                        lists.add(song);
                        break;
                    }
                }
            }
            cursor.close();
        }
        return lists;
    }
}
