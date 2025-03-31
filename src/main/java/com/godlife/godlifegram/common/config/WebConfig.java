package com.godlife.godlifegram.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**")
                .addResourceLocations("classpath:/static/image/")
                .setCachePeriod(60000).resourceChain(true);
        registry.addResourceHandler("/icon/**")
                .addResourceLocations("classpath:/static/icon/")
                .setCachePeriod(60000).resourceChain(true);
        registry.addResourceHandler("/favicon/**")
                .addResourceLocations("classpath:/static/favicon/")
                .setCachePeriod(60000).resourceChain(true);
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod(60000).resourceChain(true);

    }

}

