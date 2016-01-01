package com.ace.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.musicplayer.R;
import com.ace.musicplayer.data.MediaData;
import com.ace.musicplayer.entity.ArtistMessage;
import com.ace.musicplayer.util.Constant;
import com.ace.musicplayer.util.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/1/1 0001.
 */
public class DetailArtistAdapter extends BaseAdapter {
    private Context context;
    private List<ArtistMessage> artistMessages;

    public DetailArtistAdapter(Context context) {
        this.context = context;
        artistMessages = MediaData.artistList.get(Constant.groupid);
    }

    @Override
    public int getCount() {
        return artistMessages.size();
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
        DetailArtist detailArtist = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_music, null);
            detailArtist = new DetailArtist();
            detailArtist.tv_musicTitle = (TextView) convertView.findViewById(R.id.tv_musicTitle);
            detailArtist.tv_total = (TextView) convertView.findViewById(R.id.tv_musicDuration);
            detailArtist.tv_album = (TextView) convertView.findViewById(R.id.tv_artistalbumname);
            convertView.setTag(detailArtist);
        } else {
            detailArtist = (DetailArtist) convertView.getTag();
        }

        detailArtist.tv_musicTitle.setText(artistMessages.get(position).getTitle());
        detailArtist.tv_album.setText(artistMessages.get(position).getAlbum());
        String total = TimeUtil.toTime(artistMessages.get(position).getDuration());
        detailArtist.tv_total.setText(total);

        return convertView;
    }

    private class DetailArtist {
        TextView tv_musicTitle;
        TextView tv_total;
        TextView tv_album;
    }
}
