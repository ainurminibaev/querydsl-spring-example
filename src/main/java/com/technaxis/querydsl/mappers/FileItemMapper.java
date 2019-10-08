package com.technaxis.querydsl.mappers;

import com.technaxis.querydsl.dto.FileItemJson;
import com.technaxis.querydsl.model.FileItem;
import com.technaxis.querydsl.model.enums.FileItemType;
import com.technaxis.querydsl.model.enums.MediaFormat;
import com.technaxis.querydsl.service.ContentService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Vitaly on 14.02.2018.
 */
@Component
public class FileItemMapper {

    @Autowired
    private ContentService contentService;

    @Nullable
    public FileItemJson apply(@Nullable FileItem fileItem) {
        if (fileItem == null) {
            return null;
        }
        return apply(fileItem, Collections.singletonMap(fileItem.getUrl(),
                contentService.getAlternativeContent(MediaFormat.IMAGE_400, fileItem.getUrl())));
    }

    public List<FileItemJson> apply(List<FileItem> fileItems) {
        if (CollectionUtils.isEmpty(fileItems)) {
            return Collections.emptyList();
        }
        List<String> imagesUrls = fileItems
                .stream()
                .filter(fileItem -> FileItemType.IMAGE.equals(fileItem.getFileItemType()))
                .map(FileItem::getUrl)
                .collect(Collectors.toList());
        Map<String, String> alternativeContentsByUrls = contentService.getAlternativeContent(MediaFormat.IMAGE_400, imagesUrls);
        return fileItems.stream().map(fileItem -> apply(fileItem, alternativeContentsByUrls)).collect(Collectors.toList());
    }

    private FileItemJson apply(FileItem fileItem, Map<String, String> alternativeContentsByUrls) {
        if (fileItem == null) {
            return null;
        }
        FileItemJson fileItemJson = new FileItemJson();
        fileItemJson.setFileItemType(fileItem.getFileItemType());
        fileItemJson.setId(fileItem.getId());
        Boolean visibility = fileItem.getVisibleForAll();
        fileItemJson.setUrl(FileItemType.URL.equals(fileItem.getFileItemType()) ? fileItem.getUrl() : contentService.getFileUrl(fileItem.getUrl(), visibility));
        if (FileItemType.IMAGE.equals(fileItem.getFileItemType())) {
            String alternativeContent = alternativeContentsByUrls.get(fileItem.getUrl());
            alternativeContent = alternativeContent == null ? fileItem.getUrl() : alternativeContent;
            fileItemJson.setPreviewUrl(contentService.getFileUrl(alternativeContent, visibility));
        }
        fileItemJson.setCreatedDate(Date.valueOf(fileItem.getCreatedDateTime().toLocalDate()));
        fileItemJson.setName(fileItem.getName());
        fileItemJson.setOwnerId(fileItem.getOwnerId());
        fileItemJson.setOrder(fileItem.getOrder());
        fileItemJson.setFileSize(fileItem.getFileSize());
        return fileItemJson;
    }
}
