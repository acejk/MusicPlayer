package com.ace.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.musicplayer.R;
import com.ace.musicplayer.data.MediaData;

/**
 * Created by Administrator on 2016/1/1 0001.
 */
public class AlbumAdapter extends BaseAdapter {
    private Context context;

    public AlbumAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return MediaData.albumListG.size();
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
        AlbumContent albumContent = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_artist, null);
            albumContent = new AlbumContent();
            albumContent.tv_albumname = (TextView) convertView.findViewById(R.id.tv_artistname);
            albumContent.tv_albumcount = (TextView) convertView.findViewById(R.id.tv_artistcount);
            convertView.setTag(albumContent);
        } else {
            albumContent = (AlbumContent) convertView.getTag();
        }

        albumContent.tv_albumname.setText(MediaData.albumListG.get(position).getAlbum() + "--" + MediaData.albumListG.get(position).getArtist());
        albumContent.tv_albumcount.setText(MediaData.albumListG.get(position).getCount() + "首歌");

        return convertView;
    }

    private class AlbumContent {
        TextView tv_albumname;
        TextView tv_albumcount;
    }
}
