package com.ace.musicplayer.list;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ace.musicplayer.adapter.ArtistAdapter;
import com.ace.musicplayer.util.Constant;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class ArtistList extends ListActivity implements AdapterView.OnItemClickListener {
    private ListView lv_artist;
    private ArtistAdapter artistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        lv_artist = this.getListView();
        artistAdapter = new ArtistAdapter(ArtistList.this);
        lv_artist.setAdapter(artistAdapter);
        lv_artist.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Constant.currentArr = Constant.ARTIST;
        Constant.groupid = position;

        Intent intent = new Intent(ArtistList.this, DetailArtistList.class);
        startActivity(intent);
    }
}
