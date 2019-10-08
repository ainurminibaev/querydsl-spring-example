package com.technaxis.querydsl.mappers;

import com.google.common.base.Function;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dmitry Sadchikov
 */
public abstract class JsonMapper<E, J> implements Function<E, J> {

    public List<J> apply(List<E> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }
        return items
                .stream()
                .map(this::apply)
                .collect(Collectors.toList());
    }
}
