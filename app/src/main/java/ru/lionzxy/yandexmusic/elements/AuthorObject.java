package ru.lionzxy.yandexmusic.elements;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AuthorObject {
    public String name;
    public String description;
    public Bitmap image;

    public AuthorObject(String name, String description, Bitmap image) {
        this.name = Character.isUpperCase(name.charAt(0)) ? name : name.replaceFirst(String.valueOf(name.charAt(0)), String.valueOf(Character.toUpperCase(name.charAt(0))));
        this.description = Character.isUpperCase(description.charAt(0)) ? description : description.replaceFirst(String.valueOf(description.charAt(0)), String.valueOf(Character.toUpperCase(description.charAt(0))));
        this.image = image;
    }
}
