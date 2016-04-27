package ru.lionzxy.yandexmusic.collections.recyclerviews.elements;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.lionzxy.yandexmusic.AboutAuthor;
import ru.lionzxy.yandexmusic.LoadingActivity;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.collections.enums.ImageResourceType;
import ru.lionzxy.yandexmusic.exceptions.ContextDialogException;
import ru.lionzxy.yandexmusic.exceptions.ErrorJsonFileException;
import ru.lionzxy.yandexmusic.helper.ColorHelper;
import ru.lionzxy.yandexmusic.helper.DatabaseHelper;
import ru.lionzxy.yandexmusic.helper.PixelHelper;
import ru.lionzxy.yandexmusic.helper.TextHelper;
import ru.lionzxy.yandexmusic.interfaces.IListElement;
import ru.lionzxy.yandexmusic.io.ImageResource;
import ru.lionzxy.yandexmusic.views.AnimatedImageView;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */

public class AuthorObject implements Serializable, IListElement, View.OnClickListener {
    public static final AuthorObject UNKNOWN = new AuthorObject();
    public List<GenresObject> genresObjects = new ArrayList<>();
    public String name, description, link;
    public ImageResource bigImage, smallImage;
    public int authorId = 1, tracks = -1, albums = -1, color = 0;
    public long idInDB = -1L;

    private AuthorObject() {
        this.name = "Неизвестный автор";
        this.description = "Если вы видете это сообщение, значит произошла ошибка в программе. Пожалуйста сообщите об этом разработчику";
        this.link = null;
        this.bigImage = null;
        this.smallImage = null;

    }

    public AuthorObject(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
    }

