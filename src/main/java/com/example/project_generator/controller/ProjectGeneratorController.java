package com.example.project_generator.controller;

import com.example.project_generator.model.CustomProjectRequest;
import com.example.project_generator.model.CustomProjectDescription;
import com.example.project_generator.converter.CustomProjectRequestToDescriptionConverter;
import com.example.project_generator.configuration.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.Path;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/generate")
public class ProjectGeneratorController {

    @Value("${project.directory}")
    private Path projectDirectory;

    @Autowired
    private CustomProjectRequestToDescriptionConverter converter;

    @Autowired
    private DockerFileContributors dockerFileContributors;

    @Autowired
    private SubconnectManifestContributors subconnectManifestContributors;

    @Autowired
    private CICRPluginInContributors cicrPluginInContributors;

    @Autowired
    private ArchitectureContributors architectureContributors;

    @Autowired
    private ProjectSocketContributors projectSocketContributors;

    @Autowired
    private DockerComposeContributor dockerComposeContributor;

    @Autowired
    private GitLabCIContributor gitLabCIContributor;

    @PostMapping
    public ResponseEntity<byte[]> generateProject(@RequestBody CustomProjectRequest request) throws IOException {
        CustomProjectDescription description = converter.convert(request);

        dockerFileContributors.setDescription(description);
        dockerComposeContributor.setDescription(description);
        gitLabCIContributor.setDescription(description);
        
        // Création du ZIP en mémoire
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {
            
            // Configuration de l'architecture et des sockets
            architectureContributors.configureArchitecture(description.getArchitectureType());
            projectSocketContributors.configureSockets();

            // Génération des fichiers Spring Boot de base
            generateSpringBootBase(zipOut, description);

            // Génération des fichiers Docker si demandé
            if (description.isGenerateDocker()) {
                addFileToZip(zipOut, "Dockerfile", dockerFileContributors.generateContent());
                addFileToZip(zipOut, "docker-compose.yml", dockerComposeContributor.generateContent());
            }

            // Génération des manifests Kubernetes si demandé
            if (description.isGenerateKubernetes()) {
                subconnectManifestContributors.generateKubernetesManifests();
                // Ajouter ici la génération des fichiers Kubernetes dans le ZIP
            }

            // Génération de la configuration CI/CD si demandé
            if (description.isGenerateCLCG()) {
                cicrPluginInContributors.configureCI();
                addFileToZip(zipOut, ".gitlab-ci.yml", gitLabCIContributor.generateContent());
            }
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + description.getArtifactId() + ".zip")
                .body(baos.toByteArray());
    }

    private void generateSpringBootBase(ZipOutputStream zipOut, CustomProjectDescription description) throws IOException {
        // Structure de base d'un projet Spring Boot
        addFileToZip(zipOut, "pom.xml", generatePomContent(description));
        addFileToZip(zipOut, "src/main/java/com/example/" + description.getArtifactId() + "/Application.java", 
            "package com.example." + description.getArtifactId() + ";\n\n" +
            "import org.springframework.boot.SpringApplication;\n" +
            "import org.springframework.boot.autoconfigure.SpringBootApplication;\n\n" +
            "@SpringBootApplication\n" +
            "public class Application {\n\n" +
            "    public static void main(String[] args) {\n" +
            "        SpringApplication.run(Application.class, args);\n" +
            "    }\n" +
            "}");
        addFileToZip(zipOut, "src/main/resources/application.properties", "");
    }

    private String generatePomContent(CustomProjectDescription description) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
               "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
               "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
               "    <modelVersion>4.0.0</modelVersion>\n" +
               "    <parent>\n" +
               "        <groupId>org.springframework.boot</groupId>\n" +
               "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
               "        <version>3.4.4</version>\n" +
               "        <relativePath/>\n" +
               "    </parent>\n" +
               "    <groupId>com.example</groupId>\n" +
               "    <artifactId>" + description.getArtifactId() + "</artifactId>\n" +
               "    <version>0.0.1-SNAPSHOT</version>\n" +
               "    <name>" + description.getName() + "</name>\n" +
               "    <dependencies>\n" +
               "        <dependency>\n" +
               "            <groupId>org.springframework.boot</groupId>\n" +
               "            <artifactId>spring-boot-starter-web</artifactId>\n" +
               "        </dependency>\n" +
               "    </dependencies>\n" +
               "</project>";
    }


    private void addFileToZip(ZipOutputStream zipOut, String filePath, String content) throws IOException {
        ZipEntry zipEntry = new ZipEntry(filePath);
        zipOut.putNextEntry(zipEntry);
        zipOut.write(content.getBytes());
        zipOut.closeEntry();
    }
}