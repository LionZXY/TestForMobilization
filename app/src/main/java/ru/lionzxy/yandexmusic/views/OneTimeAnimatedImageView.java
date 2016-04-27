package ru.lionzxy.yandexmusic.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by LionZXY on 27.04.2016.
 * YandexMusic
 */
public class OneTimeAnimatedImageView extends AnimatedImageView{
    boolean anim = true;
    public OneTimeAnimatedImageView(Context context) {
        super(context);
    }

    public OneTimeAnimatedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageDrawable(Drawable d) {
        super.setImageDrawable(d, anim);
        anim = false;
    }
}
