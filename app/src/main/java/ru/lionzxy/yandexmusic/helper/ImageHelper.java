package ru.lionzxy.yandexmusic.helper;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;

import ru.lionzxy.yandexmusic.R;

public class ImageHelper {
    public static ImageLoader imageLoader = ImageLoader.getInstance();

    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/Android/data/ru.lionzxy.yandexmusic/images";


    public static void setImageOnImageView(final ImageView imageView, String url, String uniqName) {
        String path = DATA_PATH + "/" + uniqName;
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loading).showImageOnFail(R.drawable.notfoundmusic).build();
        final File file = new File(DATA_PATH, uniqName);
        if (file.exists()) {
            imageLoader.displayImage("file://" + path, imageView);
        } else {
            imageLoader.displayImage(url, imageView, options, new ImageLoadingListener() {
                public void onLoadingStarted(String imageUri, View view) {}
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

                @Override
                public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!file.getParentFile().exists())
                                    file.getParentFile().mkdirs();
                                file.createNewFile();


                                FileOutputStream fos = new FileOutputStream(file);
                                loadedImage.compress(Bitmap.CompressFormat.PNG, 70, fos);
                                fos.flush();
                                fos.close();
                            } catch (Exception e) {
                                Log.e("ImageSaver", "Error while save image", e);
                            }
                        }
                    }).start();
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
    }
}