package com.bookshop.bookshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static resources - DO NOT use /** pattern as it intercepts API calls
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod(0);
        
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCachePeriod(0);
        
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCachePeriod(0);
        
        // Serve HTML files from root
        registry.addResourceHandler("/*.html")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0);
        
        // Serve admin HTML files
        registry.addResourceHandler("/admin/**")
                .addResourceLocations("classpath:/static/admin/")
                .setCachePeriod(0);
    }
}
