package com.ace.musicplayer.list;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ace.musicplayer.adapter.AllMusicAdapter;
import com.ace.musicplayer.data.MediaData;
import com.ace.musicplayer.service.MediaPlayerService;
import com.ace.musicplayer.util.Constant;
import com.ace.musicplayer.util.TimeUtil;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class AllMusicList extends ListActivity implements AdapterView.OnItemClickListener {
    private ListView lv_music;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lv_music = getListView();
        AllMusicAdapter allMusicAdapter = new AllMusicAdapter(this);
        lv_music.setAdapter(allMusicAdapter);
        lv_music.setOnItemClickListener(this);

        intent = new Intent(AllMusicList.this, MediaPlayerService.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Constant.currentArr = Constant.ALL;
        Constant.currentIndex = position;

        Constant.title = MediaData.allmusicList.get(position).getTitle();
        Constant.artist = MediaData.allmusicList.get(position).getArtist();
        Constant.duration = MediaData.allmusicList.get(position).getDuration();
        Constant.total = TimeUtil.toTime(MediaData.allmusicList.get(position).getDuration());
        Constant._data = MediaData.allmusicList.get(position).getData();

        MainView.tv_music.setText(Constant.artist + "--" + Constant.title);

        intent.putExtra(Constant.FLAG, Constant.ALL);
        intent.putExtra(Constant.POSITION, position);
        startService(intent);
    }
}
