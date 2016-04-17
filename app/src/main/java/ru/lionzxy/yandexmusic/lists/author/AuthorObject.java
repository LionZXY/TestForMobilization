package ru.lionzxy.yandexmusic.lists.author;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.lionzxy.yandexmusic.LoadingActivity;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.exceptions.ErrorJsonFileException;
import ru.lionzxy.yandexmusic.helper.DatabaseHelper;
import ru.lionzxy.yandexmusic.helper.TextHelper;
import ru.lionzxy.yandexmusic.io.IRecieveImage;
import ru.lionzxy.yandexmusic.io.ImageResource;
import ru.lionzxy.yandexmusic.lists.genres.GenresObject;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */

public class AuthorObject implements Serializable {
    public static final AuthorObject UNKNOWN = new AuthorObject();
    public ImageResource bigImage, smallImage;
    public List<GenresObject> genresObjects = new ArrayList<>();
    public String name, description, link;
    public int authorId = 1, tracks = -1, albums = -1;
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
        bigImage = new ImageResource(true, "author_" + authorId, cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.BIG_IMAGE_LINK_COLUMN)));
        smallImage = new ImageResource(false, "author_" + authorId, cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.SMALL_IMAGE_LINK_COLUMN)));

        albums = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.ALBUMS_INT_COLUMN));
        tracks = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.TRACKS_INT_COLUMN));
        description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.DESCRIPTION_COLUMN));

        name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.NAME_COLUNM));
        link = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.LINK_COLUMN));
        String tmpGenres = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.GENRES_INT_ARR_COLUMN));
        if (tmpGenres != null) {
            String tmpGenresString[] = tmpGenres.split(",");
            for (String str : tmpGenresString) {
                GenresObject ge = LoadingActivity.genresHashMapOnDBID.get(Long.parseLong(str));
                if (ge != null)
                    genresObjects.add(ge);
            }
        }
    }

    public AuthorObject(JSONObject jsonObject) throws ErrorJsonFileException, JSONException {

        if (jsonObject == null || !jsonObject.has("id"))
            throw new ErrorJsonFileException();
        authorId = jsonObject.getInt("id");
        name = jsonObject.has("name") ? TextHelper.upperFirstSymbols(jsonObject.getString("name")) : UNKNOWN.name;

        if (jsonObject.has("genres")) {
            int brokenVal = 0;
            JSONArray arr = jsonObject.getJSONArray("genres");
            for (int i = 0; i < arr.length(); i++) {
                GenresObject genresObject = LoadingActivity.genresHashMap.get(arr.getString(i));
                if (genresObject != null)
                    genresObjects.add(genresObject);
            }
        }

        tracks = jsonObject.has("tracks") ? jsonObject.getInt("tracks") : UNKNOWN.tracks;
        albums = jsonObject.has("albums") ? jsonObject.getInt("albums") : UNKNOWN.albums;
        link = jsonObject.has("link") ? jsonObject.getString("link") : UNKNOWN.link;
        description = jsonObject.has("description") ? TextHelper.upperFirstSymbols(jsonObject.getString("description")) : UNKNOWN.description;

        if (jsonObject.has("cover")) {
            JSONObject covers = jsonObject.getJSONObject("cover");
            smallImage = covers.has("small") ? new ImageResource(false, "author_" + authorId, covers.getString("small")) : UNKNOWN.smallImage;
            bigImage = covers.has("big") ? new ImageResource(true, "author_" + authorId, covers.getString("big")) : UNKNOWN.bigImage;
        }
    }


    public AuthorObject putInDB(SQLiteDatabase mSqLiteDatabase) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.AUTHOR_COLUMN.ALBUMS_INT_COLUMN, albums);
        values.put(DatabaseHelper.AUTHOR_COLUMN.AUTHOR_ID_COLUMN, authorId);
        values.put(DatabaseHelper.AUTHOR_COLUMN.TRACKS_INT_COLUMN, tracks);
        if (bigImage != null)
            values.put(DatabaseHelper.AUTHOR_COLUMN.BIG_IMAGE_LINK_COLUMN, bigImage.getAsURL());
        if (smallImage != null)
            values.put(DatabaseHelper.AUTHOR_COLUMN.SMALL_IMAGE_LINK_COLUMN, smallImage.getAsURL());
        values.put(DatabaseHelper.AUTHOR_COLUMN.DESCRIPTION_COLUMN, description);
        values.put(DatabaseHelper.AUTHOR_COLUMN.NAME_COLUNM, name);
        values.put(DatabaseHelper.AUTHOR_COLUMN.LINK_COLUMN, link);
        if (genresObjects.size() > 0) {
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

    public void setImageOnItemView(final Activity activity, final ImageView iv, boolean isBigPicture) {
        final Animation vis = AnimationUtils.loadAnimation(activity, R.anim.alphavisible);
        final Animation unvis = AnimationUtils.loadAnimation(activity, R.anim.alphaunvisible);
        final ImageResource ir = isBigPicture ? bigImage == null ? smallImage : bigImage : smallImage == null ? bigImage : smallImage;
        if (ir != null)
            ir.getImage(new IRecieveImage() {
                @Override
                public void recieveResource(final @Nullable Bitmap bitmap, final String name) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv.clearAnimation();
                            if (name.substring(name.lastIndexOf("_") + 1).equals(String.valueOf(authorId))) {
                                if (bitmap != null) {
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            if (name.substring(name.lastIndexOf("_") + 1).equals(String.valueOf(authorId)))
                                                iv.setImageBitmap(bitmap);
                                            iv.setAnimation(vis);
                                        }
                                    }, unvis.getDuration());
                                    iv.setAlpha(1.0F);
                                    iv.startAnimation(unvis);
                                } else {
                                    iv.startAnimation(vis);
                                    iv.setAlpha(1.0F);
                                }
                            }
                        }
                    });
                }
            });
        else {
            iv.setImageResource(R.drawable.notfoundmusic);
            iv.startAnimation(vis);
            iv.setAlpha(1.0F);
        }
    }


}
