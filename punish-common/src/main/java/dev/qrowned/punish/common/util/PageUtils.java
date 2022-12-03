package dev.qrowned.punish.common.util;

import java.util.Collections;
import java.util.List;

public final class PageUtils {

    public static <T> List<T> getPaginatedList(List<T> source, int page) {
        return getPaginatedList(source, page, 5);
    }

    public static <T> List<T> getPaginatedList(List<T> source, int page, int entriesPerPage) {
        int fromIndex = (page - 1) * entriesPerPage;
        if (source.size() <= fromIndex) {
            return Collections.emptyList();
        }

        return source.subList(fromIndex, Math.min(fromIndex + entriesPerPage, source.size()));
    }
}
