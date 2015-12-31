package com.ace.musicplayer.data;

import com.ace.musicplayer.entity.AlbumMessage;
import com.ace.musicplayer.entity.ArtistMessage;
import com.ace.musicplayer.entity.Group;
import com.ace.musicplayer.entity.MusicMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class MediaData {
    public static List<Group> artistListG = new ArrayList<Group>();
    public static List<List<ArtistMessage>> artistList = new ArrayList<List<ArtistMessage>>();
    public static List<Group> albumListG = new ArrayList<Group>();
    public static List<List<AlbumMessage>> albumList = new ArrayList<List<AlbumMessage>>();
    public static List<MusicMessage> allmusicList = new ArrayList<MusicMessage>();
}
