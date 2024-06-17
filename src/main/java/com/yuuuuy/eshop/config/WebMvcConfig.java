package com.yuuuuy.eshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 静态资源映射配置
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    static final String IMG_PATH = System.getProperty("user.dir") + "/static/";
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:" + IMG_PATH);
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
