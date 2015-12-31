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
 * Created by Administrator on 2015/12/31 0031.
 */
public class ArtistAdapter extends BaseAdapter {
    private Context context;

    public ArtistAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return MediaData.artistListG.size();
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
        ArtistContent artistContent = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_artist, null);
            artistContent = new ArtistContent();
            artistContent.tv_artistname = (TextView) convertView.findViewById(R.id.tv_artistname);
            artistContent.tv_artistcount = (TextView) convertView.findViewById(R.id.tv_artistcount);
            convertView.setTag(artistContent);
        } else {
            artistContent = (ArtistContent) convertView.getTag();
        }

        artistContent.tv_artistname.setText(MediaData.artistListG.get(position).getArtist());
        artistContent.tv_artistcount.setText(MediaData.artistListG.get(position).getCount() + "首歌");

        return convertView;
    }

    private class ArtistContent {
        TextView tv_artistname;
        TextView tv_artistcount;
    }
}
