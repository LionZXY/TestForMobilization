package ru.lionzxy.yandexmusic.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by LionZXY on 16.04.2016.
 * YandexMusic
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mobilization.db";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_AUTHOR_TABLE = "author";
    public static final String DATABASE_GENRES_TABLE = "genres";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_AUTHOR_TABLE + "(\n" +
                "    " + AUTHOR_COLUMN.BIG_IMAGE_LINK_COLUMN + " TEXT,\n" +
                "    " + AUTHOR_COLUMN.SMALL_IMAGE_LINK_COLUMN + " TEXT,\n" +
                "    " + AUTHOR_COLUMN.GENRES_INT_ARR_COLUMN + " TEXT,\n" +
                "    " + AUTHOR_COLUMN.NAME_COLUNM + " TEXT,\n" +
                "    " + AUTHOR_COLUMN.DESCRIPTION_COLUMN + " LONGTEXT,\n" +
                "    " + AUTHOR_COLUMN.LINK_COLUMN + " TEXT,\n" +
                "    " + AUTHOR_COLUMN.AUTHOR_ID_COLUMN + " INT,\n" +
                "    " + AUTHOR_COLUMN.TRACKS_INT_COLUMN + " INT,\n" +
                "    " + AUTHOR_COLUMN.ALBUMS_INT_COLUMN + " INT\n" +
                ");");

        db.execSQL("create table " + DATABASE_GENRES_TABLE + "(\n" +
                "    " + GENRES_COLUMN.BIG_IMAGE_LINK_COLUMN + " TEXT,\n" +
                "    " + GENRES_COLUMN.SMALL_IMAGE_LINK_COLUMN + " TEXT,\n" +
                "    " + GENRES_COLUMN.CODE_COLUMN + " TEXT,\n" +
                "    " + GENRES_COLUMN.NAME_COLUNM + " TEXT,\n" +
                "    " + GENRES_COLUMN.FULLNAME_COLUMN + " TEXT,\n" +
                "    " + GENRES_COLUMN.COLOR_COLUMN + " INT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static final class AUTHOR_COLUMN implements BaseColumns {
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

    public static final class GENRES_COLUMN implements BaseColumns {
        public static final String COLOR_COLUMN = "color";
        public static final String NAME_COLUNM = "name";
        public static final String CODE_COLUMN = "code";
        public static final String FULLNAME_COLUMN = "description";
        public static final String BIG_IMAGE_LINK_COLUMN = "bigimage";
        public static final String SMALL_IMAGE_LINK_COLUMN = "smallimage";
    }
}
