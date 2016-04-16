package ru.lionzxy.yandexmusic.lists.genres;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.lionzxy.yandexmusic.R;

/**
 * Created by LionZXY on 14.04.2016.
 * YandexMusic
 */
public class GenresRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public LinearLayout view;
    public GenresObject go;
    private Context context;

    public GenresRecyclerViewHolder(LinearLayout itemView, Context context) {
        super(itemView);
        view = itemView;
        itemView.findViewById(R.id.card_view).setOnClickListener(this);
        this.context = context;
    }

    public GenresRecyclerViewHolder setItem(GenresObject go) {
        this.go = go;
        ((ImageView) view.findViewById(R.id.genresPic)).setImageResource(go.imageId);
        ((TextView) view.findViewById(R.id.genresName)).setText(go.name);
        return this;
    }


    @Override
    public void onClick(View v) {
        //TODO
    }
}
