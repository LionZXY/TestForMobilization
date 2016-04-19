package ru.lionzxy.yandexmusic.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
public class NormalTranslateAnimation extends Animation {

    final float startY;
    final float targetY;
    final float startX;
    final float targetX;
    View view;

    public NormalTranslateAnimation(View view, float targetX, float targetY) {
        this.view = view;
        startY = view.getY();
        startX = view.getX();
        this.targetY = targetY;
        this.targetX = targetX;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float newY = (startY + (targetY - startX) * interpolatedTime);
        float newX = (startX + (targetX - startX) * interpolatedTime);
        view.setY(newY);
        view.setX(newX);
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
