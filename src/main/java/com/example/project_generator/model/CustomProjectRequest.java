package com.example.project_generator.model;

import java.util.List;
import java.util.Set;

public class CustomProjectRequest {

    private String architectureType;  // Type d'architecture (par exemple "hexagonale", "en couches", etc.)
    private boolean generateDocker;   // Générer Dockerfile ?
    private boolean generateKubernetes;  // Générer Kubernetes ?
    private boolean generateCLCG;  // Générer le CLCG (CI/CD) ?
    private List<String> entities;  // Liste des entités (modèles) à générer
    private String dockerRepository;
    private String artifactId;
    private String name;
    private String javaVersion;
    private Integer port;
    private String profile;


    private String buildTool;  // "maven", "gradle-groovy", "gradle-kotlin"
    private String language;  // "java", "kotlin", "groovy"
    private String springBootVersion;  // Version Spring Boot choisie
    private Set<String> dependencies;  // Dependencies choisies



    public String getBuildTool() {
        return buildTool;
    }
    
    public void setBuildTool(String buildTool) {
        this.buildTool = buildTool;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getSpringBootVersion() {
        return springBootVersion;
    }
    
    public void setSpringBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
    }
    
    public Set<String> getDependencies() {
        return dependencies;
    }
    
    public void setDependencies(Set<String> dependencies) {
        this.dependencies = dependencies;
    }

    public String getArtifactId() {
        return artifactId;
    }
    
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getJavaVersion() {
        return javaVersion;
    }
    
    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }
    
    public Integer getPort() {
        return port;
    }
    
    public void setPort(Integer port) {
        this.port = port;
    }
    
    public String getProfile() {
        return profile;
    }
    
    public void setProfile(String profile) {
        this.profile = profile;
    }

    // Getters et Setters
    public String getArchitectureType() {
        return architectureType;
    }

    public String getDockerRepository() {
        return dockerRepository;
    }

    public void setDockerRepository(String dockerRepository) {
        this.dockerRepository = dockerRepository;
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

