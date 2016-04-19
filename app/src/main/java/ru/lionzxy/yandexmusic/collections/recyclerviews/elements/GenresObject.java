package ru.lionzxy.yandexmusic.collections.recyclerviews.elements;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import ru.lionzxy.yandexmusic.LoadingActivity;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.exceptions.ErrorJsonFileException;
import ru.lionzxy.yandexmusic.helper.DatabaseHelper;
import ru.lionzxy.yandexmusic.helper.ImageHelper;
import ru.lionzxy.yandexmusic.interfaces.IListElement;

/**
 * Created by nikit_000 on 14.04.2016.
 */
public class GenresObject implements Serializable, IListElement {
    public static final GenresObject UNKNOWN = new GenresObject();

    public int color;
    public long idInDB = -1L;
    public String name, code, fullname, smallImage, bigImage;

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
            smallImage = images.has("208x208") ? images.getString("208x208") : null;
            bigImage = images.has("300x300") ? images.getString("300x300") : null;
        }
        LoadingActivity.genresHashMap.put(code, this);
    }

    public GenresObject(Cursor cursor) {
        idInDB = cursor.getLong(cursor.getColumnIndex("rowid"));

        code = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.CODE_COLUMN));
        bigImage = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.BIG_IMAGE_LINK_COLUMN));
        smallImage = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.SMALL_IMAGE_LINK_COLUMN));
        color = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.COLOR_COLUMN));
        name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.NAME_COLUNM));
        fullname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN.FULLNAME_COLUMN));

        LoadingActivity.genresHashMap.put(code, this);
        LoadingActivity.genresHashMapOnDBID.put(idInDB, this);
    }

    public GenresObject putInDB(SQLiteDatabase mSqLiteDatabase) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.GENRES_COLUMN.BIG_IMAGE_LINK_COLUMN, bigImage);
        values.put(DatabaseHelper.GENRES_COLUMN.SMALL_IMAGE_LINK_COLUMN, smallImage);
        values.put(DatabaseHelper.GENRES_COLUMN.CODE_COLUMN, code);
        values.put(DatabaseHelper.GENRES_COLUMN.COLOR_COLUMN, color);
        values.put(DatabaseHelper.GENRES_COLUMN.FULLNAME_COLUMN, fullname);
        values.put(DatabaseHelper.GENRES_COLUMN.NAME_COLUNM, name);

        idInDB = mSqLiteDatabase.insert(DatabaseHelper.DATABASE_GENRES_TABLE, null, values);
        Log.i("Genres", "Genre " + name + " put in table. " + idInDB);
        return this;
    }

    @Override
    public void setImage(ImageView imageView) {
        ImageHelper.setImageOnImageView(imageView, smallImage, "smallGenre" + code);
    }

    @Override
    public void setItem(View view) {
        ((TextView) view.findViewById(R.id.genresName)).setText(name);
    }

    @Override
    public void onClick(View v) {

    }
}
