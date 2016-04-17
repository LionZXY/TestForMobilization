package ru.lionzxy.yandexmusic.lists.author;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.lionzxy.yandexmusic.AboutAuthor;
import ru.lionzxy.yandexmusic.R;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AuthorRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public LinearLayout view;
    public AuthorObject ao;
    private Activity activity;

    public AuthorRecyclerViewHolder(LinearLayout itemView, Activity activity) {
        super(itemView);
        this.view = itemView;
        itemView.findViewById(R.id.card_view).setOnClickListener(this);
        this.activity = activity;
    }

    public AuthorRecyclerViewHolder setItem(AuthorObject ao) {
        this.ao = ao;
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.clearAnimation();
        iv.setImageResource(R.drawable.notfoundmusic);
        ao.setImageOnItemView(activity, iv, false);
        ((TextView) view.findViewById(R.id.description)).setText(ao.description);
        ((TextView) view.findViewById(R.id.head_author)).setText(ao.name);
        return this;
    }


    @Override
    public void onClick(View v) {
        try {
            Intent intent = new Intent(activity, AboutAuthor.class);
            intent.putExtra("authorObject", ao);
            activity.startActivity(intent);
        } catch (Exception e) {
            Log.e("YandexMusic", "Error on open about author activity", e);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Невозможно открыть страничку автора").setNeutralButton("Смириться", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }
    }
}
