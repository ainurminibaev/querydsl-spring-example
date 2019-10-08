package com.technaxis.querydsl.model.enums.sorting;

import java.util.function.Function;
import java.util.stream.Stream;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

/**
 * @author Dmitry Sadchikov
 */
public interface ISortType {

    ComparableExpressionBase[] getExpressions();

    default OrderSpecifier[] of(Order order) {
        Function<ComparableExpressionBase, OrderSpecifier> orderMapper = order == Order.DESC
                ? ComparableExpressionBase::desc
                : ComparableExpressionBase::asc;
        return Stream.of(getExpressions())
                .map(orderMapper)
                .map(OrderSpecifier::nullsLast)
                .toArray(OrderSpecifier[]::new);
    }
}
