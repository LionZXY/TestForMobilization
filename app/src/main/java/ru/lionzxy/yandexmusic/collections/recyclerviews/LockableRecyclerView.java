package ru.lionzxy.yandexmusic.collections.recyclerviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
public class LockableRecyclerView extends RecyclerView {

    private boolean lock = false;

    public LockableRecyclerView(Context context) {
        super(context);
    }

    public LockableRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LockableRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return !lock && super.onTouchEvent(e);
    }
}
