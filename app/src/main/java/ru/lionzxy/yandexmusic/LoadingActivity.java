package ru.lionzxy.yandexmusic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.lionzxy.yandexmusic.collections.recyclerviews.elements.AuthorObject;
import ru.lionzxy.yandexmusic.collections.recyclerviews.elements.GenresObject;
import ru.lionzxy.yandexmusic.exceptions.ContextDialogException;
import ru.lionzxy.yandexmusic.helper.DatabaseHelper;
import ru.lionzxy.yandexmusic.io.ImageResource;

/**
 * Created by LionZXY on 16.04.2016.
 * YandexMusic
 */
public class LoadingActivity extends AppCompatActivity {
    private static final String TAG = "YaMobLoading";
    public static final String SHAREDTAG = "YandexMobil";
    public static List<AuthorObject> authorObjects = new ArrayList<>();
    public static HashMap<String, GenresObject> genresHashMap = new HashMap<>();
    public static HashMap<Long, GenresObject> genresHashMapOnDBID = new HashMap<>();
    public static DatabaseHelper databaseHelper = null;

    private HashMap<Integer, AuthorObject> testForNullHashMap = new HashMap<>();
    public SQLiteDatabase databaseSql;
    private RoundCornerProgressBar progress;

    static {
        genresHashMap.put("unkn", GenresObject.UNKNOWN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        ImageResource.imageLoader.init(ImageLoaderConfiguration.createDefault(LoadingActivity.this));

        progress = (RoundCornerProgressBar) findViewById(R.id.progressBar);
        progress.setProgressColor(getResources().getColor(R.color.colorPrimary));
        progress.setProgressBackgroundColor(Color.parseColor("#BFBFBF"));
        progress.setSecondaryProgressColor(getResources().getColor(R.color.colorPrimaryDark));
        progress.setMax(1000);
        databaseHelper = new DatabaseHelper(this);

        final SharedPreferences sp = getSharedPreferences(LoadingActivity.this.getPackageName(), MODE_APPEND);

        new Thread(new Runnable() {
            @Override
            public void run() {
                databaseSql = databaseHelper.getReadableDatabase();
                mHandler.obtainMessage(3, getResources().getString(R.string.load1)).sendToTarget();
                loadGenresFromDatabase(databaseSql);
                mHandler.obtainMessage(3, getResources().getString(R.string.load2)).sendToTarget();
                loadAuthorsFromDatabase(databaseSql);

                mHandler.obtainMessage(3, getResources().getString(R.string.load3)).sendToTarget();
                databaseSql = databaseHelper.getWritableDatabase();
                try {
                    //Yandex.Music don't support file size
                    HttpURLConnection urlConnection = (HttpURLConnection) new URL("https://api.music.yandex.net/genres").openConnection();
                    urlConnection.connect();
                    downloadGenres(urlConnection);
                } catch (Exception e) {
                    Log.e("Genres", "Error while connect to internet", e);
                }

                mHandler.obtainMessage(3, getResources().getString(R.string.load4)).sendToTarget();
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://download.cdn.yandex.net/mobilization-2016/artists.json").openConnection();
                    urlConnection.connect();
                    int file_size = urlConnection.getContentLength();

                    //Check for update. If i have access to server, i use MD5 or version in start file for check update
                    if (sp.contains("auhtorsize")) {
                        if (sp.getInt("auhtorsize", 0) == file_size) {
                            urlConnection.disconnect();
                        } else {
                            downloadAuthors(urlConnection, file_size);
                            sp.edit().putInt("auhtorsize", file_size).apply();
                        }
                    } else {
                        downloadAuthors(urlConnection, file_size);
                        sp.edit().putInt("auhtorsize", file_size).apply();
                    }

                    urlConnection.disconnect();
                } catch (Exception e) {
                    Log.e(TAG, "Error while connect to internet", e);
                }

                databaseSql.close();
                testForNullHashMap = null;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplication(), MusicList.class);
                        startActivity(intent);
                    }
                });
            }
        }).start();

    }


    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message != null)
                switch (message.what) {
                    case 1: {
                        progress.setProgress((int) message.obj);
                        break;
                    }
                    case 2: {
                        progress.setSecondaryProgress((int) message.obj);
                        break;
                    }
                    case 3: {
                        ((TextView) findViewById(R.id.progress)).setText((String) message.obj);
                        break;
                    }
                }

        }
    };

    public void loadGenresFromDatabase(SQLiteDatabase sdb) {
        String query = "SELECT rowid, * FROM " + DatabaseHelper.DATABASE_GENRES_TABLE;
        Cursor cursor = sdb.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            try {
                GenresObject genresObject = new GenresObject(cursor);
                genresHashMap.put(genresObject.code, genresObject);
                genresHashMapOnDBID.put(genresObject.idInDB, genresObject);
            } catch (Exception e) {
                Log.e(TAG, "Error while add from database", e);
                new ContextDialogException(LoadingActivity.this, e, R.string.error_data_load);
            }
        }
        cursor.close();
    }

    public void loadAuthorsFromDatabase(SQLiteDatabase sdb) {
        String query = "SELECT rowid, * FROM " + DatabaseHelper.DATABASE_AUTHOR_TABLE;
        Cursor cursor = sdb.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            try {
                AuthorObject authorObject = new AuthorObject(cursor);
                authorObjects.add(authorObject);
                testForNullHashMap.put(authorObject.authorId, authorObject);
            } catch (Exception e) {
                Log.e(TAG, "Error while add from database", e);
            }
        }
        cursor.close();
    }

    public void downloadGenres(final HttpURLConnection urlConnection) {
        try {
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("GET");

            int file_size = urlConnection.getContentLength();
            int this_progress = 0;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            InputStream is = urlConnection.getInputStream();
            byte buffer[] = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = is.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                this_progress += bytesRead;
                mHandler.obtainMessage(2, (int) ((this_progress * 500) / file_size)).sendToTarget();
            }

            String str = new String(byteArrayOutputStream.toByteArray());

            JSONArray arr = new JSONObject(str).getJSONArray("result");
            for (int i = 0; i < arr.length(); i++) {
                addGenreFromJsonObject(arr.getJSONObject(i));
                mHandler.obtainMessage(1, (int) ((i * 500) / arr.length())).sendToTarget();
            }

            urlConnection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error while genres download", e);
            new ContextDialogException(LoadingActivity.this, e, R.string.error_data_load);
        }
    }

    public void downloadAuthors(HttpURLConnection urlConnection, long file_size) {
        try {
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("GET");

            int this_progress = 0;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            InputStream is = urlConnection.getInputStream();
            byte buffer[] = new byte[1024];
            int bytesRead = -1;

            while ((bytesRead = is.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                this_progress += bytesRead;
                mHandler.obtainMessage(2, (int) ((this_progress * 500) / file_size) + 500).sendToTarget();
            }

            String str = new String(byteArrayOutputStream.toByteArray());

            JSONArray arr = new JSONArray(str);
            for (int i = 0; i < arr.length(); i++) {
                addAuthorFromJsonObject(arr.getJSONObject(i));
                mHandler.obtainMessage(1, (int) ((i * 500) / arr.length()) + 500).sendToTarget();
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error while authors download", e);
        }
    }

    public boolean addAuthorFromJsonObject(JSONObject jsonObject) {
        try {
            if (jsonObject == null)
                return false;

            AuthorObject authorObject = new AuthorObject(jsonObject);
            if (testForNullHashMap.get(authorObject.authorId) == null) {
                testForNullHashMap.put(authorObject.authorId, authorObject);
                authorObjects.add(authorObject);
                authorObject.putInDB(databaseSql);
                return true;
            } else return false;
        } catch (Exception e) {
            Log.e(TAG, "Error on parse author from json object", e);
            return false;
        }
    }

    public boolean addGenreFromJsonObject(JSONObject jsonObject) {
        try {
            if (jsonObject == null)
                return false;

            if (jsonObject.has("subGenres")) {
                JSONArray array = jsonObject.getJSONArray("subGenres");
                for (int i = 0; i < array.length(); i++)
                    addGenreFromJsonObject(array.getJSONObject(i));
            }

            GenresObject genresObject = new GenresObject(jsonObject);
            if (genresHashMap.get(genresObject.code) == null) {
                genresHashMap.put(genresObject.code, genresObject);
                genresObject.putInDB(databaseSql);
                return true;
            } else return false;
        } catch (Exception e) {
            Log.e(TAG, "Error on parse genre from json object", e);
            return false;
        }
    }

}
