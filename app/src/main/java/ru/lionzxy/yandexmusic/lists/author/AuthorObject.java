package ru.lionzxy.yandexmusic.lists.author;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import java.io.Serializable;
import android.content.res.Resources;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */


import android.content.res.*;public class AuthorObject implements Serializable
{
    public String name;
    public String description;
	private int imageID = -1;
	
    public AuthorObject(String name, String description, int imageID) {
        this.name = Character.isUpperCase(name.charAt(0)) ? name : name.replaceFirst(String.valueOf(name.charAt(0)), String.valueOf(Character.toUpperCase(name.charAt(0))));
        this.description = Character.isUpperCase(description.charAt(0)) ? description : description.replaceFirst(String.valueOf(description.charAt(0)), String.valueOf(Character.toUpperCase(description.charAt(0))));
        this.imageID = imageID;
    }
	
	public void setImageOnItemView(final ImageView iv, Resources r, boolean isBigPicture){
		iv.setImageResource(imageID);
	}

}
