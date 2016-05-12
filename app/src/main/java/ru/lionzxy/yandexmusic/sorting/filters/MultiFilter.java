package ru.lionzxy.yandexmusic.sorting.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.lionzxy.yandexmusic.interfaces.IAuthorSortingFilter;
import ru.lionzxy.yandexmusic.model.AuthorObject;

/**
 * Created by LionZXY on 08.05.2016.
 */
public class MultiFilter implements IAuthorSortingFilter {
    List<IAuthorSortingFilter> sortingFilters = new ArrayList<>();

    public MultiFilter(){}

    public MultiFilter(IAuthorSortingFilter... filters) {
        sortingFilters = Arrays.asList(filters);
    }

    public void addFilter(IAuthorSortingFilter filter) {
        sortingFilters.add(filter);
    }

    public void removeFilter(IAuthorSortingFilter filter) { sortingFilters.remove(filter); }

    @Override
    public boolean isAccept(AuthorObject authorObject) {
        for (IAuthorSortingFilter filter : sortingFilters)
            if (!filter.isAccept(authorObject))
                return false;

        return true;
    }
}
