package ru.lionzxy.yandexmusic;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.io.File;

import ru.lionzxy.yandexmusic.collections.recyclerviews.RecyclerViewAdapter;
import ru.lionzxy.yandexmusic.collections.recyclerviews.elements.AuthorObject;
import ru.lionzxy.yandexmusic.collections.recyclerviews.elements.GenresObject;
import ru.lionzxy.yandexmusic.exceptions.ContextDialogException;
import ru.lionzxy.yandexmusic.views.AnimatedImageView;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AboutAuthor extends AppCompatActivity {


    AuthorObject ao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ao = intent.hasExtra("authorObject") ? (AuthorObject) intent.getSerializableExtra("authorObject") : AuthorObject.UNKNOWN;

        setContentView(R.layout.activity_about_author);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ao.color));
            getWindow().setStatusBarColor(ao.color);
        }


        //Set content
        try {
            final AnimatedImageView image = (AnimatedImageView) findViewById(R.id.imageView);
            ao.setImage(image, true);
            TextView descr = (TextView) findViewById(R.id.description);
            ((TextView) findViewById(R.id.head_author)).setText(ao.name);
            ((TextView) findViewById(R.id.trackscol)).setText(String.valueOf(ao.tracks));
            ((TextView) findViewById(R.id.albumscol)).setText(String.valueOf(ao.albums));
            descr.setText(ao.description);
            findViewById(R.id.additionalInfo).startAnimation(AnimationUtils.loadAnimation(this, R.anim.alphavisible));
        } catch (Exception e) {
            new ContextDialogException(AboutAuthor.this, e);
        }

        try {
            if (ao.genresObjects.size() > 0) {
                View list = getLayoutInflater().inflate(R.layout.genreslist, null, false);
                ((LinearLayout) findViewById(R.id.linearLayoutCards)).addView(list);
                RecyclerView mRecyclerView;

                mRecyclerView = (RecyclerView) list.findViewById(R.id.genresList);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(R.layout.genrecard);
                mRecyclerView.setAdapter(recyclerViewAdapter);

                for (GenresObject genresObject : ao.genresObjects)
                    recyclerViewAdapter.addItem(genresObject);
            }
        } catch (Exception e) {
            new ContextDialogException(AboutAuthor.this, e);
        }

        try {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            if (ao.link == null || ao.link.length() == 0)
                fab.hide();
            else {
                fab.setVisibility(View.VISIBLE);
                fab.attachToScrollView((ObservableScrollView) findViewById(R.id.scrollViewList));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ao.link));
                        startActivity(browserIntent);
                    }
                });
            }
        } catch (Exception e) {
            new ContextDialogException(AboutAuthor.this, e);
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
        File file = ao.getFile(true);
        if (file == null || !file.exists()) {
            Toast.makeText(this, getResources().getString(R.string.wait), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "image/*");
            startActivity(intent);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ao = (AuthorObject) savedInstanceState.getSerializable("authorObject");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("authorObject", ao);

        super.onSaveInstanceState(outState);
    }
}
