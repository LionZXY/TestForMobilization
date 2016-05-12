package ru.lionzxy.yandexmusic.model;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import ru.lionzxy.yandexmusic.LoadingActivity;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.interfaces.IListElement;

/**
 * Created by LionZXY on 09.05.2016.
 */
public class GenresCheck implements IListElement {
    GenresObject genresObject;

    public GenresCheck(GenresObject genresObject) {
        this.genresObject = genresObject;
    }


    @Override
    public void setItem(ImageView imageView, View view, Activity activity) {
        ((TextView) view.findViewById(R.id.textView)).setText(genresObject.name);
        //((CheckBox) view.findViewById(R.id.checkBox)).listene
    }
}
