package com.ace.musicplayer.entity;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class Group {
    private String artist;//歌手名
    private int count;//总数
    private String album;//专辑

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
