package ru.lionzxy.yandexmusic.lists.author;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.lionzxy.yandexmusic.AboutAuthor;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.animations.NormalTranslateAnimation;
import ru.lionzxy.yandexmusic.animations.ResizeAnimation;
import ru.lionzxy.yandexmusic.animations.ResizeTextAnimation;
import ru.lionzxy.yandexmusic.helper.ImageHelper;
import ru.lionzxy.yandexmusic.helper.PixelHelper;
import ru.lionzxy.yandexmusic.lists.LockableRecyclerView;
import ru.lionzxy.yandexmusic.views.AnimatedImageView;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AuthorRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public LinearLayout view;
    public AuthorObject author;
    private ImageView imageView;
    Activity activity;
    private LockableRecyclerView recyclerView;

    public AuthorRecyclerViewHolder(LinearLayout itemView, Activity activity, LockableRecyclerView recyclerView) {
        super(itemView);
        this.view = itemView;
        this.activity = activity;
        itemView.findViewById(R.id.card_view).setOnClickListener(this);
        this.recyclerView = recyclerView;
    }

    public AuthorRecyclerViewHolder setItem(AuthorObject ao) {
        this.author = ao;

        //Find image
        imageView = (ImageView) view.findViewById(R.id.imageView);

        //Clear
        imageView.clearAnimation();
        imageView.setImageResource(R.drawable.notfoundmusic);

        //Set content
        ImageHelper.setImageOnImageView((AnimatedImageView) view.findViewById(R.id.imageView), ao.smallImage, "smallAuthor" + ao.idInDB, Picasso.Priority.LOW);
        ((TextView) view.findViewById(R.id.description)).setText(ao.description);
        ((TextView) view.findViewById(R.id.head_author)).setText(ao.name);
        return this;
    }

    @Override
    public void onClick(final View v) {
        openAboutAuthor();
        /*
        //TODO view animation
        recyclerView.setLock(true);

        final Animation unVisible = AnimationUtils.loadAnimation(view.getContext(), R.anim.alphaunvisible);
        unVisible.setFillAfter(true);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        for (int i = 0; i < layoutManager.getChildCount(); i++) {
            final View childView = layoutManager.getChildAt(i);
            childView.findViewById(R.id.card_view).setOnClickListener(null);
            if (childView != view) {
                childView.startAnimation(unVisible);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        childView.setAlpha(0.0F);
                    }
                }, unVisible.getDuration());
            }
        }

        float thisY = - view.getY() - recyclerView.getY();
        final TranslateAnimation animation = new TranslateAnimation(0, 0, 0, thisY);
        animation.setDuration(unVisible.getDuration());
        animation.setFillAfter(true);
        view.findViewById(R.id.description).startAnimation(unVisible);
        view.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Resize cardview
                int textSize = (int) PixelHelper.pixelFromDP(view.getResources(), 60);
                CardView cv = (CardView) view.findViewById(R.id.card_view);
                ResizeAnimation resizeAnimation = new ResizeAnimation(cv, (int) PixelHelper.pixelFromDP(cv.getResources(), 250), cv.getWidth());
                resizeAnimation.setFillAfter(true);
                resizeAnimation.setDuration(unVisible.getDuration());
                cv.startAnimation(resizeAnimation);

                //Move image
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                final NormalTranslateAnimation translateAnimation = new NormalTranslateAnimation(imageView, PixelHelper.pixelFromDP(imageView.getResources(), 2), textSize + (int) PixelHelper.pixelFromDP(cv.getResources(), 10));
                translateAnimation.setDuration(unVisible.getDuration());
                translateAnimation.setFillAfter(true);
                imageView.startAnimation(translateAnimation);

                //Resize text
                final TextView textView = (TextView) view.findViewById(R.id.head_author);
                ResizeTextAnimation resizeTextAnimation = new ResizeTextAnimation(textView, 50F);
                resizeTextAnimation.setDuration(unVisible.getDuration());
                resizeTextAnimation.setFillBefore(true);
                textView.startAnimation(resizeTextAnimation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final CardView cv = (CardView) view.findViewById(R.id.card_view);

                        //Resize image
                        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                        int newSize = (int) PixelHelper.pixelFromDP(imageView.getResources(), 180);
                        ResizeAnimation resizeImageAnimation = new ResizeAnimation(imageView, newSize, newSize);
                        resizeImageAnimation.setDuration(unVisible.getDuration());
                        resizeImageAnimation.setFillAfter(true);
                        imageView.startAnimation(resizeImageAnimation);

                        //Move text
                        final TextView textViewHead = (TextView) view.findViewById(R.id.head_author);
                        NormalTranslateAnimation normalTranslateAnimation = new NormalTranslateAnimation(textViewHead, 0 - PixelHelper.pixelFromDP(imageView.getResources(), 90), textViewHead.getY());
                        normalTranslateAnimation.setDuration(unVisible.getDuration());
                        normalTranslateAnimation.setFillAfter(true);
                        textViewHead.startAnimation(normalTranslateAnimation);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openAboutAuthor();
                            }
                        }, unVisible.getDuration());
                    }
                }, unVisible.getDuration());
            }
        }, animation.getDuration());*/
    }


    public void openAboutAuthor() {
        try {
            Intent intent = new Intent(imageView.getContext(), AboutAuthor.class);
            intent.putExtra("authorObject", author);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivity(intent);
        } catch (Exception e) {
            Log.e("YandexMobilization", "Error on open about author activity", e);
            AlertDialog.Builder builder = new AlertDialog.Builder(imageView.getContext());
            builder.setMessage("Невозможно открыть страничку автора").setNeutralButton("Смириться", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }

    }

}
