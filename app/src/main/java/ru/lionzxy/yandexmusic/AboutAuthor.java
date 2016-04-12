package ru.lionzxy.yandexmusic;

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
        final AuthorObject ao = new AuthorObject("Ne-Yo", "Шведская певица и автор песен. Она привлекла к себе внимание в 2013 году с выпуском сингла «Habits», но настоящего успеха добилась с ремиксом хип-хоп продюсера Hippie Sabotage на эту песню, который получил название «Stay High». 4 марта 2014 года вышел её дебютный мини-альбом Truth Serum, а 24 сентября этого же года дебютный студийный альбом Queen of the Clouds. Туве Лу является автором песен таких артистов, как Icona Pop, Girls Aloud и Шер Ллойд.", BitmapFactory.decodeResource(getResources(), R.drawable.notfoundmusic));
        final ImageView image = (ImageView) findViewById(R.id.imageView);
        final TextView descr = (TextView) findViewById(R.id.description);


        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                String text = ao.description;

                int leftMargin = image.getMeasuredWidth() - (int) PixelHelper.pixelFromDP(getResources(), 8);
                SpannableString ss = new SpannableString(text);
                ss.setSpan(new TextHelper.LeadingMarginSpan2(8, leftMargin), 0, ss.length(), 0);
                descr.setText(ss);
                return true;
            }
        });

    }
}
