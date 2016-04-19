package ru.lionzxy.yandexmusic.animations;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
public class ResizeTextAnimation extends Animation {

    final float startSize;
    final float targetSize;
    TextView view;

    public ResizeTextAnimation(TextView view, float targetSize) {
        this.view = view;
        this.targetSize = targetSize;
        startSize = view.getTextSize();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float newSize = (startSize + (targetSize - startSize) * interpolatedTime);
        view.setTextSize(newSize);
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
