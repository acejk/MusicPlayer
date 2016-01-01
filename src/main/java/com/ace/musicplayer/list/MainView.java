package com.ace.musicplayer.list;

import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.musicplayer.R;
import com.ace.musicplayer.play.PlayMusic;
import com.ace.musicplayer.util.Constant;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class MainView extends ActivityGroup {
    private MyBroadCastReceiver receiver;
    public static TextView tv_music;
    private LinearLayout body;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);
        init();
    }

    private void init() {
        body = (LinearLayout) findViewById(R.id.body);
        goArtist();
        tv_music = (TextView) findViewById(R.id.tv_music);
        updataDisplay();
    }

    private void goArtist() {
        updataBody(ArtistList.class);
    }

    public void artist(View view) {
        goArtist();
    }

    public void album(View view) {
        updataBody(AlbumList.class);
    }

    public void allmusic(View view) {
        updataBody(AllMusicList.class);
    }

    private void updataBody(Class<?> cl) {
        body.removeAllViews();
        Intent intent = new Intent(getApplicationContext(), cl);
        body.addView(getLocalActivityManager().startActivity("", intent).getDecorView());
    }

    private void updataDisplay() {
        tv_music.setText(Constant.artist + "--" + Constant.title);
    }

    public void goPlayMusic(View view) {
        startActivity(new Intent(MainView.this, PlayMusic.class));
    }

    private class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updataDisplay();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updataDisplay();
        receiver = new MyBroadCastReceiver();
        IntentFilter filter = new IntentFilter(Constant.UPDATACTION);
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(receiver);
        receiver = null;
    }


}
