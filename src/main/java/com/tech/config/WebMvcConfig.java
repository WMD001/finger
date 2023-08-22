package com.tech.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Wang
 * 2023/7/24
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 添加外部资源目录
        registry.addResourceHandler("/app/**")
                .addResourceLocations("file:E:/xd-workspace-4/SenceDemo/build/");
    }
}
