package com.technaxis.querydsl.dto.forms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.technaxis.querydsl.utils.HttpResponseStatus;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Min;

/**
 * @author Dmitry Sadchikov
 */
@Data
public class PageableForm {

    public static final Integer DEFAULT_PAGE = 1;
    public static final Integer DEFAULT_LIMIT = 20;

    @Min(value = 0, message = HttpResponseStatus.INVALID_PARAM)
    private Integer page = DEFAULT_PAGE;
    @Min(value = 0, message = HttpResponseStatus.INVALID_PARAM)
    private Integer limit = DEFAULT_LIMIT;

    public Integer getPage() {
        return Math.max(0, page - 1);
    }

    @JsonIgnore
    public Pageable getPageRequest() {
        return PageRequest.of(getPage(), getLimit());
    }
}

