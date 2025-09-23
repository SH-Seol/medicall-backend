package com.medicall.support;

import java.util.List;

public record CursorPageResult<T>(
        Long nextCursorId,
        List<T> data
) {
    public static <T> CursorPageResult<T> of(List<T> data, Long nextCursorId) {
        return new CursorPageResult<>(nextCursorId, data);
    }
}
