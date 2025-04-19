package com.example.project_generator.config;

import com.example.project_generator.model.CustomProjectDescription;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {
    
    @Bean
    public CustomProjectDescription customProjectDescription() {
        CustomProjectDescription description = new CustomProjectDescription();
        description.setArtifactId("default-artifact");
        description.setName("default-name");
        return description;
    }
}