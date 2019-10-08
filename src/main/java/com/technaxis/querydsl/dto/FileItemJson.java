package com.technaxis.querydsl.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.technaxis.querydsl.model.enums.FileItemType;
import lombok.Data;

import java.util.Date;

/**
 * Created by Vitaly on 13.02.2018.
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileItemJson {
    private Long id;
    private String url;
    private String previewUrl;
    private String name;
    private Date createdDate;
    private FileItemType fileItemType;
    private Long ownerId;
    private Integer order;
    private Long fileSize;
}
