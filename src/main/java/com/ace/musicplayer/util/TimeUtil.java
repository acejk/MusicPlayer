package com.ace.musicplayer.util;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class TimeUtil {
    public static String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        return String.format("%02d:%02d", minute, second);
    }
}
