package ru.lionzxy.yandexmusic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.lionzxy.yandexmusic.lists.author.AuthorObject;
import ru.lionzxy.yandexmusic.lists.genres.GenresObject;

/**
 * Created by LionZXY on 16.04.2016.
 * YandexMusic
 */
public class LoadingActivity {
    public static List<AuthorObject> authorObjects = new ArrayList<>();
    public static HashMap<String, Integer> genresHashMap = new HashMap<>();
    public static List<GenresObject> genresList = new ArrayList<>();

    static {
        genresList.add(GenresObject.UNKNOWN);
        genresHashMap.put("unkn", 0);
    }

}
