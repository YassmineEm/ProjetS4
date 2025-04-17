package com.example.project_generator.configuration;

import org.springframework.stereotype.Component;

@Component
public class ArchitectureContributors {

    public void configureArchitecture(String architectureType) {
        // Logique pour configurer l'architecture en fonction du type demandé
        System.out.println("Configuring architecture: " + architectureType);
    }
}
