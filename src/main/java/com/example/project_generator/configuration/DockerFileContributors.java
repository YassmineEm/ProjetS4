package com.example.projectgenerator.configuration;

import org.springframework.stereotype.Component;

@Component
public class DockerFileContributors {

    public void generateDockerFile() {
        System.out.println("Generating Dockerfile...");
    }
}
