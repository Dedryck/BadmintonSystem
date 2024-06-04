package com.coding24.badmintonsystem_1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//添加 WebConfig 解决了资源映射问题，使得Spring Boot能够正确处理并返回最新的静态资源文件，
// 从而解决了在上传图片后需要重启应用才能看到图片的问题。

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:src/main/resources/static/images/");
    }
}

