package ru.lionzxy.yandexmusic.lists.author;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.lionzxy.yandexmusic.AboutAuthor;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.io.IRecieveImage;
import ru.lionzxy.yandexmusic.io.ImageResource;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AuthorRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, IRecieveImage {

    public LinearLayout view;
    public AuthorObject ao;
    private Activity activity;
    private ImageView iv;
    //One view - one thread
    private Thread imageLoadThread = null;

    public AuthorRecyclerViewHolder(LinearLayout itemView, Activity activity) {
        super(itemView);
        this.view = itemView;
        itemView.findViewById(R.id.card_view).setOnClickListener(this);
        this.activity = activity;
    }

    public AuthorRecyclerViewHolder setItem(AuthorObject ao) {
        this.ao = ao;

        //Find image
        iv = (ImageView) view.findViewById(R.id.imageView);

        //Clear
        if (imageLoadThread != null)
            imageLoadThread.interrupt();
        iv.clearAnimation();
        iv.setImageResource(R.drawable.notfoundmusic);

        //Set content
        setImageOnItemView(activity, iv, false);
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

    public void setImageOnItemView(final Activity activity, final ImageView iv, boolean isBigPicture) {
        final Animation vis = AnimationUtils.loadAnimation(activity, R.anim.alphavisible);
        final ImageResource ir = isBigPicture ? ao.bigImage == null ? ao.smallImage : ao.bigImage : ao.smallImage == null ? ao.bigImage : ao.smallImage;
        if (ir != null)
            this.imageLoadThread = ir.getImage(this);
        else {
            iv.setImageResource(R.drawable.notfoundmusic);
            iv.startAnimation(vis);
            iv.setAlpha(1.0F);
        }
    }


    @Override
    public void recieveResource(final @Nullable Bitmap bitmap, final String id) {
        final Thread thisThread = Thread.currentThread();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final Animation vis = AnimationUtils.loadAnimation(activity, R.anim.alphavisible);
                final Animation unvis = AnimationUtils.loadAnimation(activity, R.anim.alphaunvisible);
                iv.clearAnimation();

                if (thisThread.isInterrupted())
                    return;

                if (bitmap != null) {
                    if (id.substring(id.lastIndexOf("_") + 1).equals(String.valueOf(ao.authorId)))
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                iv.setImageBitmap(bitmap);
                                iv.setAnimation(vis);
                            }
                        }, unvis.getDuration());
                    iv.setAlpha(1.0F);
                    iv.startAnimation(unvis);
                } else {
                    iv.startAnimation(vis);
                    iv.setAlpha(1.0F);
                }
            }
        });
    }
}
