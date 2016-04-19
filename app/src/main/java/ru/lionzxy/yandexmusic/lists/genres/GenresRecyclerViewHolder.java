package ru.lionzxy.yandexmusic.lists.genres;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.lionzxy.yandexmusic.R;
import com.squareup.picasso.*;

/**
 * Created by LionZXY on 14.04.2016.
 * YandexMusic
 */
public class GenresRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public LinearLayout view;
    public GenresObject go;
    private Activity activity;

    public GenresRecyclerViewHolder(LinearLayout itemView, Activity activity) {
        super(itemView);
        view = itemView;
        itemView.findViewById(R.id.card_view).setOnClickListener(this);
        this.activity = activity;
    }

    public GenresRecyclerViewHolder setItem(GenresObject go) {
        this.go = go;
		
		Picasso.with(view.getContext()).load(go.smallImage).placeholder(R.drawable.notfoundmusic).into((ImageView) view.findViewById(R.id.genresPic));
        ((TextView) view.findViewById(R.id.genresName)).setText(go.name);
        return this;
    }


    @Override
    public void onClick(View v) {
        //TODO
    }
}
