package com.ace.musicplayer.util;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class Constant {
    public static final String FLAG = "flag";//播放标识
    public static final String POSITION = "position";//当前位置
    public static final String GROUP = "group";//哪一组
    public static final String ALL = "all";//所有音乐
    public static final String ALBUM = "album";//专辑
    public static final String ARTIST = "artist";//歌手

    public static final String UPDATADIAPALY = "updataDiaplay";
    public static final String SEEKBAR = "seekbar";
    public static final String PLOPA = "plopa";
    public static final String NEXT = "next";//下一首
    public static final String PREVIOUS = "previous";//上一首

    public static final int CURRENT_TIME = 1;
    public static final int PAUSE = 10;//暂停
    public static final int PLAY = 20;//播放
    public static final int IDLE = 30;
    public static int currentState = IDLE;//当前状态

    public static String currentArr;
    public static int groupid = 0;//哪一组
    public static int currentIndex;//当前索引


    public static String title;//歌曲名
    public static String artist;//歌手名
    public static String total;//总数
    public static int duration;//歌曲时长
    public static String _data;//歌曲路径

    public static int playModler = Constant.loop;//播放模式，默认是单曲循环
    public static final int loop = 40;//单曲循环
    public static final int order = 50;//顺序播放
    public static final int random = 60;//随机播放

    public static final String ACTION = "com.mrzhu.mediaplay.receiver";//广播接收器的action
    public static final String CURR = "curr";

    public static final String UPDATACTION = "com.mrzhu.mediaplay.updata";//更新的action
    public static int seekBarCurr;//进度条

    public static int interval = 25;
    public static int sizeWord = 25; //显示歌词文字的大小值
}
