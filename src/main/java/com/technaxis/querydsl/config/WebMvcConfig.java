package com.technaxis.querydsl.config;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.servlet.Filter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    public static final int CACHE_PERIOD_3DAY = 3 * 24 * 60 * 60;

    @Value("#{'${drugstars.storage.properties.jclouds.filesystem.basedir}' + '${drugstars.storage.container}' + '/'}")
    private String uploadingPath;

    // TODO: change project name
    @Value("${projectName.storage.baseUrl}")
    private String uploadingUrl;

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"};

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler(relativeUrl(uploadingUrl) + "**").addResourceLocations("file:" + uploadingPath);
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS).setCachePeriod(CACHE_PERIOD_3DAY);
    }

    private String relativeUrl(String url) {
        try {
            return new URL(url).getPath();
        } catch (Exception e) {
            return url;
        }
    }

    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    @Bean
    @Primary
    public FreeMarkerConfigurationFactoryBean freemarkerConfig() throws IOException, TemplateException {
        FreeMarkerConfigurationFactoryBean configurationFactory = new FreeMarkerConfigurationFactoryBean();
        configurationFactory.setTemplateLoaderPath("classpath:/web-templates/");
        configurationFactory.setFreemarkerSettings(new Properties() {{
            this.put("default_encoding", "UTF-8");
        }});
        configurationFactory.setDefaultEncoding("UTF-8");
        return configurationFactory;
    }

    @Bean(name = "freeMarkerConfigurer")
    @Primary
    public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException, TemplateException {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(freemarkerConfig().getObject());
        return freeMarkerConfigurer;
    }

    @Bean
    public ViewResolver getViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}