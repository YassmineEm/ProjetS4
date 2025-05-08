package com.example.project_generator.service;

import com.example.project_generator.configuration.*;
import com.example.project_generator.model.CustomProjectDescription;

import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import io.spring.initializr.web.project.ProjectGenerationResult;
import io.spring.initializr.web.project.ProjectRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;



@Service
public class ProjectGenerationService {
    private final ProjectGenerationInvoker projectGenerationInvoker;
    private final InitializrMetadataProvider metadataProvider;
    private final DockerFileContributors dockerFileContributors;
    private final DockerComposeContributor dockerComposeContributor;
    private final ArchitectureContributors architectureContributors;
    private final ProjectSocketContributors projectSocketContributors;
    private final SubconnectManifestContributors subconnectManifestContributors;
    private final CICRPluginInContributors cicrPluginInContributors;
    private final GitLabCIContributor gitLabCIContributor;


    @Autowired
    public ProjectGenerationService(
            ProjectGenerationInvoker projectGenerationInvoker,
            InitializrMetadataProvider metadataProvider,
            DockerFileContributors dockerFileContributors,
            DockerComposeContributor dockerComposeContributor,
            ArchitectureContributors architectureContributors,
            ProjectSocketContributors projectSocketContributors,
            SubconnectManifestContributors subconnectManifestContributors,
            CICRPluginInContributors cicrPluginInContributors,
            GitLabCIContributor gitLabCIContributor) {
        this.projectGenerationInvoker = projectGenerationInvoker;
        this.metadataProvider = metadataProvider;
        this.dockerFileContributors = dockerFileContributors;
        this.dockerComposeContributor = dockerComposeContributor;
        this.architectureContributors = architectureContributors;
        this.projectSocketContributors = projectSocketContributors;
        this.subconnectManifestContributors = subconnectManifestContributors;
        this.cicrPluginInContributors = cicrPluginInContributors;
        this.gitLabCIContributor = gitLabCIContributor;
    }
    

    public String generateProject(CustomProjectDescription description) {
        try {
            
            ProjectRequest request = toProjectRequest(description);
            ProjectGenerationResult result = projectGenerationInvoker.invokeProjectStructureGeneration(request);
            Path projectRoot = result.getRootDirectory();



            // 2. Architecture
            architectureContributors.configureArchitecture(
                description.getArchitectureType(),
                projectRoot,
                description.getGroupId(),
                description.getArtifactId()
            );

            // 3. Générer les entités
            generateEntities(description, projectRoot);

            // 4. Sockets
            projectSocketContributors.configureSockets();

            // 5. Docker
            if (description.isGenerateDocker()) {
                dockerFileContributors.setDescription(description);
                dockerFileContributors.contribute(projectRoot);
                dockerComposeContributor.setDescription(description);
                dockerComposeContributor.contribute(projectRoot);
            }

            // 6. Kubernetes
            if (description.isGenerateKubernetes()) {
                subconnectManifestContributors.generateKubernetesManifests();
            }

            // 7. CI/CD
            if (description.isGenerateCLCG()) {
                cicrPluginInContributors.configureCI();
                gitLabCIContributor.setDescription(description);
                gitLabCIContributor.contribute(projectRoot);
            }

            return projectRoot.toString();

        } catch (Exception e) {
            throw new ProjectGenerationException("Failed to generate project: " + e.getMessage(), e);
        }
    }

        private void generateEntities(CustomProjectDescription description, Path projectRoot) throws IOException {
        for (String entityName : description.getEntities()) {
            String packagePath = description.getGroupId().replace(".", "/") + "/" + description.getArtifactId().toLowerCase() + "/model";
            String packageName = description.getGroupId() + "." + description.getArtifactId().toLowerCase() + ".model";

            Path entityFile = projectRoot.resolve("src/main/java/" + packagePath + "/" + entityName + ".java");
            Files.createDirectories(entityFile.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(entityFile)) {
                writer.write("package " + packageName + ";\n\n");
                writer.write("public class " + entityName + " {\n");
                writer.write("    // TODO: Add fields and methods\n");
                writer.write("}\n");
            }
        }
    }

    public static class ProjectGenerationException extends RuntimeException {
        public ProjectGenerationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private ProjectRequest toProjectRequest(CustomProjectDescription description) {
        ProjectRequest request = new ProjectRequest();
        request.setType(description.getBuildTool().equals("maven") ? "maven-project" : "gradle-project");
        request.setBootVersion(description.getSpringBootVersion());
        request.setGroupId(description.getGroupId());
        request.setArtifactId(description.getArtifactId());
        request.setName(description.getName());
        request.setDescription(description.getDescription());
        request.setPackageName(description.getPackageName());
        request.setJavaVersion(description.getJavaVersion());
        request.setPackaging("jar");
        request.setLanguage("java");
        request.setDependencies(new ArrayList<>(description.getDependencies())); 
      return request;
}
}

