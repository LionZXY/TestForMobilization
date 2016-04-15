package ru.lionzxy.yandexmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import ru.lionzxy.yandexmusic.lists.author.AuthorObject;
import ru.lionzxy.yandexmusic.lists.author.AuthorRecyclerAdapter;
import ru.lionzxy.yandexmusic.lists.genres.GenresObject;
import ru.lionzxy.yandexmusic.lists.genres.GenresRecyclerAdapter;

import android.view.*;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AboutAuthor extends AppCompatActivity {

    private GenresRecyclerAdapter genresAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);
        Intent intent = getIntent();
        final AuthorObject ao = intent.hasExtra("authorObject") ? (AuthorObject) intent.getSerializableExtra("authorObject") : MusicList.unknowObject;
        final ImageView image = (ImageView) findViewById(R.id.imageView);
        ao.setImageOnItemView(this,image, getResources(), true);
        final TextView descr = (TextView) findViewById(R.id.description);
        ((TextView) findViewById(R.id.head_author)).setText(ao.name);
        descr.setText(ao.description);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RecyclerView mRecyclerView;

        mRecyclerView = (RecyclerView) findViewById(R.id.genresList);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        genresAdapter = new GenresRecyclerAdapter(this);
        mRecyclerView.setAdapter(genresAdapter);

        genresAdapter.addItem(GenresObject.UNKNOWN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
