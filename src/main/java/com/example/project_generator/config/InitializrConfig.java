package com.example.project_generator.config;

import io.spring.initializr.generator.project.ProjectGenerator;
import io.spring.initializr.generator.project.ProjectAssetGenerator;
import io.spring.initializr.web.project.ProjectGenerationInvoker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitializrConfig {

    @Bean
    public ProjectGenerationInvoker<?> projectGenerationInvoker(ProjectGenerator projectGenerator) {
        return new ProjectGenerationInvoker<>(projectGenerator);
    }

    @Bean
    public ProjectGenerator projectGenerator(ProjectAssetGenerator assetGenerator) {
        // La version 0.11.1 n'utilise pas de descriptionResolver/contextFactory/customizer
        return new ProjectGenerator(assetGenerator);
    }
}
