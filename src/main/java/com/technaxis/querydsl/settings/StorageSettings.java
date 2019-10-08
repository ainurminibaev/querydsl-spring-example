package com.technaxis.querydsl.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties(prefix = "project-name.storage")
public class StorageSettings {

    @NotNull(message = "Provider name not specified")
    private String provider;

    @NotNull(message = "Identity (Access Key ID / Client ID) not specified")
    private String identity;

    @NotNull(message = "Credential (Secret Access Key / Private Key) not specified")
    private String credential;

    @NotNull(message = "Endpoint not specified")
    private String endpoint;

    @NotNull(message = "Container / Bucket name not specified")
    private String container;
}
