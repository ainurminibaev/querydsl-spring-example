package com.technaxis.querydsl.config;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;

import javax.persistence.spi.PersistenceUnitInfo;

/**
 * @author Dmitry Sadchikov
 */
@Configuration
@EntityScan("com.technaxis.querydsl.model")
@AutoConfigureAfter({HibernateJpaAutoConfiguration.class})
public class SchemaGenerationConfig {

    @Bean
    public EntityScanPackages entityScanPackages(BeanFactory beanFactory) {
        return EntityScanPackages.get(beanFactory);
    }

    @Bean
    public Metadata getMetadata(StandardServiceRegistry standardServiceRegistry,
                                PersistenceUnitInfo persistenceUnitInfo) {
        final var metadataSources = new MetadataSources(standardServiceRegistry);
        final var managedClassNames = persistenceUnitInfo.getManagedClassNames();
        for (var managedClassName : managedClassNames) {
            metadataSources.addAnnotatedClassName(managedClassName);
        }
        return metadataSources.buildMetadata();
    }

    @Bean
    public StandardServiceRegistry getStandardServiceRegistry(JpaProperties jpaProperties,
                                                              DataSourceProperties dataSourceProperties) {
        final var properties = jpaProperties.getProperties();
        return new StandardServiceRegistryBuilder()
                .applySettings(properties)
                .applySetting("hibernate.connection.username", dataSourceProperties.getUsername())
                .applySetting("hibernate.connection.password", dataSourceProperties.getPassword())
                .applySetting("hibernate.connection.url", dataSourceProperties.getUrl())
                .applySetting(
                        "hibernate.connection.driver_class",
                        dataSourceProperties.getDriverClassName()
                )
                .applySetting(
                        "hibernate.implicit_naming_strategy",
                        "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy"
                )
                .applySetting(
                        "hibernate.physical_naming_strategy",
                        "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy"
                )
                .build();
    }

    @Bean
    public PersistenceUnitInfo getPersistenceUnitInfo(EntityScanPackages entityScanPackages) {
        final var packagesToScan = entityScanPackages.getPackageNames();
        final var persistenceUnitManager = new DefaultPersistenceUnitManager();
        String[] packagesToScanArr = packagesToScan.toArray(new String[0]);
        persistenceUnitManager.setPackagesToScan(packagesToScanArr);
        persistenceUnitManager.afterPropertiesSet();
        return persistenceUnitManager.obtainDefaultPersistenceUnitInfo();
    }
}
