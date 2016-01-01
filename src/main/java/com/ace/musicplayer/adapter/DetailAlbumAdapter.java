package com.ace.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.musicplayer.R;
import com.ace.musicplayer.data.MediaData;
import com.ace.musicplayer.entity.AlbumMessage;
import com.ace.musicplayer.util.Constant;
import com.ace.musicplayer.util.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/1/1 0001.
 */
public class DetailAlbumAdapter extends BaseAdapter {
    private Context context;
    private List<AlbumMessage> albumMessages;

    public DetailAlbumAdapter(Context context) {
        this.context = context;
        albumMessages = MediaData.albumList.get(Constant.groupid);
    }


    @Override
    public int getCount() {
        return albumMessages.size();
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
        DetailAlbum detailAlbum = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_music, null);
            detailAlbum = new DetailAlbum();
            detailAlbum.tv_musicTitle = (TextView) convertView.findViewById(R.id.tv_musicTitle);
            detailAlbum.tv_total = (TextView) convertView.findViewById(R.id.tv_musicDuration);
            convertView.setTag(detailAlbum);
        } else {
            detailAlbum = (DetailAlbum) convertView.getTag();
        }

        detailAlbum.tv_musicTitle.setText(albumMessages.get(position).getTitle());
        String total = TimeUtil.toTime(albumMessages.get(position).getDuration());
        detailAlbum.tv_total.setText(total);

        return convertView;
    }

    private class DetailAlbum {
        TextView tv_musicTitle;
        TextView tv_total;
    }
}
