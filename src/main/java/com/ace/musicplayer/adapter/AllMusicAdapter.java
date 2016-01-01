package com.ace.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.musicplayer.R;
import com.ace.musicplayer.data.MediaData;
import com.ace.musicplayer.util.TimeUtil;

/**
 * Created by Administrator on 2016/1/1 0001.
 */
public class AllMusicAdapter extends BaseAdapter {
    private Context context;

    public AllMusicAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return MediaData.allmusicList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AllMusic allMusic = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_music, null);
            allMusic = new AllMusic();
            allMusic.tv_musicTitle = (TextView) convertView.findViewById(R.id.tv_musicTitle);
            allMusic.tv_musicDuration = (TextView) convertView.findViewById(R.id.tv_musicDuration);
            allMusic.tv_artistalbumname = (TextView) convertView.findViewById(R.id.tv_artistalbumname);
            convertView.setTag(allMusic);
        } else {
            allMusic = (AllMusic) convertView.getTag();
        }

        int duration = MediaData.allmusicList.get(position).getDuration();
        String totalDuration = TimeUtil.toTime(duration);
        allMusic.tv_musicDuration.setText(totalDuration);
        allMusic.tv_musicTitle.setText(MediaData.allmusicList.get(position).getTitle());
        allMusic.tv_artistalbumname.setText(MediaData.allmusicList.get(position).getArtist() + " " + MediaData.allmusicList.get(position).getAlbum());

        return convertView;
    }

    private class AllMusic {
        TextView tv_musicTitle;
        TextView tv_musicDuration;
        TextView tv_artistalbumname;
    }
}
