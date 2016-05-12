package ru.lionzxy.yandexmusic.sorting.filters;

import ru.lionzxy.yandexmusic.interfaces.IAuthorSortingFilter;
import ru.lionzxy.yandexmusic.model.AuthorObject;

/**
 * Created by LionZXY on 08.05.2016.
 */
public class Filters {

    public static final IAuthorSortingFilter onlyWithSite = new IAuthorSortingFilter() {
        @Override
        public boolean isAccept(AuthorObject authorObject) {
            return authorObject.link != null;
        }
    };

    public static final IAuthorSortingFilter onlyWithBigPicture = new IAuthorSortingFilter() {
        @Override
        public boolean isAccept(AuthorObject authorObject) {
            return authorObject.bigImage != null;
        }
    };
}
