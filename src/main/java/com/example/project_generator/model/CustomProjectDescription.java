package com.example.project_generator.model;

import io.spring.initializr.generator.project.ProjectDescription;
import java.util.List;

public class CustomProjectDescription implements ProjectDescription {
    private String architectureType;
    private boolean generateDocker;
    private boolean generateKubernetes;
    private boolean generateCLCG;
    private List<String> entities;
    private String javaVersion = "17";
    private String artifactId;
    private String name;
    private String packageName;
    private String version;

    
    @Override
    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    // Getters et Setters personnalisés
    public String getArchitectureType() {
        return architectureType;
    }

    public void setArchitectureType(String architectureType) {
        this.architectureType = architectureType;
    }

    public boolean isGenerateDocker() {
        return generateDocker;
    }

    public void setGenerateDocker(boolean generateDocker) {
        this.generateDocker = generateDocker;
    }

    public boolean isGenerateKubernetes() {
        return generateKubernetes;
    }

    public void setGenerateKubernetes(boolean generateKubernetes) {
        this.generateKubernetes = generateKubernetes;
    }

    public boolean isGenerateCLCG() {
        return generateCLCG;
    }

    public void setGenerateCLCG(boolean generateCLCG) {
        this.generateCLCG = generateCLCG;
    }

    public List<String> getEntities() {
        return entities;
    }

    public void setEntities(List<String> entities) {
        this.entities = entities;
    }
}