package com.technaxis.querydsl.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Dmitry Sadchikov
 */
public final class JpaUtils {

    public static <T> Page<T> getPageFromList(List<T> list, Pageable pageable) {
        Pair<Integer, Integer> bounds = getSublistBounds(list.size(), pageable.getPageNumber(), pageable.getPageSize());
        List<T> result = list.subList(bounds.getLeft(), bounds.getRight());
        return new PageImpl<>(result, pageable, list.size());
    }

    private static Pair<Integer, Integer> getSublistBounds(Integer listSize, Integer page, Integer limit) {
        int leftBound = page * limit;
        leftBound = leftBound < 0 ? 0 : leftBound;
        if (leftBound > listSize) {
            return Pair.of(listSize, listSize);
        }
        int rightBound = leftBound + limit;
        rightBound = rightBound > leftBound && rightBound <= listSize ? rightBound : listSize;
        return Pair.of(leftBound, rightBound);
    }
}
