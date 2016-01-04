package com.ace.musicplayer.play;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ace.musicplayer.R;
import com.ace.musicplayer.lrc.LrcView;
import com.ace.musicplayer.service.MediaPlayerService;
import com.ace.musicplayer.util.Constant;
import com.ace.musicplayer.util.TimeUtil;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class PlayMusic extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private TextView tv_musictitle, tv_artist, tv_currentTime, tv_total;
    private Button btn_play, btn_next, btn_previous, btn_playmode;
    private ImageButton ib_back;
    private LrcView lrcView;
    private SeekBar seekBar;

    private MyReceiver myReceiver;

    private Intent intent;

    private int pro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.playmusic);

        init();
    }

    private void init() {
        lrcView = (LrcView) findViewById(R.id.lrcView);
        lrcView.setTextSize();
        lrcView.setOffsetY(350);
        searchLrc();

        tv_artist = (TextView) findViewById(R.id.tv_artist);
        tv_musictitle = (TextView) findViewById(R.id.tv_musictitle);
        tv_currentTime = (TextView) findViewById(R.id.tv_currentTime);
        tv_total = (TextView) findViewById(R.id.tv_totalTime);

        btn_next = (Button) findViewById(R.id.btn_playMusic_next);
        btn_play = (Button) findViewById(R.id.btn_playMusic_play);
        btn_playmode = (Button) findViewById(R.id.btn_playMusic_playModle);
        btn_previous = (Button) findViewById(R.id.btn_playMusic_previous);

        btn_previous.setOnClickListener(this);
        btn_playmode.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        ib_back = (ImageButton) findViewById(R.id.btn_playMusic_back);
        ib_back.setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.sb_playmusic_sb);
        seekBar.setOnSeekBarChangeListener(this);

        checkPlayMode();
        setPlayMode();

        intent = new Intent(PlayMusic.this, MediaPlayerService.class);
    }

    /**
     * 设置播放模式
     */
    private void setPlayMode() {
        switch (Constant.playModler) {
            case Constant.loop:
                btn_playmode.setBackgroundResource(R.drawable.cycleone_selector);
                break;
            case Constant.order:
                btn_playmode.setBackgroundResource(R.drawable.cycleall_selector);
                break;
            case Constant.random:
                btn_playmode.setBackgroundResource(R.drawable.random_selector);
                break;
        }
    }

    /**
     * 选择播放模式
     */
    private void checkPlayMode() {
        switch (Constant.currentState) {
            case Constant.PLAY:
                btn_play.setBackgroundResource(R.drawable.pause_selector);
                break;
            case Constant.PAUSE:
                btn_play.setBackgroundResource(R.drawable.play_selector);
                break;
        }
    }

    /**
     * 查询歌词，如果有则显示出来
     */
    public static void searchLrc() {
        String lrc = Constant._data;
        lrc = lrc.substring(0, lrc.length() - 4).trim() + ".lrc".trim();
        LrcView.read(lrc);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_playMusic_play:
                play();
                break;
            case R.id.btn_playMusic_next:
                next();
                break;
            case R.id.btn_playMusic_previous:
                previous();
                break;
            case R.id.btn_playMusic_back:
                finish();
                break;
            case R.id.btn_playMusic_playModle:
                playModle();
                break;
        }
    }

    private void play() {
        switch (Constant.currentState) {
            case Constant.PLAY:
                Constant.currentState = Constant.PAUSE;
                break;
            case Constant.PAUSE:
                Constant.currentState = Constant.PLAY;
                break;
        }

        checkPlayMode();
        startService();
    }

    private void next() {
        intent.putExtra(Constant.FLAG, Constant.NEXT);
        startService(intent);
    }

    private void previous() {
        intent.putExtra(Constant.FLAG, Constant.PREVIOUS);
        startService(intent);
    }

    private void playModle() {
        switch (Constant.playModler) {
            case Constant.loop:
                btn_playmode.setBackgroundResource(R.drawable.cycleall_selector);
                Constant.playModler = Constant.order;
                break;
            case Constant.order:
                btn_playmode.setBackgroundResource(R.drawable.random_selector);
                Constant.playModler = Constant.random;
                break;
            case Constant.random:
                btn_playmode.setBackgroundResource(R.drawable.cycleone_selector);
                Constant.playModler = Constant.loop;
                break;
        }
    }

    private void startService() {
        intent.putExtra(Constant.FLAG, Constant.PLOPA);
        startService(intent);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            pro = progress;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        lrcView.setOffsetY(220 - lrcView.selectIndex(pro) * (lrcView.getSizeWord() + Constant.interval -1));
        intent.putExtra(Constant.POSITION, pro);
        intent.putExtra(Constant.FLAG, Constant.SEEKBAR);
        startService(intent);
    }

    private void updataDisplay() {
        seekBar.setMax(Constant.duration);
        seekBar.setProgress(Constant.seekBarCurr);

        tv_musictitle.setText(Constant.title);
        tv_artist.setText(Constant.artist);
        tv_total.setText(Constant.total);
        tv_currentTime.setText(TimeUtil.toTime(Constant.seekBarCurr));

    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int curr = intent.getIntExtra(Constant.CURR, 0);
            seekBar.setProgress(curr);
            tv_currentTime.setText(TimeUtil.toTime(curr));

            lrcView.setOffsetY(220 - lrcView.selectIndex(curr) * (lrcView.getSizeWord() + Constant.interval - 1));
            lrcView.setOffsetY(lrcView.getOffsetY() - lrcView.speedLrc());
            lrcView.selectIndex(curr);
            lrcView.invalidate(); // 更新视图

            if(intent.getStringExtra(Constant.FLAG).equals(Constant.UPDATADIAPALY)) {
                updataDisplay();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updataDisplay();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter(Constant.ACTION);
        this.registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.unregisterReceiver(myReceiver);
        myReceiver = null;
    }
}
