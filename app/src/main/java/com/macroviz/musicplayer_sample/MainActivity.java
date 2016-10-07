package com.macroviz.musicplayer_sample;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView musicList;
    private SeekBar seekBar;
    private Button btnPlay,btnStop,btnPrev,btnNext;
    private TextView txtTotal;

    private LinkedList<Song> songList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getMusics();
    }
    private void initView(){
        musicList = (ListView) findViewById(R.id.musicList);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
    }
    private void getMusics(){
        songList = new LinkedList<Song>();
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null,null,null);
        if (cursor == null) {
            Log.d("=======>", "查詢錯誤");
        } else if (!cursor.moveToFirst()) {
            Log.d("=======>", "沒有媒體檔");
        } else {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            do{
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thisAlbum = cursor.getString(albumColumn);
                Log.d("=======>", "id: " + thisId + ", title: " + thisTitle + ",album:" + thisAlbum);
                songList.add(new Song(thisId,thisTitle,thisAlbum));
            }while(cursor.moveToNext());
            SongAdapter adapter = new SongAdapter(MainActivity.this, songList);
            musicList.setAdapter(adapter);
            txtTotal.setText("共有 " + songList.size() + " 首歌曲");

        }
    }
}
