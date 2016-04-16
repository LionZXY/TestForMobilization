package ru.lionzxy.yandexmusic.animations;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Created by LionZXY on 16.04.2016.
 * YandexMusic
 */
public class VisibleImageAnimation implements Animation.AnimationListener {
    private ImageView iv;
    private Bitmap bitmap;
    private Animation lastanimation;

    public VisibleImageAnimation(ImageView imageView, Bitmap bitmap, Animation lastanimation) {
        this.bitmap = bitmap;
        this.lastanimation = lastanimation;
        this.iv = imageView;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Log.v("TEST","Animation start");
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.v("TEST","Animation end");
        iv.setImageBitmap(bitmap);
        iv.setAnimation(lastanimation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        Log.v("TEST","Animation end");
    }
}
