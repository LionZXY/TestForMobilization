package ru.lionzxy.yandexmusic;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

import ru.lionzxy.yandexmusic.collections.recyclerviews.LockableRecyclerView;
import ru.lionzxy.yandexmusic.collections.recyclerviews.RecyclerViewAdapter;
import ru.lionzxy.yandexmusic.exceptions.ContextDialogException;
import ru.lionzxy.yandexmusic.interfaces.IListElement;

public class MusicList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);


        //Set up recyclerlist
        LockableRecyclerView mRecyclerView;
        try {
            mRecyclerView = (LockableRecyclerView) findViewById(R.id.recyclerview);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(new RecyclerViewAdapter(new ArrayList<IListElement>(LoadingActivity.authorObjects),R.layout.authorcard));

            checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.INTERNET");
        } catch (Exception e) {
            new ContextDialogException(this, e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
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
