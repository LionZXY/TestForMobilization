package ru.lionzxy.yandexmusic.lists.author;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.logging.Handler;

import android.content.res.Resources;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */


import ru.lionzxy.yandexmusic.helper.TextHelper;
import ru.lionzxy.yandexmusic.io.IRecieveImage;
import ru.lionzxy.yandexmusic.io.ImageResource;

public class AuthorObject implements Serializable {
    private ImageResource bigImage, smallImage;
    public String name, description;
    private int imageID = -1, authorId = -1;

    public AuthorObject(String name, String description, int authorId, int imageID) {
        this.name = TextHelper.upperFirstSymbols(name);
        this.description = TextHelper.upperFirstSymbols(description);
        this.imageID = imageID;
        this.authorId = authorId;
    }

    public void setImageOnItemView(final Activity activity, final ImageView iv, Resources r, boolean isBigPicture) {
        new ImageResource(false, String.valueOf(authorId), "http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300").getImage(new IRecieveImage() {
            @Override
            public void recieveResource(final @Nullable Bitmap bitmap) {
                if (bitmap != null)
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(bitmap);
                        }
                    });
            }
        });
    }

}
