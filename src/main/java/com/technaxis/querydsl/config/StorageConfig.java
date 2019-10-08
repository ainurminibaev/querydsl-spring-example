package com.technaxis.querydsl.config;

import com.technaxis.querydsl.settings.StorageSettings;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Optional;

/**
 * Created by ainurminibaev on 30.09.16.
 */
@Configuration
public class StorageConfig {

    @Autowired
    private StorageSettings storageSettings;

    @Lazy
    @Bean
    public BlobStore uploadBlobStore() {
        ContextBuilder credentials = ContextBuilder
                .newBuilder(storageSettings.getProvider())
                .credentials(storageSettings.getIdentity(), storageSettings.getCredential());
        Optional.ofNullable(storageSettings.getEndpoint())
                .ifPresent(credentials::endpoint);
        return credentials
                .buildView(BlobStoreContext.class)
                .getBlobStore();

    }
}
