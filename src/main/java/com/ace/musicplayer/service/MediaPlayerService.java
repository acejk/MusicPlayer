package com.ace.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ace.musicplayer.data.MediaData;
import com.ace.musicplayer.play.PlayMusic;
import com.ace.musicplayer.util.Constant;
import com.ace.musicplayer.util.TimeUtil;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/1/1 0001.
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, Runnable {
    private MediaPlayer mediaPlayer;
    private ExecutorService executorService;
    private boolean fl = true;//默认播放
    private Intent intent;

    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        intent = new Intent(Constant.ACTION);
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        executorService = Executors.newSingleThreadExecutor();
        if(intent != null) {
            String flag = intent.getStringExtra(Constant.FLAG);
            int position = intent.getIntExtra(Constant.POSITION, 0);
            play(intent, position, flag);

        }
    }

    private void play(Intent intent, int position, String flag) {
        //下一首
        if(flag.equals(Constant.NEXT)) {
            next();
        //上一首
        } else if(flag.equals(Constant.PREVIOUS)) {
            previous();
        } else if(flag.equals(Constant.ALL)) {
            String musicData = MediaData.allmusicList.get(position).getData();
            startPlay(musicData);
        } else if(flag.equals(Constant.ARTIST)) {
            String musicData = MediaData.artistList.get(Constant.groupid).get(position).getData();
            startPlay(musicData);
        } else if(flag.equals(Constant.ALBUM)) {
            String musicData = MediaData.albumList.get(Constant.groupid).get(position).getData();
            startPlay(musicData);
        //进度条
        } else if(flag.equals(Constant.SEEKBAR)) {
            mediaPlayer.seekTo(position);
        //当前播放状态
        } else if(flag.equals(Constant.PLOPA)) {
            switch (Constant.currentState) {
                case Constant.PAUSE:
                    mediaPlayer.pause();
                    break;
                case Constant.PLAY:
                    mediaPlayer.start();
                    break;
            }
        }


    }

    /**
     * 开始播放
     * @param musicData
     */
    private void startPlay(String musicData) {
        try {
            reset();
            mediaPlayer.setDataSource(musicData);
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下一首
     */
    private void next() {
        if(Constant.currentArr.equals(Constant.ALL)) {
            if(Constant.currentIndex + 1 > MediaData.allmusicList.size() - 1) {
                Constant.currentIndex = 0;
            } else {
                Constant.currentIndex++;
            }

            setAllMusic();
            play();
        } else if(Constant.currentArr.equals(Constant.ARTIST)) {
            if(Constant.currentIndex + 1 > MediaData.artistList.get(Constant.groupid).size() - 1) {
                Constant.currentIndex = 0;
            } else {
                Constant.currentIndex++;
            }

            setArtist();
            play();
        } else if(Constant.currentArr.equals(Constant.ALBUM)) {
            if(Constant.currentIndex + 1 > MediaData.albumList.get(Constant.groupid).size() - 1) {
                Constant.currentIndex = 0;
            } else {
                Constant.currentIndex++;
            }

            setAlbum();
            play();
        }

        PlayMusic.searchLrc();
        sendBRDisplay();
    }

    /**
     * 上一首
     */

    private void previous() {
        if(Constant.currentArr.equals(Constant.ALL)) {
            if(Constant.currentIndex - 1 < 0) {
                Constant.currentIndex = MediaData.allmusicList.size() - 1;
            } else {
                Constant.currentIndex--;
            }

            setAllMusic();
            play();
        } else if(Constant.currentArr.equals(Constant.ARTIST)) {
            if(Constant.currentIndex - 1 < 0) {
                Constant.currentIndex = MediaData.artistList.get(Constant.groupid).size() - 1;
            } else {
                Constant.currentIndex--;
            }

            setArtist();
            play();
        } else if(Constant.currentArr.equals(Constant.ALBUM)) {
            if(Constant.currentIndex - 1 < 0) {
                Constant.currentIndex = MediaData.albumList.get(Constant.groupid).size() - 1;
            } else {
                Constant.currentIndex--;
            }

            setAlbum();
            play();
        }

        PlayMusic.searchLrc();
        sendBRDisplay();
    }

    private void setAllMusic() {
        Constant.title = MediaData.allmusicList.get(Constant.currentIndex).getTitle();
        Constant.artist = MediaData.allmusicList.get(Constant.currentIndex).getArtist();
        Constant.total = TimeUtil.toTime(MediaData.allmusicList.get(Constant.currentIndex).getDuration());
        Constant.duration = MediaData.allmusicList.get(Constant.currentIndex).getDuration();
        Constant._data = MediaData.allmusicList.get(Constant.currentIndex).getData();

    }

    private void setArtist() {
        Constant.title = MediaData.artistList.get(Constant.groupid).get(Constant.currentIndex).getTitle();
        Constant.artist = MediaData.artistList.get(Constant.groupid).get(Constant.currentIndex).getArtist();
        Constant.total = TimeUtil.toTime(MediaData.artistList.get(Constant.groupid).get(Constant.currentIndex).getDuration());
        Constant.duration = MediaData.artistList.get(Constant.groupid).get(Constant.currentIndex).getDuration();
        Constant._data = MediaData.artistList.get(Constant.groupid).get(Constant.currentIndex).getData();
    }

    private void setAlbum() {
        Constant.title = MediaData.albumList.get(Constant.groupid).get(Constant.currentIndex).getTitle();
        Constant.artist = MediaData.albumList.get(Constant.groupid).get(Constant.currentIndex).getArtist();
        Constant.total = TimeUtil.toTime(MediaData.albumList.get(Constant.groupid).get(Constant.currentIndex).getDuration());
        Constant.duration = MediaData.albumList.get(Constant.groupid).get(Constant.currentIndex).getDuration();
        Constant._data = MediaData.albumList.get(Constant.groupid).get(Constant.currentIndex).getData();
    }

    private void play() {
        try {
            if(Constant.currentArr.equals(Constant.ALL)) {
                reset();
                mediaPlayer.setDataSource(MediaData.allmusicList.get(Constant.currentIndex).getData());
                init();
            } else if(Constant.currentArr.equals(Constant.ARTIST)) {
                reset();
                mediaPlayer.setDataSource(MediaData.artistList.get(Constant.groupid).get(Constant.currentIndex).getData());
                init();
            } else if(Constant.currentArr.equals(Constant.ALBUM)) {
                reset();
                mediaPlayer.setDataSource(MediaData.albumList.get(Constant.groupid).get(Constant.currentIndex).getData());
                init();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        fl = false;
        mediaPlayer.stop();
        mediaPlayer.reset();

    }

    private void init() {
        try {
            mediaPlayer.prepare();
            mediaPlayer.start();
            fl = true;
            Constant.currentState = Constant.PLAY;
            executorService.execute(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendBRDisplay() {
        intent.putExtra(Constant.FLAG, Constant.UPDATADIAPALY);
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        switch (Constant.playModler) {
            case Constant.loop:
                loop();
                break;
            case Constant.order:
                next();
                break;
            case Constant.random:
                random();
                break;
        }

        PlayMusic.searchLrc();
        Intent intent = new Intent(Constant.UPDATACTION);
        sendBroadcast(intent);
        sendBRDisplay();
    }

    /**
     * 单曲循环
     */
    private void loop() {
        if(Constant.currentArr.equals(Constant.ALL)) {
            setAllMusic();
            play();
        } else if(Constant.currentArr.equals(Constant.ARTIST)) {
            setArtist();
            play();
        } else if(Constant.currentArr.equals(Constant.ALBUM)) {
            setAlbum();
            play();
        }
    }

    /**
     * 随机播放
     */

    private void random() {
        if(Constant.currentArr.equals(Constant.ALL)) {
            int size = MediaData.allmusicList.size();
            randomFactory(size);
            setAllMusic();
            play();
        } else if(Constant.currentArr.equals(Constant.ARTIST)) {
            int size = MediaData.artistList.get(Constant.groupid).size();
            randomFactory(size);
            setArtist();
            play();
        } else if(Constant.currentArr.equals(Constant.ALBUM)) {
            int size = MediaData.albumList.get(Constant.groupid).size();
            randomFactory(size);
            setAlbum();
            play();
        }

    }

    private void randomFactory(int size) {
        Random random = new Random();
        Constant.currentIndex = random.nextInt(size);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void run() {
        while(fl) {
            if(mediaPlayer.getCurrentPosition() < mediaPlayer.getDuration()) {
                intent.putExtra(Constant.FLAG, Constant.CURR);
                intent.putExtra(Constant.CURR, mediaPlayer.getCurrentPosition());
                Constant.seekBarCurr = mediaPlayer.getCurrentPosition();
                sendBroadcast(intent);
            } else {
                fl = false;
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
