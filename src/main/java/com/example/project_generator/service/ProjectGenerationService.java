package com.example.project_generator.service;

import com.example.project_generator.configuration.*;
import com.example.project_generator.model.CustomProjectDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.file.Path;

@Service
public class ProjectGenerationService {

    @Value("${project.directory}")
    private Path projectDirectory;

    @Autowired
    private DockerFileContributors dockerFileContributors;

    @Autowired
    private DockerComposeContributor dockerComposeContributor;

    @Autowired
    private ArchitectureContributors architectureContributors;

    @Autowired
    private ProjectSocketContributors projectSocketContributors;

    @Autowired
    private SubconnectManifestContributors subconnectManifestContributors;

    @Autowired
    private CICRPluginInContributors cicrPluginInContributors;

    public String generateProject(CustomProjectDescription description) {
        try {
            // 1. Configurer l'architecture
            architectureContributors.configureArchitecture(description.getArchitectureType());

            // 2. Configurer les sockets
            projectSocketContributors.configureSockets();

            // 3. Générer les fichiers Docker si demandé
            if (description.isGenerateDocker()) {
                dockerFileContributors.contribute(projectDirectory);
                dockerComposeContributor.contribute(projectDirectory);
            }

            // 4. Générer les manifestes Kubernetes si demandé
            if (description.isGenerateKubernetes()) {
                subconnectManifestContributors.generateKubernetesManifests();
            }

            // 5. Configurer CI/CD si demandé
            if (description.isGenerateCLCG()) {
                cicrPluginInContributors.configureCI();
            }

            return "Project generated successfully in directory: " + projectDirectory.toString();
            
        } catch (Exception e) {
            throw new ProjectGenerationException("Failed to generate project: " + e.getMessage(), e);
        }
    }

    public static class ProjectGenerationException extends RuntimeException {
        public ProjectGenerationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}