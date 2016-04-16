package ru.lionzxy.yandexmusic.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by LionZXY on 16.04.2016.
 * YandexMusic
 */
public class AuthorDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mobilization.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_AUTHOR_TABLE = "author";
    private static final String DATABASE_GENRES_TABLE = "genres";

    public AuthorDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO
        db.execSQL("create table " + DATABASE_AUTHOR_TABLE + "()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static final class AUTHOR_COLUMN implements BaseColumns {
        public static final String ID_COLUMN = "id";
        public static final String BIG_IMAGE_LINK_COLUMN = "bigimage";
        public static final String SMALL_IMAGE_LINK_COLUMN = "smallimage";
        public static final String GENRES_INT_ARR_COLUMN = "genres";
        public static final String NAME_COLUNM = "name";
        public static final String DESCRIPTION_COLUMN = "description";
        public static final String LINK_COLUMN = "link";
        public static final String AUTHOR_ID_COLUMN = "author_id";
        public static final String TRACKS_INT_COLUMN = "tracks";
        public static final String ALBUMS_INT_COLUMN = "albums";
    }
}
