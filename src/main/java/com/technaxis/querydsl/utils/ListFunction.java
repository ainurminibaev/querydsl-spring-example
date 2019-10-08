package com.technaxis.querydsl.utils;

import java.util.List;
import java.util.function.Function;

/**
 * @author Dmitry Sadchikov
 */
public interface ListFunction<T, R> extends Function<List<T>, List<R>> {
}
