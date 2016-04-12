package ru.lionzxy.yandexmusic.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ru.lionzxy.yandexmusic.R;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder{

    public TextView title, description;
    public ImageView icon;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.head_author);
        description = (TextView) itemView.findViewById(R.id.description);
        icon = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
