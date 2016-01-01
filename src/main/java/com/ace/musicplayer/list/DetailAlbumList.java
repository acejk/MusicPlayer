package com.ace.musicplayer.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ace.musicplayer.R;
import com.ace.musicplayer.adapter.DetailAlbumAdapter;
import com.ace.musicplayer.data.MediaData;
import com.ace.musicplayer.service.MediaPlayerService;
import com.ace.musicplayer.util.Constant;
import com.ace.musicplayer.util.TimeUtil;

/**
 * Created by Administrator on 2016/1/1 0001.
 */
public class DetailAlbumList extends Activity implements AdapterView.OnItemClickListener {
    private ListView lv_music;
    private TextView tv_albumartistname;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailartist);

        lv_music = (ListView) findViewById(R.id.lv_music);
        DetailAlbumAdapter detailAlbumAdapter = new DetailAlbumAdapter(this);
        lv_music.setAdapter(detailAlbumAdapter);
        lv_music.setOnItemClickListener(this);

        tv_albumartistname = (TextView) findViewById(R.id.tv_artistname);

        String albumname = MediaData.albumListG.get(Constant.groupid).getAlbum();
        String artistname = MediaData.albumListG.get(Constant.groupid).getArtist();

        tv_albumartistname.setText(albumname + "--" + artistname);

        intent = new Intent(DetailAlbumList.this, MediaPlayerService.class);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Constant.currentIndex = position;

        Constant.title = MediaData.artistList.get(Constant.groupid).get(position).getTitle();;
        Constant.artist = MediaData.artistList.get(Constant.groupid).get(position).getArtist();;
        Constant.duration = MediaData.artistList.get(Constant.groupid).get(position).getDuration();
        Constant.total = TimeUtil.toTime(MediaData.artistList.get(Constant.groupid).get(position).getDuration());
        Constant._data = MediaData.artistList.get(Constant.groupid).get(position).getData();

        intent.putExtra(Constant.FLAG, Constant.ALBUM);
        intent.putExtra(Constant.POSITION, position);
        intent.putExtra(Constant.GROUP, Constant.groupid);
        startService(intent);

    }

    public void back(View view) {
        intent = null;
        finish();
    }
}
