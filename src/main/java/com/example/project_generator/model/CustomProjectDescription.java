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
    private String groupId = "com.example";  // Valeur par défaut
    private String javaVersion = "17";      // Valeur par défaut
    private String artifactId;
    private String name;
    private String packageName;
    private String version = "0.0.1-SNAPSHOT";  // Valeur par défaut
    private Integer port = 8080;            // Valeur par défaut
    private String profile = "dev";         // Valeur par défaut
    private String dockerRepository = "your-default-repo";  // Valeur par défaut

    private String buildTool = "maven";     // Valeur par défaut
    private String springBootVersion;
    private Set<String> dependencies;

    // Getters et Setters
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

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public String getBaseDirectory() {
        return ".";
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
        updatePackageName();
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        updatePackageName();
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

    private void updatePackageName() {
        if (groupId != null && artifactId != null) {
            this.packageName = groupId + "." + artifactId.toLowerCase();
        }
    }

    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        // Normalise la version (ex: "17" au lieu de "17.0.2")
        this.javaVersion = javaVersion.split("\\.")[0];
    }

    @Override
    public String getApplicationName() {
        return name != null ? name : artifactId;
    }

    @Override
    public String getDescription() {
        return "Custom Project Description";
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

    public String getDockerRepository() {
        return dockerRepository;
    }

    public void setDockerRepository(String dockerRepository) {
        this.dockerRepository = dockerRepository;
    }

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