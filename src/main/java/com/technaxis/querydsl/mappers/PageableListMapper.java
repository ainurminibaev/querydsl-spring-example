package com.technaxis.querydsl.mappers;

import com.technaxis.querydsl.dto.PageableListDto;
import com.technaxis.querydsl.utils.ListFunction;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Dmitry Sadchikov
 */
@Component
public class PageableListMapper<E, D> {

    public PageableListDto<D> apply(Page<E> page, ListFunction<E, D> listMapper) {
        PageableListDto<D> dto = new PageableListDto<>();
        dto.setData(listMapper.apply(page.getContent()));
        dto.setPage(page);
        return dto;
    }

    public PageableListDto<D> simpleApply(Page<E> page, Function<E, D> mapper) {
        PageableListDto<D> dto = new PageableListDto<>();
        dto.setData(
                page.getContent()
                        .stream()
                        .map(mapper)
                        .collect(Collectors.toList())
        );
        dto.setPage(page);
        return dto;
    }
}
