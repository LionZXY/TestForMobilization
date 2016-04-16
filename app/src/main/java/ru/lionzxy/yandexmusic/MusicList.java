package ru.lionzxy.yandexmusic;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.lionzxy.yandexmusic.lists.author.AuthorObject;
import ru.lionzxy.yandexmusic.lists.author.AuthorRecyclerAdapter;

public class MusicList extends AppCompatActivity {

    private AuthorRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AuthorRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        //Test
        try {
            mAdapter.addItem(new AuthorObject("{\n" +
                    "\"id\":1080505,\n" +
                    "\"name\":\"Tove Lo\",\n" +
                    "\"genres\":[\n" +
                    "\"pop\",\n" +
                    "\"dance\",\n" +
                    "\"electronics\"\n" +
                    "],\n" +
                    "\"tracks\":81,\n" +
                    "\"albums\":22,\n" +
                    "\"link\":\"http://www.tove-lo.com/\",\n" +
                    "\"description\":\"шведская певица и автор песен. Она привлекла к себе внимание в 2013 году с выпуском сингла «Habits», но настоящего успеха добилась с ремиксом хип-хоп продюсера Hippie Sabotage на эту песню, который получил название «Stay High». 4 марта 2014 года вышел её дебютный мини-альбом Truth Serum, а 24 сентября этого же года дебютный студийный альбом Queen of the Clouds. Туве Лу является автором песен таких артистов, как Icona Pop, Girls Aloud и Шер Ллойд.\",\n" +
                    "\"cover\":{\n" +
                    "\"small\":\"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300\",\n" +
                    "\"big\":\"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/1000x1000\"\n" +
                    "}\n" +
                    "}"));
        } catch (Exception e) {
            Log.e("TEST", "TEST", e);
        }
        checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.INTERNET");
    }

    public void checkPermission(String... perm) {
        List<String> requestPerm = new ArrayList<String>();
        for (String p : perm)
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED && !ActivityCompat.shouldShowRequestPermissionRationale(this, p))
                requestPerm.add(p);
        if (requestPerm.size() > 0)
            ActivityCompat.requestPermissions(this, requestPerm.toArray(new String[requestPerm.size()]), 1);
    }
}
