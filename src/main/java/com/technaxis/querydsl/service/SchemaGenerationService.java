package com.technaxis.querydsl.service;

import com.technaxis.querydsl.settings.SchemaGenerationSettings;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.Metadata;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.hbm2ddl.SchemaValidator;
import org.hibernate.tool.schema.TargetType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.MessageFormat;
import java.util.EnumSet;

/**
 * @author Dmitry Sadchikov
 */
@Service
@RequiredArgsConstructor
public class SchemaGenerationService {

    private final Metadata metadata;
    private final ServiceRegistry serviceRegistry;
    private final SchemaGenerationSettings schemaGenerationSettings;

    public void generate() {
        var ddlFile = new File(schemaGenerationSettings.getDdlLocation());
        deleteFileIfExists(ddlFile);
        new SchemaUpdate()
                .setDelimiter(";")
                .setFormat(false)
                .setOutputFile(ddlFile.getAbsolutePath())
                .execute(EnumSet.of(TargetType.SCRIPT), metadata, serviceRegistry);
    }

    public void validate() {
        new SchemaValidator()
                .validate(metadata, serviceRegistry);
    }

    private void deleteFileIfExists(File ddlFile) {
        if (ddlFile.exists()) {
            if (!ddlFile.isFile()) {
                var msg = MessageFormat.format("File is not a normal file {0}", ddlFile);
                throw new IllegalStateException(msg);
            }
            if (!ddlFile.delete()) {
                var msg = MessageFormat.format("Unable to delete file {0}", ddlFile);
                throw new IllegalStateException(msg);
            }
        }
    }
}

