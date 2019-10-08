package com.technaxis.querydsl.service;

import com.technaxis.querydsl.settings.FlywaySettings;
import com.technaxis.querydsl.settings.SchemaGenerationSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Dmitry Sadchikov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlywayService implements InitializingBean {

    private static final String AUTO_MIGRATION_NAME = "auto-migration";
    private static final String CUSTOM_SCRIPTS_PATH = "src/main/resources/db/custom_scripts.sql";

    private final Flyway flyway;
    private final SchemaGenerationService schemaGenerationService;
    private final FlywaySettings flywaySettings;
    private final SchemaGenerationSettings schemaGenerationSettings;

    @Override
    public void afterPropertiesSet() {
        try {
            migrate();
            if (flywaySettings.isGenerationEnabled()) {
                schemaGenerationService.generate();
                File ddlFile = new File(schemaGenerationSettings.getDdlLocation());
                if (ddlFile.exists() && ddlFile.length() != 0) {
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
                    String fileName = String.format("V%s__%s.sql", timestamp, AUTO_MIGRATION_NAME);
                    Path migrationPath = Paths.get(flywaySettings.getLocation(), fileName);
                    Files.createDirectories(migrationPath.getParent());
                    Path ddlPath = ddlFile.toPath();
                    log.info("Moving file {} to {}", ddlPath.toString(), migrationPath.toString());
                    Files.move(ddlPath, migrationPath);
                    log.info("Successfully moved");
                } else {
                    log.info("DDL file is empty");
                }
            } else {
                log.info("DDL generation disabled");
            }
            createMigrationFilesFromCustomScripts();
            migrate();
            schemaGenerationService.validate();
        } catch (Exception e) {
            log.error("Migration error. Cause: ", e);
        }
    }

    private void migrate() {
        log.info("Starting flyway migration");
        flyway.migrate();
        log.info("Migration completed successfully");
    }

    private void createMigrationFilesFromCustomScripts() throws Exception {
        File customScriptsFile = new File(CUSTOM_SCRIPTS_PATH);
        if (customScriptsFile.exists() && customScriptsFile.length() != 0) {
            Path customScriptsPath = customScriptsFile.toPath();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            String description = Files
                    .lines(customScriptsPath)
                    .findFirst()
                    .filter(descriptionComment -> descriptionComment.startsWith("--"))
                    .map(descriptionComment -> descriptionComment
                            .replaceAll("--", "")
                            .trim()
                            .replaceAll(" ", "-"))
                    .orElse(AUTO_MIGRATION_NAME);
            String fileName = String.format("V%s__%s.sql", timestamp, description);
            Path migrationPath = Paths.get(flywaySettings.getLocation(), fileName);
            Files.copy(customScriptsPath, migrationPath);
            Files.write(customScriptsPath, new byte[0]);
        }
    }
}
