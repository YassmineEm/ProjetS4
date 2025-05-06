package com.example.project_generator.model;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.packaging.Packaging;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomProjectDescription implements ProjectDescription {

    private String architectureType;
    private boolean generateDocker;
    private boolean generateKubernetes;
    private boolean generateCLCG;
    private List<String> entities;
    private Language language = new JavaLanguage();
    private String groupId = "com.example";
    private String javaVersion = "17";
    private String artifactId;
    private String name;
    private String packageName;
    private String version;
    private Integer port;
    private String profile;
    private String dockerRepository;


    private String buildTool;
    private String springBootVersion;
    private Set<String> dependencies;


    public String getBuildTool() {
        return buildTool;
    }
    
    public void setBuildTool(String buildTool) {
        this.buildTool = buildTool;
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

    @Override
    public Language getLanguage() {
        return language;
    }
    public Integer getPort() {
        return port;
    }

    public String getDockerRepository() {
        return dockerRepository;
    }

    public void setDockerRepository(String dockerRepository) {
        this.dockerRepository = dockerRepository;
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

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public String getBaseDirectory() {
        return ".";
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    @Override
    public String getApplicationName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Custom Project Description";
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    // Setters
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    // Champs personnalisés
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

    @Override
    public Map<String, Dependency> getRequestedDependencies() {
        throw new UnsupportedOperationException("Unimplemented method 'getRequestedDependencies'");
    }

    @Override
    public Version getPlatformVersion() {
        throw new UnsupportedOperationException("Unimplemented method 'getPlatformVersion'");
    }

    @Override
    public BuildSystem getBuildSystem() {
        throw new UnsupportedOperationException("Unimplemented method 'getBuildSystem'");
    }

    @Override
    public Packaging getPackaging() {
        throw new UnsupportedOperationException("Unimplemented method 'getPackaging'");
    }
}