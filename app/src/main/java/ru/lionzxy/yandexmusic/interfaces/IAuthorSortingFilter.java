package ru.lionzxy.yandexmusic.interfaces;

import ru.lionzxy.yandexmusic.model.AuthorObject;

/**
 * Created by Никита on 07.05.2016.
 */
public interface IAuthorSortingFilter {
    boolean isAccept(AuthorObject authorObject);
}
