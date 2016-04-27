package ru.lionzxy.yandexmusic.collections.recyclerviews;

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
    View view = null;
    ImageView imageView = null;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        if (view != null)
            imageView = (ImageView) view.findViewById(R.id.imageView);
    }

    public RecyclerViewHolder setItem(IListElement listElement) {
        if (view == null)
            return this;

        if (imageView != null) {
            imageView.setImageDrawable(null);
            listElement.setImage(imageView, false);
        }

        view.findViewById(R.id.card_view).setOnClickListener(listElement);
        listElement.setItem(view);
        return this;
    }
}
