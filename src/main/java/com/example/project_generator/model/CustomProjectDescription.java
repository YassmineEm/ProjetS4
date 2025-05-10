package com.example.project_generator.model;


import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.packaging.Packaging;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.buildsystem.Dependency;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CustomProjectDescription extends MutableProjectDescription {

    private String architectureType;
    private boolean generateDocker;
    private boolean generateKubernetes;
    private boolean generateCLCG;
    private List<String> entities;

    private Language language = new JavaLanguage(); // Valeur par défaut : Java
    private String groupId = "com.example";
    private String artifactId;
    private String name;
    private String version = "0.0.1-SNAPSHOT";
    private String packageName;

    private String javaVersion = "17";
    private String profile = "dev";
    private Integer port = 8080;
    private String dockerRepository = "your-default-repo";

    private String buildTool = "maven"; 
    private String springBootVersion = "3.2.0";
    private Set<String> dependencies = new HashSet<>();

    // === Nouveaux ajouts obligatoires ===

    @Override
    public BuildSystem getBuildSystem() {
        return switch (buildTool) {
          case "gradle-groovy" -> new GradleBuildSystem();
          case "gradle-kotlin" -> new GradleBuildSystem(GradleBuildSystem.DIALECT_KOTLIN);
          case "maven"-> new MavenBuildSystem();
          default -> new MavenBuildSystem();
        };
    }

    @Override
    public Version getPlatformVersion() {
        return Version.parse(springBootVersion);
    }

    @Override
    public Map<String, Dependency> getRequestedDependencies() {
      Map<String, Dependency> map = new HashMap<>();
      if (dependencies != null) {
        for (String id : dependencies) {
            // Supposons que l'ID est au format "groupId:artifactId"
            String[] parts = id.split(":");
            if (parts.length == 2) {
                map.put(id, Dependency.withCoordinates(parts[0], parts[1]).build());
            } else {
                // Gestion d'erreur si le format n'est pas bon
                throw new IllegalArgumentException("Dependency ID must be in format 'groupId:artifactId'");
            }
        }
     }
      return map;
    }

    @Override
    public Packaging getPackaging() {
        return Packaging.forId("jar");
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
        return name != null ? name : artifactId;
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
        this.javaVersion = javaVersion.split("\\.")[0]; // normaliser "17.0.1" -> "17"
    }

    @Override
    public String getApplicationName() {
        return getName();
    }

    @Override
    public String getDescription() {
        return "Custom Project Description";
    }

    // === Champs personnalisés ===

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
}
