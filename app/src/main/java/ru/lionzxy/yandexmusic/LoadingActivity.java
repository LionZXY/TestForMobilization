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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.lionzxy.yandexmusic.helper.DatabaseHelper;
import ru.lionzxy.yandexmusic.lists.author.AuthorObject;
import ru.lionzxy.yandexmusic.lists.genres.GenresObject;

/**
 * Created by LionZXY on 16.04.2016.
 * YandexMusic
 */
public class LoadingActivity extends AppCompatActivity {
    public static List<AuthorObject> authorObjects = new ArrayList<>();
    public static HashMap<String, GenresObject> genresHashMap = new HashMap<>();
    public static HashMap<Long, GenresObject> genresHashMapOnDBID = new HashMap<>();
    private DatabaseHelper mDatabaseHelper;
    private RoundCornerProgressBar progress;

    static {
        genresHashMap.put("unkn", GenresObject.UNKNOWN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        progress = (RoundCornerProgressBar) findViewById(R.id.progress_2);
        progress.setProgressColor(getResources().getColor(R.color.colorPrimary));
        progress.setProgressBackgroundColor(Color.parseColor("#BFBFBF"));
        progress.setSecondaryProgressColor(getResources().getColor(R.color.colorPrimaryDark));
        progress.setSecondaryProgress(480);
        progress.setMax(1000);
        progress.setProgress(400);
        mDatabaseHelper = new DatabaseHelper(this);

        final SharedPreferences sp = getSharedPreferences(getApplicationContext().getPackageName(), MODE_APPEND);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase sdb = mDatabaseHelper.getReadableDatabase();
                mHandler.obtainMessage(3, getResources().getString(R.string.load1)).sendToTarget();
                loadGenresFromDatabase(sdb);
                mHandler.obtainMessage(3, getResources().getString(R.string.load2)).sendToTarget();
                loadAuthorsFromDatabase(sdb);

                mHandler.obtainMessage(3,getResources().getString(R.string.load3)).sendToTarget();
                sdb = mDatabaseHelper.getWritableDatabase();
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) new URL("https://api.music.yandex.net/genres").openConnection();
                    urlConnection.connect();
                    int file_size = urlConnection.getContentLength();
                    downloadGenres(urlConnection, sdb);
                   /* if (!sp.contains("genressize")) {
                        sp.edit().putInt("genressize", file_size).apply();
                    } else {
                        if (sp.getInt("genressize", 0) != file_size) {

                        } else urlConnection.disconnect();
                    }*/
                } catch (Exception e) {
                    Log.e("Genres", "Error while connect to internet", e);
                }

                mHandler.obtainMessage(3,getResources().getString(R.string.load4)).sendToTarget();
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://download.cdn.yandex.net/mobilization-2016/artists.json").openConnection();
                    urlConnection.connect();
                    int file_size = urlConnection.getContentLength();
                    downloadAuthors(urlConnection, sdb);
                   /* if (!sp.contains("genressize")) {
                        sp.edit().putInt("genressize", file_size).apply();
                    } else {
                        if (sp.getInt("genressize", 0) != file_size) {

                        } else urlConnection.disconnect();
                    }*/
                } catch (Exception e) {
                    Log.e("Authors", "Error while connect to internet", e);
                }
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
                new GenresObject(cursor);
            } catch (Exception e) {
                Log.e("Genres", "Error while add from database", e);
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
                authorObjects.add(new AuthorObject(cursor));
            } catch (Exception e) {
                Log.e("Auhtors", "Error while add from database", e);
            }
        }
        cursor.close();
    }

    public void downloadGenres(final HttpURLConnection urlConnection, SQLiteDatabase sdb) {
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
                JSONObject object = arr.getJSONObject(i);
                if (genresHashMap.get(object.getString("id")) == null)
                    new GenresObject(object).putInDB(sdb);
                mHandler.obtainMessage(1, (int) ((i * 500) / arr.length())).sendToTarget();
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            Log.e("Genres", "Error while genres download", e);
        }
    }

    public void downloadAuthors(HttpURLConnection urlConnection, SQLiteDatabase sdb) {
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
                mHandler.obtainMessage(2, (int) ((this_progress * 500) / file_size) + 500).sendToTarget();
            }

            String str = new String(byteArrayOutputStream.toByteArray());

            JSONArray arr = new JSONArray(str);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                if (!isContains(object.getInt("id")))
                    authorObjects.add(new AuthorObject(object).putInDB(sdb));
                mHandler.obtainMessage(1, (int) ((i * 500) / arr.length()) + 500).sendToTarget();
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            Log.e("Authors", "Error while authors download", e);
        }
    }

    public static boolean isContains(int id) {
        for (int j = 0; j < authorObjects.size(); j++)
            if (authorObjects.get(j).authorId == id)
                return true;
        return false;
    }

}
