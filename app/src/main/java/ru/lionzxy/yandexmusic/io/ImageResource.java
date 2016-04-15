package ru.lionzxy.yandexmusic.io;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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
    private boolean isBig;
    private Thread thread = null;

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

    public void getImage(final IRecieveImage recieveImage) {
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(new Runnable() {
                public void run() {
                    try {
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
                                if (file == null)
                                    file = new File(DATA_PATH + (isBig ? "/big/" : "/small/"), TextHelper.getFileName(name) + ".png");
                                if (!file.getParentFile().exists())
                                    file.getParentFile().mkdirs();
                                file.createNewFile();

                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 85, fos);
                                fos.flush();
                                fos.close();

                                type = ImageResourceType.LOCAL_STORAGE;
                                break;
                            }

                            case LOCAL_STORAGE: {
                                if (file == null) {
                                    recieveImage.recieveResource(null);
                                    break;
                                }
                                Log.v("ImageResource", "Load image from device");

                                recieveImage.recieveResource(BitmapFactory.decodeStream(new FileInputStream(file)));

                                break;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("ImageResource", "Error while get image", e);
                        recieveImage.recieveResource(null);
                    }
                }
            });
            thread.start();
        } else {
            //When thread now working
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        thread.join();
                        getImage(recieveImage);
                    } catch (Exception e) {
                        Log.e("ImageResource", "Error while get image when thread is starting", e);
                        recieveImage.recieveResource(null);
                    }
                }
            }).start();
        }
    }

}
