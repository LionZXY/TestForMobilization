package ru.lionzxy.yandexmusic.collections.recyclerviews.elements;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.lionzxy.yandexmusic.AboutAuthor;
import ru.lionzxy.yandexmusic.LoadingActivity;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.exceptions.ContextDialogException;
import ru.lionzxy.yandexmusic.exceptions.ErrorJsonFileException;
import ru.lionzxy.yandexmusic.helper.DatabaseHelper;
import ru.lionzxy.yandexmusic.helper.ImageHelper;
import ru.lionzxy.yandexmusic.helper.TextHelper;
import ru.lionzxy.yandexmusic.interfaces.IListElement;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */

public class AuthorObject implements Serializable, IListElement, View.OnClickListener {
    public static final AuthorObject UNKNOWN = new AuthorObject();
    public List<GenresObject> genresObjects = new ArrayList<>();
    public String name, description, link, bigImage, smallImage;
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
        bigImage = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.BIG_IMAGE_LINK_COLUMN));
        smallImage = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_COLUMN.SMALL_IMAGE_LINK_COLUMN));

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
            smallImage = covers.has("small") ? covers.getString("small") : UNKNOWN.smallImage;
            bigImage = covers.has("big") ? covers.getString("big") : UNKNOWN.bigImage;
        }
    }


    public AuthorObject putInDB(SQLiteDatabase mSqLiteDatabase) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.AUTHOR_COLUMN.ALBUMS_INT_COLUMN, albums);
        values.put(DatabaseHelper.AUTHOR_COLUMN.AUTHOR_ID_COLUMN, authorId);
        values.put(DatabaseHelper.AUTHOR_COLUMN.TRACKS_INT_COLUMN, tracks);
        if (bigImage != null)
            values.put(DatabaseHelper.AUTHOR_COLUMN.BIG_IMAGE_LINK_COLUMN, bigImage);
        if (smallImage != null)
            values.put(DatabaseHelper.AUTHOR_COLUMN.SMALL_IMAGE_LINK_COLUMN, smallImage);
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


    @Override
    public void setImage(ImageView imageView) {
        ImageHelper.setImageOnImageView(imageView, smallImage, "smallAuthor" + authorId);
    }

    @Override
    public void setItem(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        //Clear
        imageView.clearAnimation();
        imageView.setImageResource(R.drawable.notfoundmusic);

        //Set content
        ((TextView) view.findViewById(R.id.description)).setText(description);
        ((TextView) view.findViewById(R.id.head_author)).setText(name);
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

        /*
        //TODO view animation
        recyclerView.setLock(true);

        final Animation unVisible = AnimationUtils.loadAnimation(view.getContext(), R.anim.alphaunvisible);
        unVisible.setFillAfter(true);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        for (int i = 0; i < layoutManager.getChildCount(); i++) {
            final View childView = layoutManager.getChildAt(i);
            childView.findViewById(R.id.card_view).setOnClickListener(null);
            if (childView != view) {
                childView.startAnimation(unVisible);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        childView.setAlpha(0.0F);
                    }
                }, unVisible.getDuration());
            }
        }

        float thisY = - view.getY() - recyclerView.getY();
        final TranslateAnimation animation = new TranslateAnimation(0, 0, 0, thisY);
        animation.setDuration(unVisible.getDuration());
        animation.setFillAfter(true);
        view.findViewById(R.id.description).startAnimation(unVisible);
        view.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Resize cardview
                int textSize = (int) PixelHelper.pixelFromDP(view.getResources(), 60);
                CardView cv = (CardView) view.findViewById(R.id.card_view);
                ResizeAnimation resizeAnimation = new ResizeAnimation(cv, (int) PixelHelper.pixelFromDP(cv.getResources(), 250), cv.getWidth());
                resizeAnimation.setFillAfter(true);
                resizeAnimation.setDuration(unVisible.getDuration());
                cv.startAnimation(resizeAnimation);

                //Move image
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                final NormalTranslateAnimation translateAnimation = new NormalTranslateAnimation(imageView, PixelHelper.pixelFromDP(imageView.getResources(), 2), textSize + (int) PixelHelper.pixelFromDP(cv.getResources(), 10));
                translateAnimation.setDuration(unVisible.getDuration());
                translateAnimation.setFillAfter(true);
                imageView.startAnimation(translateAnimation);

                //Resize text
                final TextView textView = (TextView) view.findViewById(R.id.head_author);
                ResizeTextAnimation resizeTextAnimation = new ResizeTextAnimation(textView, 50F);
                resizeTextAnimation.setDuration(unVisible.getDuration());
                resizeTextAnimation.setFillBefore(true);
                textView.startAnimation(resizeTextAnimation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final CardView cv = (CardView) view.findViewById(R.id.card_view);

                        //Resize image
                        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                        int newSize = (int) PixelHelper.pixelFromDP(imageView.getResources(), 180);
                        ResizeAnimation resizeImageAnimation = new ResizeAnimation(imageView, newSize, newSize);
                        resizeImageAnimation.setDuration(unVisible.getDuration());
                        resizeImageAnimation.setFillAfter(true);
                        imageView.startAnimation(resizeImageAnimation);

                        //Move text
                        final TextView textViewHead = (TextView) view.findViewById(R.id.head_author);
                        NormalTranslateAnimation normalTranslateAnimation = new NormalTranslateAnimation(textViewHead, 0 - PixelHelper.pixelFromDP(imageView.getResources(), 90), textViewHead.getY());
                        normalTranslateAnimation.setDuration(unVisible.getDuration());
                        normalTranslateAnimation.setFillAfter(true);
                        textViewHead.startAnimation(normalTranslateAnimation);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openAboutAuthor();
                            }
                        }, unVisible.getDuration());
                    }
                }, unVisible.getDuration());
            }
        }, animation.getDuration());*/
    }
}
