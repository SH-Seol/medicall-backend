package com.medicall.support;

import java.util.List;
import java.util.function.Function;

public final class CorePageUtils {public static final int DEFAULT_PAGE_SIZE = 15;
    public static final int MAX_PAGE_SIZE = 100;

    private CorePageUtils() {}

    public static int normalizeSize(Integer size) {
        if (size == null || size <= 0) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(size, MAX_PAGE_SIZE);
    }

    public static void validateCursorId(Long cursorId) {
        if (cursorId != null && cursorId <= 0) {
            throw new IllegalArgumentException("커서 ID는 1 이상이어야 합니다.");
        }
    }

    public static boolean hasCursor(Long cursorId) {
        return cursorId != null;
    }

    public static boolean hasStatusFilter(String status) {
        return status != null && !status.trim().isEmpty();
    }

    public static boolean hasKeywordFilter(String keyword) {
        return keyword != null && !keyword.trim().isEmpty();
    }

    public static <T> CursorPageResult<T> buildCursorResult(List<T> list, int size, Function<T, Long> idExtractor) {
        Long nextCursorId = null;

        if(list.size() > size){
            list = list.subList(0, size);
            nextCursorId = idExtractor.apply(list.get(list.size() - 1));
        }

        return CursorPageResult.of(list, nextCursorId);
    }
}

