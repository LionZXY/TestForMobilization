package ru.lionzxy.yandexmusic.io;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.lionzxy.yandexmusic.helper.TextHelper;
import ru.lionzxy.yandexmusic.lists.enums.ImageResourceType;

/**
 * Created by LionZXY on 15.04.2016.
 * YandexMusic
 */
public class ImageResource implements Serializable {
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/Android/data/ru.lionzxy.yandexmusic/Images";
    private ImageResourceType type = ImageResourceType.UNKNOWN;
    private String url = null, name = null;
    private File file = null;
    private boolean isBig, threadRun = false;

    public ImageResource(boolean isBig, String uniqName, String url) {
        this.url = url;
        this.isBig = isBig;
        this.name = uniqName;
        type = ImageResourceType.NETWORK;
        file = new File(DATA_PATH + (isBig ? "/big/" : "/small/"), TextHelper.getFileName(name) + ".png");
        if (file.exists()) {
            type = ImageResourceType.LOCAL_STORAGE;
        }
    }

    public ImageResource(boolean isBig, String uniqName, File file) {
        this.isBig = isBig;
        this.name = uniqName;
        type = ImageResourceType.LOCAL_STORAGE;
        this.file = file;
    }

    /**
     * Use activity.runOnUiThread for using on ImageView
     *
     * @param recieveImage interface for get image
     */
    public void getImage(@NonNull final IRecieveImage recieveImage) {
        if (!threadRun) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        getImageInThisThread(recieveImage);
                    } catch (Exception e) {
                        Log.e("ImageResource", "Error while get image", e);
                        recieveImage.recieveResource(null);
                    }
                    threadRun = false;
                }
            }).start();
            threadRun = true;
        } else {
            //When thread now working
            recieveImage.recieveResource(null);
            /*Thread thr = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (threadRun) {
                            Thread.sleep(10);
                        }
                        getImage(recieveImage);
                    } catch (Exception e) {
                        Log.e("ImageResource", "Error while get image when thread is starting", e);
                        recieveImage.recieveResource(null);
                    }
                }
            });
            thr.setPriority(Thread.MIN_PRIORITY);
            thr.start();*/
        }
    }

    public void getImageInThisThread(IRecieveImage recieveImage) throws Exception {
        getImageInThisThread(recieveImage, true);
    }

    /**
     * Work in one thread is helping for testing app
     * Why it not using return? Because it broke save on local storage technology (need download image again or rewrite
     * save in other method. It's a bad...)
     *
     * @param recieveImage interface for get image
     * @param save         only for network. Save when download.
     */
    public void getImageInThisThread(@NonNull IRecieveImage recieveImage, boolean save) throws Exception {
        switch (type) {
            case NETWORK: {
                if (url == null) {
                    recieveImage.recieveResource(null);
                    break;
                }
                Log.i("ImageResource", "Download " + url);
                //Open connection and download image
                HttpURLConnection urlc = (HttpURLConnection) new URL(url).openConnection();

                urlc.setConnectTimeout(5000);
                urlc.setRequestMethod("GET");

                Bitmap bitmap = BitmapFactory.decodeStream(urlc.getInputStream());

                recieveImage.recieveResource(bitmap);

                urlc.disconnect();

                //Save file to device
                if (save) {
                    if (file == null)
                        file = new File(DATA_PATH + (isBig ? "/big/" : "/small/"), TextHelper.getFileName(name) + ".png");
                    if (!file.getParentFile().exists())
                        file.getParentFile().mkdirs();
                    file.createNewFile();

                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fos);
                    fos.flush();
                    fos.close();
                    Log.i("ImageResource", "File saved");
                    this.type = ImageResourceType.LOCAL_STORAGE;
                }
                break;
            }

            case LOCAL_STORAGE: {
                Log.i("ImageResource", "Load image from device  " + file);

                if (file == null) {
                    recieveImage.recieveResource(null);
                    break;
                }

                recieveImage.recieveResource(BitmapFactory.decodeStream(new FileInputStream(file)));
                Log.i("ImageResource","Load sucsesful");
                break;
            }
        }
    }

}
