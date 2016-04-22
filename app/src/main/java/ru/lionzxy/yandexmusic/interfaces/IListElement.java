package ru.lionzxy.yandexmusic.interfaces;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
public interface IListElement extends View.OnClickListener{

    void setImage(ImageView imageView, boolean isBig);

    void setItem(View view);
}
