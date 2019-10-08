package com.technaxis.querydsl.config;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Objects.nonNull;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(@Value("${swagger.pathMapping}") String swaggerPathMapping,
                      @Value("${swagger.host}") String swaggerHost,
                      @Value("${swagger.enableHttps}") Boolean httpsEnabled) {
        Docket api = new Docket(DocumentationType.SWAGGER_12)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.technaxis.querydsl.controller"))
                .build()
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .apiInfo(apiInfo());
        if (StringUtils.isNotBlank(swaggerHost)) {
            api.host(swaggerHost);
        }
        if (StringUtils.isNotBlank(swaggerPathMapping)) {
            api.pathMapping(swaggerPathMapping);
        }
        if (nonNull(httpsEnabled) && httpsEnabled) {
            api.protocols(Sets.newHashSet("https"));
        }

        return api;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Query DSL example REST API")
                .version("v1")
                .build();
    }
}