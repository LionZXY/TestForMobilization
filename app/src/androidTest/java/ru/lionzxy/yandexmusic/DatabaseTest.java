package ru.lionzxy.yandexmusic;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.LargeTest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ru.lionzxy.yandexmusic.model.AuthorObject;
import ru.lionzxy.yandexmusic.model.GenresObject;
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

    public void testWritingDatabase() throws Exception{
        while (!((MusicList) getActivity()).ready) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        LoadingActivity.testForNullHashMap = new HashMap<>();

        LoadingActivity.authorObjects = new ArrayList<>();
        LoadingActivity.genresHashMap = new HashMap<>();

        LoadingActivity.addAuthorFromJsonObject(new JSONObject(jsonAuthor),database);
        LoadingActivity.addGenreFromJsonObject(new JSONObject(jsonGenres),database);
        LoadingActivity.addAuthorFromJsonObject(new JSONObject(jsonAuthor),database);
        LoadingActivity.addGenreFromJsonObject(new JSONObject(jsonGenres),database);

        assertEquals(1,LoadingActivity.authorObjects.size());
        assertEquals(2,LoadingActivity.genresHashMap.size());

    }

    public void testReadingDatabase() {
        while (!((MusicList) getActivity()).ready) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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

    public static final String jsonAuthor = "\n" +
            "{\n" +
            "\"id\":1080505,\n" +
            "\"name\":\"Tove Lo\",\n" +
            "\"genres\":[\n" +
            "\"pop\",\n" +
            "\"dance\",\n" +
            "\"electronics\"\n" +
            "],\n" +
            "\"tracks\":81,\n" +
            "\"albums\":22,\n" +
            "\"link\":\"http://www.tove-lo.com/\",\n" +
            "\"description\":\"шведская певица и автор песен. Она привлекла к себе внимание в 2013 году с выпуском сингла «Habits», но настоящего успеха добилась с ремиксом хип-хоп продюсера Hippie Sabotage на эту песню, который получил название «Stay High». 4 марта 2014 года вышел её дебютный мини-альбом Truth Serum, а 24 сентября этого же года дебютный студийный альбом Queen of the Clouds. Туве Лу является автором песен таких артистов, как Icona Pop, Girls Aloud и Шер Ллойд.\",\n" +
            "\"cover\":{\n" +
            "\"small\":\"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300\",\n" +
            "\"big\":\"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/1000x1000\"\n" +
            "}\n" +
            "}";

    public static final String jsonGenres="{\n" +
            "\"id\":\"pop\",\n" +
            "\"weight\":1,\n" +
            "\"composerTop\":false,\n" +
            "\"urlPart\":\"pop\",\n" +
            "\"tracksCount\":2537886,\n" +
            "\"title\":\"Поп\",\n" +
            "\"titles\":{\n" +
            "\"ru\":{\n" +
            "\"title\":\"Поп\"\n" +
            "},\n" +
            "\"kk\":{\n" +
            "\"title\":\"Поп\"\n" +
            "},\n" +
            "\"be\":{\n" +
            "\"title\":\"Поп\"\n" +
            "},\n" +
            "\"en\":{\n" +
            "\"title\":\"Pop\"\n" +
            "},\n" +
            "\"uk\":{\n" +
            "\"title\":\"Поп\"\n" +
            "}\n" +
            "},\n" +
            "\"color\":\"#ff6665\",\n" +
            "\"images\":{\n" +
            "\"208x208\":\"http://avatars.mds.yandex.net/get-music-misc/28052/metagenre-pop-x208/orig\",\n" +
            "\"300x300\":\"http://avatars.mds.yandex.net/get-music-misc/28052/metagenre-pop-x300/orig\"\n" +
            "},\n" +
            "\"showInMenu\":true,\n" +
            "\"radioIcon\":{\n" +
            "\"backgroundColor\":\"#ff6665\",\n" +
            "\"imageUrl\":\"avatars.yandex.net/get-music-content/rotor-genre-pop-icon/%%\"\n" +
            "},\n" +
            "\"subGenres\":[\n" +
            "{\n" +
            "\"id\":\"disco\",\n" +
            "\"weight\":1,\n" +
            "\"composerTop\":false,\n" +
            "\"urlPart\":\"disco\",\n" +
            "\"tracksCount\":7839,\n" +
            "\"title\":\"Диско\",\n" +
            "\"titles\":{\n" +
            "\"ru\":{\n" +
            "\"title\":\"Диско\"\n" +
            "},\n" +
            "\"kk\":{\n" +
            "\"title\":\"Disco\"\n" +
            "},\n" +
            "\"be\":{\n" +
            "\"title\":\"Disco\"\n" +
            "},\n" +
            "\"en\":{\n" +
            "\"title\":\"Disco\"\n" +
            "},\n" +
            "\"uk\":{\n" +
            "\"title\":\"Диско\"\n" +
            "}\n" +
            "},\n" +
            "\"color\":\"#ff6665\",\n" +
            "\"images\":{\n" +
            "},\n" +
            "\"showInMenu\":true,\n" +
            "\"radioIcon\":{\n" +
            "\"backgroundColor\":\"#ff6665\",\n" +
            "\"imageUrl\":\"avatars.yandex.net/get-music-content/rotor-genre-pop-icon/%%\"\n" +
            "}\n" +
            "}\n" +
            "]\n" +
            "}";

}
