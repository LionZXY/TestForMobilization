package ru.lionzxy.yandexmusic.io;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.collections.enums.ImageResourceType;
import ru.lionzxy.yandexmusic.exceptions.ContextDialogException;

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
     * Download and save image with special resolution
     *
     * @param imageView
     * @param width     save width bitmap. Use for low size
     * @param height    save height bitmap. Use for low size
     */
    public void setImageOnImageView(@NonNull final ImageView imageView, final int width, final int height, @Nullable final ImageLoadingListener listener) {
        if (imageView == null)
            return;

        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.notfoundmusic).build();

        try {
            switch (imageResourceType) {
                case LOCAL_STORAGE: {
                    String path = DATA_PATH + "/" + filename;
                    if (listener == null)
                        imageLoader.displayImage("file://" + path, imageView);
                    else imageLoader.displayImage("file://" + path, imageView, listener);
                    break;
                }
                case NETWORK: {
                    imageLoader.displayImage(imageUrl, imageView, options, new ImageLoadingListener() {
                        public void onLoadingStarted(String imageUri, View view) {if (listener != null) listener.onLoadingStarted(imageUri, view);}
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {if (listener != null) listener.onLoadingFailed(imageUri, view, failReason);}
                        public void onLoadingCancelled(String imageUri, View view) {if (listener != null) listener.onLoadingCancelled(imageUri, view);}

                        @Override
                        public void onLoadingComplete(final String imageUri, View view, final Bitmap loadedImage) {
                            if (listener != null) listener.onLoadingComplete(imageUri, view, loadedImage);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    saveImage(loadedImage, width, height);
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
        } catch (IllegalStateException ex) {
            ImageResource.imageLoader.init(ImageLoaderConfiguration.createDefault(imageView.getContext()));
            setImageOnImageView(imageView, width, height, listener);
        } catch (Exception e) {
            new ContextDialogException(imageView.getContext(), e);
        }
    }

    public void setImageOnImageView(final ImageView imageView) {
        setImageOnImageView(imageView, 0, 0, null);
    }

    public void setImageOnImageView(final ImageView imageView, ImageLoadingListener listener) {
        setImageOnImageView(imageView, 0, 0, listener);
    }

    public void setImageOnImageView(final ImageView imageView, boolean fit) {
        if (fit)
            setImageOnImageView(imageView, imageView.getWidth(), imageView.getHeight(), null);
        else setImageOnImageView(imageView);
    }

    public void setImageOnImageView(final ImageView imageView, ImageLoadingListener listener, boolean fit) {
        if (fit)
            setImageOnImageView(imageView, imageView.getWidth(), imageView.getHeight(), listener);
        else setImageOnImageView(imageView);
    }


    private void saveImage(Bitmap bitmap, int width, int height) {
        try {
            if (width > 0 || height > 0)
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

            if (!imageFile.getParentFile().exists())
                imageFile.getParentFile().mkdirs();
            imageFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
            fos.flush();
            fos.close();

            imageResourceType = ImageResourceType.LOCAL_STORAGE;
        } catch (Exception e) {
            Log.e("ImageSaver", "Error while save image", e);
        }
    }


    public File getImageFile() {
        return imageFile;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

