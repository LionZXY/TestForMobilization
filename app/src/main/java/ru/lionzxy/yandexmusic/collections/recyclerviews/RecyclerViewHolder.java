package ru.lionzxy.yandexmusic.collections.recyclerviews;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.interfaces.IListElement;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private View view = null;
    private ImageView imageView = null;
    private Activity activity;

    public RecyclerViewHolder(@Nullable Activity activity, View itemView) {
        super(itemView);
        this.view = itemView;
        this.activity = activity;
        if (view != null)
            imageView = (ImageView) view.findViewById(R.id.imageView);
    }

    public RecyclerViewHolder setItem(final IListElement listElement) {
        if (view == null)
            return this;

        listElement.setItem(imageView, view, activity);
        return this;
    }
}
