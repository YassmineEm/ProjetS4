package com.example.project_generator.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration  // <-- Utilisez directement @Configuration sans alias
public class FreemarkerConfig {

    @Bean
    public Configuration freemarkerConfiguration() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        
        // Charge les templates depuis src/main/resources/templates/
        cfg.setDirectoryForTemplateLoading(new ClassPathResource("templates/").getFile());
        
        // Alternative si setDirectoryForTemplateLoading ne fonctionne pas :
        // cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates/");
        
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        return cfg;
    }
}