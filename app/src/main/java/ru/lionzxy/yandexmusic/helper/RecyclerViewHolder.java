package ru.lionzxy.yandexmusic.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import ru.lionzxy.yandexmusic.AboutAuthor;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.elements.AuthorObject;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CardView cardView;
    public AuthorObject ao;
    private Context context;

    public RecyclerViewHolder(CardView itemView, Context context) {
        super(itemView);
        cardView = itemView;
        itemView.setOnClickListener(this);
        this.context = context;
    }

    public RecyclerViewHolder setItem(AuthorObject ao) {
        this.ao = ao;
        ((ImageView) cardView.findViewById(R.id.imageView)).setImageBitmap(ao.image);
        ((TextView) cardView.findViewById(R.id.description)).setText(ao.description);
        ((TextView) cardView.findViewById(R.id.head_author)).setText(ao.name);
        return this;
    }


    @Override
    public void onClick(View v) {
        try {
            Intent intent = new Intent(context, AboutAuthor.class);
            intent.putExtra("authorObject", (Serializable) ao);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Невозможно открыть страничку автора").setNeutralButton("Смириться", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }
    }
}
