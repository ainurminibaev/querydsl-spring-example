package com.technaxis.querydsl.utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Dmitry Sadchikov
 */
public class StatementUtils {

    @SafeVarargs
    public static <T> void switchType(T entity, Consumer<T>... consumers) {
        Stream.of(consumers).forEach(c -> c.accept(entity));
    }

    public static <T> Consumer caseOf(Class<T> entityClass, Consumer<T> consumer) {
        return obj -> Optional.of(obj)
                .filter(entityClass::isInstance)
                .map(entityClass::cast)
                .ifPresent(consumer);
    }
}
