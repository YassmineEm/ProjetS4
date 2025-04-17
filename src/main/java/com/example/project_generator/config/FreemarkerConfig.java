package com.example.project_generator.config;

import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FreemarkerConfig {

    @Bean
    public freemarker.template.Configuration freemarkerConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_32);
        cfg.setDirectoryForTemplateLoading(new ClassPathResource("templates/").getFile());
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }
}