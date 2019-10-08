package com.technaxis.querydsl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErrorJson {
    private String code;
    private String field;
}