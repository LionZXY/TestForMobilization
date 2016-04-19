package ru.lionzxy.yandexmusic.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.views.AnimatedImageView;

public class ImageHelper {

    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/Android/data/ru.lionzxy.yandexmusic/images";

    public static void setImageOnImageView(final AnimatedImageView imageView, String url, String uniqName, @Nullable Picasso.Priority priority) {
        priority = priority == null ? Picasso.Priority.NORMAL : priority;
        final File file = new File(DATA_PATH, uniqName);
        if (file.exists()) {
            Picasso.with(imageView.getContext()).load(file).error(R.drawable.notfoundmusic).placeholder(R.drawable.loading).fit().priority(priority).into(imageView);
        } else {
            Picasso.with(imageView.getContext()).load(url).error(R.drawable.notfoundmusic).placeholder(R.drawable.loading).into(new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageView.setImageBitmap(bitmap);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!file.getParentFile().exists())
                                    file.getParentFile().mkdirs();
                                file.createNewFile();


                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
                                fos.flush();
                                fos.close();
                            } catch (Exception e) {
                                Log.e("ImageSaver", "Error while save image", e);
                            }
                        }
                    }).start();

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    imageView.setImageDrawable(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    imageView.setImageDrawable(placeHolderDrawable);
                }
            });
        }
    }
}