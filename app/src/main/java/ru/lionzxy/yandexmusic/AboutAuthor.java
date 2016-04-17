package ru.lionzxy.yandexmusic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.io.File;

import ru.lionzxy.yandexmusic.lists.author.AuthorObject;
import ru.lionzxy.yandexmusic.lists.genres.GenresObject;
import ru.lionzxy.yandexmusic.lists.genres.GenresRecyclerAdapter;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AboutAuthor extends AppCompatActivity {

    private GenresRecyclerAdapter genresAdapter;
    AuthorObject ao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        ao = intent.hasExtra("authorObject") ? (AuthorObject) intent.getSerializableExtra("authorObject") : AuthorObject.UNKNOWN;

        final ImageView image = (ImageView) findViewById(R.id.imageView);
        ao.setImageOnItemView(this, image, true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (ao.link == null || ao.link.length() == 0)
            fab.hide();
        else {
            fab.attachToScrollView((ObservableScrollView) findViewById(R.id.scrollViewList));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ao.link));
                    startActivity(browserIntent);
                }
            });
        }
        TextView descr = (TextView) findViewById(R.id.description);
        ((TextView) findViewById(R.id.head_author)).setText(ao.name);
        ((TextView) findViewById(R.id.trackscol)).setText(String.valueOf(ao.tracks));
        ((TextView) findViewById(R.id.albumscol)).setText(String.valueOf(ao.albums));
        descr.setText(ao.description);
        if (ao.genresObjects.size() > 0) {
            RecyclerView mRecyclerView;

            mRecyclerView = (RecyclerView) findViewById(R.id.genresList);

            mRecyclerView.setHasFixedSize(true);

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(layoutManager);

            genresAdapter = new GenresRecyclerAdapter(this);
            mRecyclerView.setAdapter(genresAdapter);
            for (GenresObject genresObject : ao.genresObjects)
                genresAdapter.addItem(genresObject);
        } else {
            findViewById(R.id.genresList).setVisibility(View.INVISIBLE);
        }
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

    public void openFullImage(View view) {
        File file = ao.bigImage.getAsFile();
        if (file == null) {
            Toast.makeText(this, getResources().getString(R.string.wait), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "image/*");
            startActivity(intent);
        }
    }
}
