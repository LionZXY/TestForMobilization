package ru.lionzxy.yandexmusic.interfaces;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
public interface IListElement{

    void setItem(ImageView imageView,View view, Activity activity);
}
