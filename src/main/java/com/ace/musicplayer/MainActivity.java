package com.ace.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Window;

import com.ace.musicplayer.data.MediaData;
import com.ace.musicplayer.entity.AlbumMessage;
import com.ace.musicplayer.entity.AllMusicMessage;
import com.ace.musicplayer.entity.ArtistMessage;
import com.ace.musicplayer.entity.Group;
import com.ace.musicplayer.list.MainView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    String DATA = MediaStore.Audio.Media.DATA;
    String TITLE = MediaStore.Audio.Media.TITLE;
    String DURATION = MediaStore.Audio.Media.DURATION;
    String ARTIST = MediaStore.Audio.Media.ARTIST;
    String ALBUM = MediaStore.Audio.Media.ALBUM;
    String ARTIST_ID = MediaStore.Audio.Media.ARTIST_ID;
    String ALBUM_ID = MediaStore.Audio.Media.ALBUM_ID;
    String YEAR = MediaStore.Audio.Media.YEAR;

    Uri uri_data = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;//歌曲Uri
    Uri uri_artist = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;//歌手Uri
    Uri uri_albums = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;//专辑Uri

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                clear();
                scan();
                test();
            }
        }).start();
    }

    private void test() {
        long startTime = System.currentTimeMillis();
        initAllMusicList();
        initArtistList();
        initAlbumList();
        long endTime = System.currentTimeMillis();
        if(endTime - startTime > 3000) {
            Intent intent = new Intent(MainActivity.this, MainView.class);
            startActivity(intent);
        } else {
            try {
                Thread.sleep(3000 - (endTime - startTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(MainActivity.this, MainView.class);
            startActivity(intent);
        }
    }

    private void initAlbumList() {
        List<AlbumMessage> albumList = null;
        String artist = null;
        String album = null;
        int count = 0;

        Cursor albumCursor = getContentResolver().query(uri_albums, null, null, null, null);

        if(albumCursor != null && albumCursor.getCount() > 0) {
            while(albumCursor.moveToNext()) {
                albumList = new ArrayList<AlbumMessage>();
                int album_id = albumCursor.getInt(0);

                Cursor dataCursor = getContentResolver().query(uri_data, new String[] {DATA, TITLE, DURATION, ARTIST_ID, ALBUM_ID, YEAR}, ALBUM_ID + " = ?", new String[] {String.valueOf(album_id)}, null);
                if(dataCursor != null && dataCursor.getCount() > 0) {
                    count = dataCursor.getCount();
                    while(dataCursor.moveToNext()) {
                        AlbumMessage albumMessage = new AlbumMessage();
                        albumMessage.setData(dataCursor.getString(0));
                        albumMessage.setTitle(dataCursor.getString(1));
                        albumMessage.setDuration(dataCursor.getInt(2));
                        String year = String.valueOf(dataCursor.getInt(5));
                        if(year.equals("0")) {
                            year = "未知年份";
                        }

                        albumMessage.setYear(year);

                        album = albumCursor.getString(1);
                        if(album.equalsIgnoreCase("sdcard")) {
                            album = "未知专辑";
                        }

                        albumMessage.setAlbum(album);

                        int artist_id = dataCursor.getInt(3);

                        Cursor artistCursor = getContentResolver().query(uri_artist, null, MediaStore.Audio.Artists._ID + " = ?", new String[] {String.valueOf(artist_id)}, null);
                        if(artistCursor != null && artistCursor.getCount() > 0) {
                            artistCursor.moveToFirst();
                            artist = artistCursor.getString(1);
                            if(artist.equalsIgnoreCase("<unknown>")) {
                                artist = "未知歌手";
                            }

                            albumMessage.setArtist(artist);


                        }

                        albumList.add(albumMessage);
                        artistCursor.close();
                    }

                    Group group = new Group();
                    group.setAlbum(album);
                    group.setArtist(artist);
                    group.setCount(count);
                    MediaData.albumListG.add(group);
                    MediaData.albumList.add(albumList);

                }

                dataCursor.close();

            }
        }

        albumCursor.close();
    }

    private void initArtistList() {
        List<ArtistMessage> artistList = null;
        String artist = null;
        Cursor artistCursor = getContentResolver().query(uri_artist, null, null, null, null);
        int count = 0;//记录歌曲个数
        if(artistCursor != null && artistCursor.getCount() > 0) {
            while(artistCursor.moveToNext()) {
                artistList = new ArrayList<ArtistMessage>();
                int artist_id = artistCursor.getInt(0);

                Cursor dataCursor = getContentResolver().query(uri_data, new String[]{DATA, TITLE, DURATION, ARTIST_ID, ALBUM_ID, YEAR}, ARTIST_ID + " = ?", new String[]{String.valueOf(artist_id)}, null);
                if(dataCursor != null && dataCursor.getCount() > 0) {
                    while(dataCursor.moveToNext()) {
                        count = dataCursor.getCount();
                        ArtistMessage artistMessage = new ArtistMessage();
                        artistMessage.setData(dataCursor.getString(0));
                        artistMessage.setTitle(dataCursor.getString(1));
                        artistMessage.setDuration(dataCursor.getInt(2));
                        String year = String.valueOf(dataCursor.getInt(5));

                        if(year.equals("0")) {
                            year = "未知年份";
                        }

                        artistMessage.setYear(year);

                        artistMessage.setCount(count);
                        artist = artistCursor.getString(1);

                        if(artist.equals("<unknown>")) {
                            artist = "未知歌手";
                        }

                        artistMessage.setArtist(artist);

                        int album_id = dataCursor.getInt(4);

                        Cursor albumCursor = getContentResolver().query(uri_albums, null, MediaStore.Audio.Albums._ID + " = ?", new String[] {String.valueOf(album_id)}, null);
                        if(albumCursor != null && albumCursor.getCount() > 0) {
                            albumCursor.moveToFirst();
                            String album = albumCursor.getString(1);
                            if(album.equalsIgnoreCase("sdcard")) {
                                album = "未知专辑";
                            }

                            artistMessage.setAlbum(album);

                        }

                        albumCursor.close();
                        artistList.add(artistMessage);

                    }
                }

                Group group = new Group();
                group.setArtist(artist);
                group.setCount(count);
                MediaData.artistListG.add(group);
                MediaData.artistList.add(artistList);
                dataCursor.close();
            }
        }

        artistCursor.close();


    }

    private void initAllMusicList() {
        Cursor dataCursor = getContentResolver().query(uri_data, new String[] {DATA, TITLE, DURATION, ARTIST, ALBUM, YEAR}, null, null, null);
        if(dataCursor != null && dataCursor.getCount() > 0) {
            while(dataCursor.moveToNext()) {
                AllMusicMessage allMusicMessage = new AllMusicMessage();
                allMusicMessage.setData(dataCursor.getString(0));
                allMusicMessage.setTitle(dataCursor.getString(1));
                allMusicMessage.setDuration(dataCursor.getInt(2));
                allMusicMessage.setArtist(dataCursor.getString(3));
                allMusicMessage.setAlbum(dataCursor.getString(4));
                String year = String.valueOf(dataCursor.getInt(5));
                if(year.equals("0")) {
                    year = "未知年份";
                }

                allMusicMessage.setYear(year);
                MediaData.allmusicList.add(allMusicMessage);
            }
        }

        dataCursor.close();
    }

    private void scan() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED,  Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath()));
        sendBroadcast(intent);
    }

    private void clear() {
        MediaData.artistListG.clear();
        MediaData.artistList.clear();
        MediaData.albumListG.clear();
        MediaData.albumList.clear();
        MediaData.allmusicList.clear();
    }


}
