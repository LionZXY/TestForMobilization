package ru.lionzxy.yandexmusic.lists.genres;

import ru.lionzxy.yandexmusic.R;

/**
 * Created by nikit_000 on 14.04.2016.
 */
public class GenresObject {
    public static final GenresObject UNKNOWN = new GenresObject("Неизвестный жанр", "unknown", R.drawable.notfoundmusic);

    public int imageId;
    public String name, code;

    private GenresObject(String name, String code, int imageId) {
        this.name = name;
        this.code = code;
        this.imageId = imageId;
    }
}
