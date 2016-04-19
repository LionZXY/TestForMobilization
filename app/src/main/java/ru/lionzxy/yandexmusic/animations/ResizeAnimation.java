package ru.lionzxy.yandexmusic.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
public class ResizeAnimation extends Animation {
    final int startHight;
    final int targetHight;
    final int startWidth;
    final int targetWidth;
    View view;

    public ResizeAnimation(View view, int targetHight, int targetWidth) {
        this.view = view;
        startWidth = view.getWidth();
        startHight = view.getHeight();
        this.targetHight = targetHight;
        this.targetWidth = targetWidth;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight = (int) (startHight + (targetHight - startHight) * interpolatedTime);
        int newWidth = (int) (startWidth + (targetWidth - startWidth) * interpolatedTime);
        view.getLayoutParams().height = newHeight;
        view.getLayoutParams().width = newWidth;
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
