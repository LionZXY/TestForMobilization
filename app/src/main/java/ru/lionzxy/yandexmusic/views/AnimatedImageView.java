package ru.lionzxy.yandexmusic.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import ru.lionzxy.yandexmusic.R;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
public class AnimatedImageView extends ImageView {
    public AnimatedImageView(Context context) {
        super(context);
    }

    public AnimatedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setImageDrawable(Drawable d) {
        setImageDrawable(d, true);
    }

    public void setImageDrawable(Drawable d, boolean animation) {
        if (animation) {
            float startAlpha = getAlpha();
            setAlpha(0.1F);

            super.setImageDrawable(d);

            startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alphavisible));
            setAlpha(startAlpha);
        } else super.setImageDrawable(d);
    }

    public void setImageBitmap(Bitmap bitmap, boolean animation) {
        if (animation) {
            this.setImageBitmap(bitmap);
        } else super.setImageBitmap(bitmap);
    }


}
