package com.technaxis.querydsl.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dmitry Sadchikov
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.jpa.schema-generation")
public class SchemaGenerationSettings {
    private String ddlLocation;
}

