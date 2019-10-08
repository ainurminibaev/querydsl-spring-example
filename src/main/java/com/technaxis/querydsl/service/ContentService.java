package com.technaxis.querydsl.service;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.technaxis.querydsl.dto.forms.queries.FileItemUploadQueryForm;
import com.technaxis.querydsl.exceptions.NotFoundException;
import com.technaxis.querydsl.exceptions.ValidationException;
import com.technaxis.querydsl.exceptions.messages.ContentMessages;
import com.technaxis.querydsl.model.AlternativeMediaFormat;
import com.technaxis.querydsl.model.FileItem;
import com.technaxis.querydsl.model.enums.FileItemType;
import com.technaxis.querydsl.model.enums.MediaFormat;
import com.technaxis.querydsl.model.enums.TechnaxisProjectModule;
import com.technaxis.querydsl.repositories.AlternativeMediaFormatRepository;
import com.technaxis.querydsl.repositories.FileItemRepository;
import com.technaxis.querydsl.utils.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContentService {

    private static Detector detector = new DefaultDetector(MimeTypes.getDefaultMimeTypes());
    private static TikaConfig tikaConfig = TikaConfig.getDefaultConfig();
    @Autowired
    private CloudStorageService cloudStorageService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private FileItemRepository fileItemRepository;
    @Autowired
    private AlternativeMediaFormatRepository alternativeMediaFormatRepository;

    public byte[] getDataFromMultipartFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        byte[] data;
        try {
            data = file.getBytes();
        } catch (Exception e) {
            log.warn("Failed to get image from file", e);
            return null;
        }

        return data;
    }

    public boolean isXls(byte[] data) {
        MediaType contentType = getContentType(data);
        return StringUtils.equalsIgnoreCase("application/x-tika-ooxml", contentType.getType() + "/" + contentType.getSubtype());
    }

    public boolean isImage(byte[] data) {
        MediaType contentType = getContentType(data);
        return StringUtils.equalsIgnoreCase("image", contentType.getType());
    }

    public boolean isVideo(byte[] data) {
        MediaType contentType = getContentType(data);
        return StringUtils.equalsIgnoreCase("video", contentType.getType());
    }

    private MediaType getContentType(byte[] data) {
        long startTime = System.currentTimeMillis();
        MediaType contentType = null;
        try {
            contentType = detector.detect(TikaInputStream.get(data), new Metadata());
        } catch (Exception e) {
            log.info("Failed to probe content type of byte array", e);
        }
        log.debug("Content type [{}] detection for byte[] of size [{}] took [{}] ms", contentType, data.length, System.currentTimeMillis() - startTime);
        return contentType;
    }

    public String getExtension(byte[] data) {
        MediaType contentType = getContentType(data);
        try {
            MimeType mimeType = tikaConfig.getMimeRepository().forName(contentType.toString());
            if (mimeType != null) {
                return mimeType.getExtension();
            }
        } catch (Exception e) {
            log.info(String.format("Failed to get extension for content type [%s]", contentType), e);
        }
        return null;
    }

    public FileItem uploadFile(FileItemUploadQueryForm form, @Nullable Long userId) {
        if (form.getFile() == null || form.getFile().isEmpty()) {
            throw new ValidationException(HttpResponseStatus.EMPTY_PARAM, FileItemUploadQueryForm.FILE_FIELD);
        }
        if (form.getModule() == null) {
            throw new ValidationException(HttpResponseStatus.EMPTY_PARAM, FileItemUploadQueryForm.MODULE_FIELD);
        }
        byte[] data = getDataFromMultipartFile(form.getFile());
        String realName = form.getFile().getOriginalFilename();
        FileItemType fileItemType = FileItemType.FILE;
        String fileExtension = Files.getFileExtension(realName);
        if (isImage(data)) {
            data = imageService.rotateExif(data, fileExtension);
            fileItemType = FileItemType.IMAGE;
        } else if (isVideo(data)) {
            fileItemType = FileItemType.VIDEO;
        }

        final String urlFileName = Strings.isNullOrEmpty(form.getEntityName()) ?
                realName :
                (form.getEntityName() +
                        (Strings.isNullOrEmpty(fileExtension)
                                ? ""
                                : "." + fileExtension));

        String filename;
        if (userId != null) {
            filename = String.format(
                    "user/%d/%s/%s",
                    userId,
                    form.getModule().getModuleName(),
                    urlFileName
            );
        } else {
            filename = String.format(
                    "%s/%s",
                    form.getModule().getModuleName(),
                    urlFileName
            );
        }
        if (form.getModule() == TechnaxisProjectModule.PROFILE && fileItemType == FileItemType.IMAGE) {
            byte[] resizedImageData = imageService.fitImageToSize(data, 500, fileExtension);
            data = resizedImageData != null ? resizedImageData : data;
        }
        FileItem fileItem = new FileItem()
                .setFileItemType(fileItemType)
                .setName(realName)
                .setOwnerId(userId)
                .setVisibleForAll(true)
                .setFileSize(form.getFile().getSize());
        fileItemRepository.save(fileItem);

        String fileUrl = cloudStorageService.uploadFile(data, filename, fileItem.getId(), true);
        fileItem.setUrl(fileUrl);

        if (fileItemType == FileItemType.IMAGE) {
            String alternativeFilePath =
                    cloudStorageService.uploadAlternativeFile(data, filename, fileItem.getId(), MediaFormat.IMAGE_400);
            if (alternativeFilePath != null) {
                saveAlternativeVersion(MediaFormat.IMAGE_400, fileUrl, alternativeFilePath);
            }
        }
        return fileItemRepository.save(fileItem);
    }

    public FileItem getFile(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(ContentMessages.EMPTY_FILE_ID);
        }
        return fileItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ContentMessages.FILE_NOT_FOUND));
    }

    public List<FileItem> getFiles(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return fileItemRepository.findAllById(ids);
    }

    public String getFileUrl(String path, Boolean visibleForAll) {
        if (path == null) {
            return null;
        }
        try {
            return cloudStorageService.prepareSignedFileUrl(path, visibleForAll);
        } catch (Exception e) {
            log.error("Error on file " + path, e);
            return null;
        }
    }

    public void saveAlternativeVersion(MediaFormat mediaFormat, String originPath, String convertedPath) {
        Optional<AlternativeMediaFormat> savedAlternative = alternativeMediaFormatRepository
                .findByOriginPathAndMediaFormat(originPath, mediaFormat);
        if (savedAlternative.isPresent()) {
            //TODO find out why there was duplicates
            return;
        }
        AlternativeMediaFormat alternative = new AlternativeMediaFormat();
        alternative.setConvertedPath(convertedPath);
        alternative.setMediaFormat(mediaFormat);
        alternative.setOriginPath(originPath);
        alternativeMediaFormatRepository.save(alternative);
    }

    public List<AlternativeMediaFormat> getAlternativeContent(String originPath) {
        if (StringUtils.isEmpty(originPath)) {
            return Collections.emptyList();
        }
        return alternativeMediaFormatRepository.findByOriginPath(originPath);
    }

    public String getAlternativeContent(MediaFormat mediaFormat, String originPath) {
        return alternativeMediaFormatRepository
                .findByOriginPathAndMediaFormat(originPath, mediaFormat)
                .map(AlternativeMediaFormat::getConvertedPath)
                .orElse(originPath);
    }

    public Map<String, String> getAlternativeContent(MediaFormat mediaFormat, List<String> originPaths) {
        if (mediaFormat == null || CollectionUtils.isEmpty(originPaths)) {
            return Collections.emptyMap();
        }
        return alternativeMediaFormatRepository.findByOriginPathInAndMediaFormat(originPaths, mediaFormat)
                .stream()
                .collect(Collectors.toMap(AlternativeMediaFormat::getOriginPath, AlternativeMediaFormat::getConvertedPath));
    }

    public List<AlternativeMediaFormat> getAlternativeContents(List<String> originPaths) {
        if (CollectionUtils.isEmpty(originPaths)) {
            return Collections.emptyList();
        }
        return alternativeMediaFormatRepository.findByOriginPathIn(originPaths);
    }

    public void deleteAlternativeContents(List<AlternativeMediaFormat> alternativeMediaFormats) {
        if (CollectionUtils.isNotEmpty(alternativeMediaFormats)) {
            alternativeMediaFormatRepository.deleteAll(alternativeMediaFormats);
        }
    }
}
