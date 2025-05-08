package com.example.project_generator.service;

import com.example.project_generator.configuration.*;
import com.example.project_generator.model.CustomProjectDescription;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    @Autowired
    private GitLabCIContributor gitLabCIContributor;

    @Autowired
    private Map<String, Set<String>> compatibleVersions;

    @Autowired
    private Map<String, String> dependencyDescriptions;

    @Autowired
    private freemarker.template.Configuration freemarkerConfig;

    public String generateProject(CustomProjectDescription description) {
        try {
            // 1. Configurer l'architecture
            architectureContributors.configureArchitecture(description.getArchitectureType(), projectDirectory);

            // 2. Générer le fichier de build approprié
            generateBuildFile(description);

            // 3. Ajouter les dépendances
            addDependencies(description);

            generateEntities(description);

            // 4. Configurer les sockets
            projectSocketContributors.configureSockets();

            // 5. Générer les fichiers Docker si demandé
            if (description.isGenerateDocker()) {
                dockerFileContributors.contribute(projectDirectory);
                dockerComposeContributor.contribute(projectDirectory);
            }

            // 6. Générer les manifestes Kubernetes si demandé
            if (description.isGenerateKubernetes()) {
                subconnectManifestContributors.generateKubernetesManifests();
            }

            // 7. Configurer CI/CD si demandé
            if (description.isGenerateCLCG()) {
                cicrPluginInContributors.configureCI();
                gitLabCIContributor.contribute(projectDirectory);
            }

            return "Project generated successfully in directory: " + projectDirectory.toString();

        } catch (Exception e) {
            throw new ProjectGenerationException("Failed to generate project: " + e.getMessage(), e);
        }
    }

    private void generateBuildFile(CustomProjectDescription description) throws IOException {
        switch (description.getBuildTool()) {
            case "maven":
                generateMavenPom(description);
                break;
            case "gradle-groovy":
                generateGradleBuildGroovy(description);
                break;
            case "gradle-kotlin":
                generateGradleBuildKotlin(description);
                break;
            default:
                generateMavenPom(description);
        }
    }


    private void generateEntities(CustomProjectDescription description) throws IOException {
        for (String entityName : description.getEntities()) {
            Map<String, Object> model = new HashMap<>();
            model.put("entityName", entityName);
    
            // Convertir le groupId en chemin : ex. "com.example.myapp" -> "com/example/myapp"
            String packagePath = description.getGroupId().replace(".", "/") + "/" + description.getArtifactId().toLowerCase() + "/model";
    
            // Générer le nom de package pour le fichier .java
            String packageName = description.getGroupId() + "." + description.getArtifactId().toLowerCase() + ".model";
            model.put("packageName", packageName);
    
            Path entityPath = projectDirectory.resolve("src/main/java/" + packagePath + "/" + entityName + ".java");
            Files.createDirectories(entityPath.getParent());
    
            try (BufferedWriter writer = Files.newBufferedWriter(entityPath)) {
                Template template = freemarkerConfig.getTemplate("Entity.java.ftl");
                template.process(model, writer);
            } catch (TemplateException e) {
                throw new IOException("Failed to generate entity: " + entityName, e);
            }
        }
    }    
    
    private void generateMavenPom(CustomProjectDescription description) throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("description", description);
        model.put("dependencies", dependencyDescriptions);

        Path pomPath = projectDirectory.resolve("pom.xml");
        try (BufferedWriter writer = Files.newBufferedWriter(pomPath)) {
            Template template = freemarkerConfig.getTemplate("pom.xml.ftl");
            template.process(model, writer);
        } catch (TemplateException e) {
            throw new IOException("Failed to generate pom.xml", e);
        }
    }

    private void generateGradleBuildGroovy(CustomProjectDescription description) throws IOException {
        // Placeholder : implémenter comme generateMavenPom avec "build.gradle.ftl"
    }

    private void generateGradleBuildKotlin(CustomProjectDescription description) throws IOException {
        // Placeholder : implémenter comme generateMavenPom avec "build.gradle.kts.ftl"
    }

    private void addDependencies(CustomProjectDescription description) {
        // Implémenter l'ajout de dépendances dynamiquement dans le fichier de build
        // en utilisant les informations contenues dans `description` et `dependencyDescriptions`.
    }

    public static class ProjectGenerationException extends RuntimeException {
        public ProjectGenerationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
