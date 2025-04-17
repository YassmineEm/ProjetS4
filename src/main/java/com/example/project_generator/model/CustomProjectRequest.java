package com.example.project_generator.model;

import java.util.List;

public class CustomProjectRequest {

    private String architectureType;  // Type d'architecture (par exemple "hexagonale", "en couches", etc.)
    private boolean generateDocker;   // Générer Dockerfile ?
    private boolean generateKubernetes;  // Générer Kubernetes ?
    private boolean generateCLCG;  // Générer le CLCG (CI/CD) ?
    private List<String> entities;  // Liste des entités (modèles) à générer

    // Getters et Setters
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