    public AuthorObject(Cursor cursor) {
        idInDB = cursor.getLong(cursor.getColumnIndex("rowid"));

        authorId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.AUTHOR_ID_COLUMN));
        bigImage = new ImageResource(cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.BIG_IMAGE_LINK_COLUMN)), "bigAuthor" + authorId);
        smallImage = new ImageResource(cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.SMALL_IMAGE_LINK_COLUMN)), "smallAuthor" + authorId);

        albums = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.ALBUMS_INT_COLUMN));
        tracks = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.TRACKS_INT_COLUMN));
        description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.DESCRIPTION_COLUMN));

        name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.NAME_COLUNM));
        link = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.LINK_COLUMN));
        String tmpGenres = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.GENRES_INT_ARR_COLUMN));
        if (tmpGenres != null) {
            List<Integer> colors = new ArrayList<>();
            String tmpGenresString[] = tmpGenres.split(",");
            for (String str : tmpGenresString) {
                GenresObject ge = LoadingActivity.genresHashMapOnDBID.get(Long.parseLong(str));
                if (ge != null) {
                    genresObjects.add(ge);
                    colors.add(ge.color);
                }
            }
            color = ColorHelper.mixColors(colors);
        }
    }

    public AuthorObject(JSONObject jsonObject) throws ErrorJsonFileException, JSONException {

        if (jsonObject == null || !jsonObject.has("id"))
            throw new ErrorJsonFileException();
        authorId = jsonObject.getInt("id");
        name = jsonObject.has("name") ? TextHelper.upperFirstSymbols(jsonObject.getString("name")) : UNKNOWN.name;

        if (jsonObject.has("genres")) {
            int brokenVal = 0;
            List<Integer> genres = new ArrayList<>();
            JSONArray arr = jsonObject.getJSONArray("genres");
            for (int i = 0; i < arr.length(); i++) {
                GenresObject genresObject = LoadingActivity.genresHashMap.get(arr.getString(i));
                if (genresObject != null){
                    genresObjects.add(genresObject);
                    genres.add(genresObject.color);
                }
            }
            color = ColorHelper.mixColors(genres);
        }

        tracks = jsonObject.has("tracks") ? jsonObject.getInt("tracks") : UNKNOWN.tracks;
        albums = jsonObject.has("albums") ? jsonObject.getInt("albums") : UNKNOWN.albums;
        link = jsonObject.has("link") ? jsonObject.getString("link") : UNKNOWN.link;
        description = jsonObject.has("description") ? TextHelper.upperFirstSymbols(jsonObject.getString("description")) : UNKNOWN.description;

        if (jsonObject.has("cover")) {
            JSONObject covers = jsonObject.getJSONObject("cover");
            smallImage = covers.has("small") ? new ImageResource(covers.getString("small"), "smallAuthor" + authorId) : UNKNOWN.smallImage;
            bigImage = covers.has("big") ? new ImageResource(covers.getString("big"), "bigAuthor" + authorId) : UNKNOWN.bigImage;
        }
    }


    public AuthorObject putInDB(SQLiteDatabase mSqLiteDatabase) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.AUTHOR_COLUMN.ALBUMS_INT_COLUMN, albums);
        values.put(DatabaseHelper.AUTHOR_COLUMN.AUTHOR_ID_COLUMN, authorId);
        values.put(DatabaseHelper.AUTHOR_COLUMN.TRACKS_INT_COLUMN, tracks);
        if (bigImage != null)
            values.put(DatabaseHelper.AUTHOR_COLUMN.BIG_IMAGE_LINK_COLUMN, bigImage.getImageUrl());
        if (smallImage != null)
            values.put(DatabaseHelper.AUTHOR_COLUMN.SMALL_IMAGE_LINK_COLUMN, smallImage.getImageUrl());
        values.put(DatabaseHelper.AUTHOR_COLUMN.DESCRIPTION_COLUMN, description);
        values.put(DatabaseHelper.AUTHOR_COLUMN.NAME_COLUNM, name);
        values.put(DatabaseHelper.AUTHOR_COLUMN.LINK_COLUMN, link);
        if (genresObjects.size() > 0){
            StringBuilder sb = new StringBuilder();
            sb.append(genresObjects.get(0).idInDB);
            for (int i = 1; i < genresObjects.size(); i++)
                sb.append(',').append(genresObjects.get(i).idInDB);
            values.put(DatabaseHelper.AUTHOR_COLUMN.GENRES_INT_ARR_COLUMN, sb.toString());
        }


        idInDB = mSqLiteDatabase.insert(DatabaseHelper.DATABASE_AUTHOR_TABLE, null, values);
        Log.i("Author", "Author " + name + " put in table. " + idInDB);
        return this;
    }


    @Override
    public void setImage(ImageView imageView, boolean isBig) {
        ImageResource imageResource = (isBig ? (bigImage == null ? smallImage : bigImage) : (smallImage == null ? bigImage : smallImage));
        if (imageResource != null) {
            if (isBig) {
                imageResource.setImageOnImageView(imageView, (imageResource.imageResourceType == ImageResourceType.NETWORK && smallImage != null && smallImage.imageResourceType == ImageResourceType.LOCAL_STORAGE) ? new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        ((ImageView) view).setImageBitmap(BitmapFactory.decodeFile(smallImage.getImageFile().getPath()));
                    }
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        ((ImageView) view).setImageBitmap(BitmapFactory.decodeFile(smallImage.getImageFile().getPath()));
                    }
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {}
                    public void onLoadingCancelled(String imageUri, View view) {}
                }:null);
            } else imageResource.setImageOnImageView(imageView, true);
        } else imageView.setImageResource(R.drawable.notfoundmusic);

    }

    public File getFile(boolean isBig) {
        return (isBig ? (bigImage == null ? smallImage : bigImage) : (smallImage == null ? bigImage : smallImage)).getImageFile();
    }

    @Override
    public void setItem(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        //Clear
        imageView.clearAnimation();
        //imageView.setImageResource(R.drawable.notfoundmusic);

        //Set content
        ((TextView) view.findViewById(R.id.description)).setText(description);
        ((TextView) view.findViewById(R.id.head_author)).setText(name);
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{color, Color.WHITE});
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gradientDrawable.setGradientCenter(0.2F, 0.5F);
        gradientDrawable.setGradientRadius(PixelHelper.pixelFromDP(view.getResources(), 100));
        ((ImageView) view.findViewById(R.id.cardViewBackground)).setImageDrawable(gradientDrawable);
        ((ImageView) view.findViewById(R.id.cardViewBackground)).setAlpha(0.6F);
    }


    @Override
    public void onClick(View view) {
        if (view == null)
            return;

        Context context = view.getContext();

        try {
            Intent intent = new Intent(context, AboutAuthor.class);
            intent.putExtra("authorObject", this);
            context.startActivity(intent);
        } catch (Exception e) {
            new ContextDialogException(context, e);
        }
    }
}
