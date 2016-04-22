package ru.lionzxy.yandexmusic;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ru.lionzxy.yandexmusic.collections.recyclerviews.elements.AuthorObject;
import ru.lionzxy.yandexmusic.collections.recyclerviews.elements.GenresObject;
import ru.lionzxy.yandexmusic.helper.DatabaseHelper;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
@LargeTest
public class DatabaseTest extends ActivityInstrumentationTestCase2 {

    DatabaseHelper databaseHelper;
    List<AuthorObject> authorObjects = new ArrayList<>();
    List<GenresObject> genresObjects = new ArrayList<>();

    public DatabaseTest() {
        super(MusicList.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        databaseHelper = new DatabaseHelper(new RenamingDelegatingContext(getActivity().getApplicationContext(), "test_"));
    }

    public void testReadingDatabase() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String genres = "SELECT rowid, * FROM " + DatabaseHelper.DATABASE_GENRES_TABLE;
        String authors = "SELECT rowid, * FROM " + DatabaseHelper.DATABASE_AUTHOR_TABLE;

        String authors_column[] = new String[]{DatabaseHelper.AUTHOR_COLUMN.BIG_IMAGE_LINK_COLUMN
                , DatabaseHelper.AUTHOR_COLUMN.SMALL_IMAGE_LINK_COLUMN
                , DatabaseHelper.AUTHOR_COLUMN.GENRES_INT_ARR_COLUMN
                , DatabaseHelper.AUTHOR_COLUMN.NAME_COLUNM
                , DatabaseHelper.AUTHOR_COLUMN.DESCRIPTION_COLUMN
                , DatabaseHelper.AUTHOR_COLUMN.LINK_COLUMN
                , DatabaseHelper.AUTHOR_COLUMN.AUTHOR_ID_COLUMN
                , DatabaseHelper.AUTHOR_COLUMN.TRACKS_INT_COLUMN
                , DatabaseHelper.AUTHOR_COLUMN.ALBUMS_INT_COLUMN};

        String genres_column[] = new String[]{DatabaseHelper.GENRES_COLUMN.COLOR_COLUMN
                , DatabaseHelper.GENRES_COLUMN.NAME_COLUNM
                , DatabaseHelper.GENRES_COLUMN.CODE_COLUMN
                , DatabaseHelper.GENRES_COLUMN.FULLNAME_COLUMN
                , DatabaseHelper.GENRES_COLUMN.BIG_IMAGE_LINK_COLUMN
                , DatabaseHelper.GENRES_COLUMN.SMALL_IMAGE_LINK_COLUMN};

        Cursor cursor = database.rawQuery(genres, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            List<String> column = Arrays.asList(cursor.getColumnNames());
            assertEquals(column.size(), genres_column.length + 1);
            for (String colum : genres_column)
                assertTrue(column.contains(colum));
            genresObjects.add(new GenresObject(cursor));
        }
        cursor.close();

        cursor = database.rawQuery(authors, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            List<String> column = Arrays.asList(cursor.getColumnNames());
            assertEquals(column.size(), authors_column.length + 1);
            for (String colum : authors_column)
                assertTrue(column.contains(colum));
            authorObjects.add(new AuthorObject(cursor));
        }
        cursor.close();
    }

}
