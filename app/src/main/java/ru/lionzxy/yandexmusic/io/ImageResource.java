package ru.lionzxy.yandexmusic.io;

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
import java.io.Serializable;

import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.collections.enums.ImageResourceType;

/**
 * Created by LionZXY on 20.04.2016.
 * YandexMusic
 */
public class ImageResource implements Serializable {
    public static ImageLoader imageLoader = ImageLoader.getInstance();

    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/Android/data/ru.lionzxy.yandexmusic/images";

    private String imageUrl, filename;
    private File imageFile;
    public ImageResourceType imageResourceType = ImageResourceType.UNKNOWN;

    public ImageResource(String imageUrl, String filename) {
        this.imageUrl = imageUrl;
        this.imageFile = new File(DATA_PATH, filename);
        this.filename = filename;

        //Local storage have height priority
        imageResourceType = imageFile.exists() ? ImageResourceType.LOCAL_STORAGE : (imageUrl == null) ? ImageResourceType.UNKNOWN : ImageResourceType.NETWORK;
    }

    /**
     * Download and save image.
     *
     * @param imageView
     */
    public void setImageOnImageView(final ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loading).showImageOnFail(R.drawable.notfoundmusic).build();

        switch (imageResourceType) {
            case LOCAL_STORAGE: {
                String path = DATA_PATH + "/" + filename;
                imageLoader.displayImage("file://" + path, imageView);
                break;
            }
            case NETWORK: {
                imageLoader.displayImage(imageUrl, imageView, options, new ImageLoadingListener() {
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    public void onLoadingCancelled(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingComplete(final String imageUri, View view, final Bitmap loadedImage) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!imageFile.getParentFile().exists())
                                        imageFile.getParentFile().mkdirs();
                                    imageFile.createNewFile();

                                    FileOutputStream fos = new FileOutputStream(imageFile);
                                    loadedImage.compress(Bitmap.CompressFormat.PNG, 70, fos);
                                    fos.flush();
                                    fos.close();

                                    imageResourceType = ImageResourceType.LOCAL_STORAGE;
                                } catch (Exception e) {
                                    Log.e("ImageSaver", "Error while save image", e);
                                }
                            }
                        }).start();
                    }
                });
                break;
            }
            case UNKNOWN: {
                imageView.setImageResource(R.drawable.notfoundmusic);
                break;
            }
        }
    }

    public File getImageFile() {
        return imageFile;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

