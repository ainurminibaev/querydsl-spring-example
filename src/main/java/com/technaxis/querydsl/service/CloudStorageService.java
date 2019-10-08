package com.technaxis.querydsl.service;

import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.technaxis.querydsl.model.enums.MediaFormat;
import com.technaxis.querydsl.settings.StorageSettings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobAccess;
import org.jclouds.blobstore.domain.BlobBuilder;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.blobstore.options.PutOptions;
import org.jclouds.http.Uris;
import org.jclouds.javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ainurminibaev on 07.02.17.
 */
@Slf4j
@Service
public class CloudStorageService {

    @Autowired
    private StorageSettings storageSettings;
    @Autowired
    private ImageService imageService;

    @Autowired
    @Qualifier("uploadBlobStore")
    private BlobStore blobStore;

    public String uploadAlternativeFile(byte[] data, String filePath,
                                        @Nullable Long idPostfix, MediaFormat mediaFormat) {
        String extension = FilenameUtils.getExtension(filePath);
        if (mediaFormat != null && mediaFormat.equals(MediaFormat.IMAGE_400)) {
            data = imageService.fitImageToSize(data, mediaFormat.getSize(), extension);
            if (data == null) {
                return null;
            }
            String folder = FilenameUtils.getPath(filePath);
            String filename = Files.getNameWithoutExtension(filePath);
            filename = mediaFormat.name().toLowerCase() + filename;
            return uploadFile(data, folder + filename + "." + extension, idPostfix, true);
        }
        return null;
    }

    public String uploadFile(byte[] data, String filePath, boolean publicAvailable) {
        return uploadFile(data, filePath, null, publicAvailable, true);
    }

    public String uploadFile(byte[] data, String filePath, @Nullable Long idPostfix, boolean publicAvailable) {
        return uploadFile(data, filePath, idPostfix, publicAvailable, true);
    }

    public String uploadFile(byte[] data, String filePath, boolean publicAvailable, boolean addPostfixToFileName) {
        return uploadFile(data, filePath, null, publicAvailable, addPostfixToFileName);
    }

    public String uploadFile(byte[] data, String filePath, @Nullable Long idPostfix,
                             boolean publicAvailable, boolean addPostfixToFileName) {
        filePath = formatFilePath(filePath, idPostfix, addPostfixToFileName);
        BlobBuilder.PayloadBlobBuilder blobBuilder = blobStore
                .blobBuilder(filePath)
                .payload(data)
                .contentLength(data.length);
        uploadFile(blobBuilder, publicAvailable);
        return filePath;
    }

    public String uploadFile(File file, String filePath, boolean publicAvailable, boolean addPostfixToFileName) {
        filePath = formatFilePath(filePath, null, addPostfixToFileName);
        BlobBuilder.PayloadBlobBuilder blobBuilder = blobStore
                .blobBuilder(filePath)
                .payload(file)
                .contentLength(file.length());
        uploadFile(blobBuilder, publicAvailable);
        return filePath;
    }

    private void uploadFile(BlobBuilder.PayloadBlobBuilder blobBuilder, boolean publicAvailable) {
        PutOptions options = PutOptions.NONE;
        if (publicAvailable) {
            blobBuilder.cacheControl("public, max-age=14400");
            options = new PutOptions(true);
            options.setBlobAccess(BlobAccess.PUBLIC_READ);
        }
        HashMap<String, String> userMetadata = new HashMap<>();
        userMetadata.put("Origin", "*");
        userMetadata.put("Method", "*");
        blobBuilder.userMetadata(userMetadata);
        Blob blob = blobBuilder.build();
        Multimap<String, String> allHeaders = blob.getAllHeaders();
        allHeaders.put("Origin", "*");
        allHeaders.put("Method", "*");
        blobStore.putBlob(storageSettings.getContainer(), blob, options);
    }

    private String formatFilePath(String filePath, @Nullable Long idPostfix, boolean addPostfixToFileName) {
        if (StringUtils.isBlank(filePath)) {
            return StringUtils.EMPTY;
        }
        String folder = FilenameUtils.getPath(filePath);
        String filename = Files.getNameWithoutExtension(filePath);
        String extension = FilenameUtils.getExtension(filePath);
        //filename = TransliterationService.transliterate(filename, CUT_FILENAME_LENGTH);

        Long postfixValue = idPostfix != null ? idPostfix : System.currentTimeMillis();
        String postfix = addPostfixToFileName ? "-" + postfixValue : "";

        return folder + filename + postfix + "." + extension;
    }

    public void deleteFileFromCloud(String filePath) {
        if (StringUtils.isNotEmpty(filePath)) {
            blobStore.removeBlob(storageSettings.getContainer(), filePath);
        }
    }

    public void deleteFilesFromCloud(Collection<String> filePaths) {
        if (CollectionUtils.isNotEmpty(filePaths)) {
            blobStore.removeBlobs(storageSettings.getContainer(), filePaths);
        }
    }

    public void deleteFilesFromCloudDirectory(String directoryPath) {
        if (StringUtils.isNotEmpty(directoryPath)) {
            List<String> filePaths = getFilesFromCloudDirectory(directoryPath);
            deleteFilesFromCloud(filePaths);
        }
    }

    public List<String> getFilesFromCloudDirectory(String directoryPath) {
        if (StringUtils.isBlank(directoryPath)) {
            return Collections.emptyList();
        }
        return blobStore.list(storageSettings.getContainer(), ListContainerOptions.Builder.inDirectory(directoryPath))
                .stream()
                .map(StorageMetadata::getName)
                .collect(Collectors.toList());
    }

    public String prepareSignedFileUrl(String blobName, boolean visibleForAll) {
        if (visibleForAll) {
            return Uris.uriBuilder(storageSettings.getEndpoint())
                    .appendPath(storageSettings.getContainer())
                    .appendPath(blobName)
                    .build()
                    .toString();
        }
        String url = blobStore
                .getContext()
                .getSigner()
                .signGetBlob(
                        storageSettings.getContainer(),
                        blobName,
                        1000//seconds
                )
                .getEndpoint()
                .toString();
        if (!url.startsWith("https")) {
            url = StringUtils.replace(url, "http", "https", 1);
        }
        return url;
    }
}
