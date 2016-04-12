package ru.lionzxy.yandexmusic;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import ru.lionzxy.yandexmusic.elements.AuthorObject;
import ru.lionzxy.yandexmusic.helper.FlowText;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AboutAuthor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);
        AuthorObject ao = new AuthorObject("Ne-Yo", "обладатель трёх премии Грэмми, американский певец, автор песен, продюсер, актёр, филантроп. В 2009 году журнал Billboard поставил Ни-Йо на 57 место в рейтинге «Артисты десятилетия».", BitmapFactory.decodeResource(getResources(), R.drawable.notfoundmusic));
        ImageView image = (ImageView) findViewById(R.id.imageView);
        TextView descr = (TextView) findViewById(R.id.description);

        FlowText.tryFlowText((String) descr.getText(), image, descr, getWindowManager().getDefaultDisplay());
    }
}
