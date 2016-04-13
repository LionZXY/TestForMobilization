package ru.lionzxy.yandexmusic;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import ru.lionzxy.yandexmusic.elements.AuthorObject;
import ru.lionzxy.yandexmusic.helper.TextHelper;
import ru.lionzxy.yandexmusic.helper.PixelHelper;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AboutAuthor extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);
        Intent intent = getIntent();
        final AuthorObject ao = intent.hasExtra("authorObject") ? (AuthorObject) intent.getSerializableExtra("authorObject") : MusicList.unknowObject;
        final ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageBitmap(ao.image);
        final TextView descr = (TextView) findViewById(R.id.description);


        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                String text = ao.description;

                int leftMargin = image.getMeasuredWidth() - (int) PixelHelper.pixelFromDP(getResources(), 8);
                SpannableString ss = new SpannableString(text);
                ss.setSpan(new TextHelper.LeadingMarginSpan2(7, leftMargin), 0, ss.length(), 0);
                descr.setText(ss);
                return true;
            }
        });

    }
}
