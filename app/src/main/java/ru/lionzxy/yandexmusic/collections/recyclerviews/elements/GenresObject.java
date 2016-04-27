package ru.lionzxy.yandexmusic.collections.recyclerviews.elements;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.exceptions.ErrorJsonFileException;
import ru.lionzxy.yandexmusic.helper.DatabaseHelper;
import ru.lionzxy.yandexmusic.interfaces.IListElement;
import ru.lionzxy.yandexmusic.io.ImageResource;

/**
 * Created by nikit_000 on 14.04.2016.
 */
public class GenresObject implements Serializable, IListElement {
    public static final GenresObject UNKNOWN = new GenresObject();

    public int color;
    public long idInDB = -1L;
    public String name, code, fullname;
    public ImageResource bigImage, smallImage;

    private GenresObject() {
        this.name = "Неизвестный жанр";
        this.code = "unknown";
        this.fullname = name;
        this.color = Color.parseColor("#BFBFBF");
    }

    public GenresObject(String json) throws JSONException {
        this(new JSONObject(json));
    }

    public GenresObject(JSONObject object) throws ErrorJsonFileException, JSONException {
        if (!object.has("id") || object == null)
            throw new ErrorJsonFileException();

        this.code = object.has("id") ? object.getString("id") : UNKNOWN.code;
        this.name = object.has("title") ? object.getString("title") : UNKNOWN.name;
        this.fullname = object.has("fullTitle") ? object.getString("fullTitle") : this.name;
        this.color = object.has("color") ? Color.parseColor(object.getString("color")) : UNKNOWN.color;

        if (object.has("images")) {
            JSONObject images = object.getJSONObject("images");
            smallImage = images.has("208x208") ? new ImageResource(images.getString("208x208"), "smallGenre" + code) : null;
            bigImage = images.has("300x300") ? new ImageResource(images.getString("300x300"), "bigGenre" + code) : null;
        }
    }

    public GenresObject(Cursor cursor) {
        idInDB = cursor.getLong(cursor.getColumnIndex("rowid"));

        code = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.CODE_COLUMN));
        bigImage = new ImageResource(cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.BIG_IMAGE_LINK_COLUMN)), "bigGenre" + code);
        smallImage = new ImageResource(cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.SMALL_IMAGE_LINK_COLUMN)), "smallGenre" + code);

        color = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.COLOR_COLUMN));
        name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.NAME_COLUNM));
        fullname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.FULLNAME_COLUMN));

    }

    public GenresObject putInDB(SQLiteDatabase mSqLiteDatabase) {
        ContentValues values = new ContentValues();

        if (bigImage != null)
            values.put(DatabaseHelper.GENRES_COLUMN.BIG_IMAGE_LINK_COLUMN, bigImage.getImageUrl());
        if (smallImage != null)
            values.put(DatabaseHelper.GENRES_COLUMN.SMALL_IMAGE_LINK_COLUMN, smallImage.getImageUrl());
        values.put(DatabaseHelper.GENRES_COLUMN.CODE_COLUMN, code);
        values.put(DatabaseHelper.GENRES_COLUMN.COLOR_COLUMN, color);
        values.put(DatabaseHelper.GENRES_COLUMN.FULLNAME_COLUMN, fullname);
        values.put(DatabaseHelper.GENRES_COLUMN.NAME_COLUNM, name);

        idInDB = mSqLiteDatabase.insert(DatabaseHelper.DATABASE_GENRES_TABLE, null, values);
        Log.i("Genres", "Genre " + name + " put in table. " + idInDB);
        return this;
    }

    @Override
    public void setImage(ImageView imageView, boolean isBig) {
        ImageResource imageResource = (isBig ? (bigImage == null ? smallImage : bigImage) : (smallImage == null ? bigImage : smallImage));
        if (imageResource != null)
            imageResource.setImageOnImageView(imageView, true);
        else imageView.setImageResource(R.drawable.notfoundmusic);
    }

    @Override
    public void setItem(View view) {
        ((TextView) view.findViewById(R.id.genresName)).setText(name);

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TR_BL, new int[]{color, Color.WHITE});
        ((ImageView) view.findViewById(R.id.cardViewBackground)).setImageDrawable(gradientDrawable);
        ((ImageView) view.findViewById(R.id.cardViewBackground)).setAlpha(0.3F);
    }

    @Override
    public void onClick(View view, Activity activity) {

    }
}
