package com.ace.musicplayer.list;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ace.musicplayer.adapter.AlbumAdapter;
import com.ace.musicplayer.util.Constant;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class AlbumList extends ListActivity implements AdapterView.OnItemClickListener {
    private ListView lv_album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lv_album = getListView();
        AlbumAdapter albumAdapter = new AlbumAdapter(this);
        lv_album.setAdapter(albumAdapter);
        lv_album.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Constant.currentArr = Constant.ALBUM;
        Constant.groupid = position;

        Intent intent = new Intent(AlbumList.this, DetailAlbumList.class);
        startActivity(intent);
    }
}
