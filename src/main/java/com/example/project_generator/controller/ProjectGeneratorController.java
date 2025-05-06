package com.example.project_generator.controller;

import com.example.project_generator.model.CustomProjectRequest;
import com.example.project_generator.model.CustomProjectDescription;
import com.example.project_generator.converter.CustomProjectRequestToDescriptionConverter;
import com.example.project_generator.configuration.*;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URL;
import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
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

    @Autowired
    private freemarker.template.Configuration freemarkerConfig;

    @PostMapping
    public ResponseEntity<byte[]> generateProject(@RequestBody CustomProjectRequest request) throws IOException {
        CustomProjectDescription description = converter.convert(request);

        dockerFileContributors.setDescription(description);
        dockerComposeContributor.setDescription(description);
        gitLabCIContributor.setDescription(description);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {
            
            architectureContributors.configureArchitecture(description.getArchitectureType(),projectDirectory);
            projectSocketContributors.configureSockets();

            generateSpringBootBase(zipOut, description);

            if (description.isGenerateDocker()) {
                addFileToZip(zipOut, "Dockerfile", dockerFileContributors.generateContent());
                addFileToZip(zipOut, "docker-compose.yml", dockerComposeContributor.generateContent());
            }

            if (description.isGenerateKubernetes()) {
                subconnectManifestContributors.generateKubernetesManifests();
            }

            if (description.isGenerateCLCG()) {
                cicrPluginInContributors.configureCI();
                addFileToZip(zipOut, ".gitlab-ci.yml", gitLabCIContributor.generateContent());
            }

            // Ajout des fichiers Maven Wrapper
            addMavenWrapperFiles(zipOut);
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + description.getArtifactId() + ".zip")
                .body(baos.toByteArray());
    }

    private void generateSpringBootBase(ZipOutputStream zipOut, CustomProjectDescription description) throws IOException {
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

    private void addMavenWrapperFiles(ZipOutputStream zipOut) throws IOException {
        // Ajout du fichier mvnw (Unix)
        addFileToZip(zipOut, "mvnw", getMvnwContent());
        
        // Ajout du fichier mvnw.cmd (Windows)
        addFileToZip(zipOut, "mvnw.cmd", getMvnwCmdContent());
        
        // Ajout du dossier .mvn/wrapper avec ses fichiers
        addFileToZip(zipOut, ".mvn/wrapper/maven-wrapper.properties", getMavenWrapperPropertiesContent());
        
        // Téléchargement du fichier maven-wrapper.jar depuis internet
        try {
            URL wrapperUrl = new URL("https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar");
            try (InputStream in = wrapperUrl.openStream()) {
                addFileToZip(zipOut, ".mvn/wrapper/maven-wrapper.jar", in.readAllBytes());
            }
        } catch (Exception e) {
            throw new IOException("Failed to download maven-wrapper.jar", e);
        }
    }

    private String getMvnwContent() {
        return "#!/bin/sh\n" +
               "# ----------------------------------------------------------------------------\n" +
               "# Licensed to the Apache Software Foundation (ASF) under one\n" +
               "# or more contributor license agreements.  See the NOTICE file\n" +
               "# distributed with this work for additional information\n" +
               "# regarding copyright ownership.  The ASF licenses this file\n" +
               "# to you under the Apache License, Version 2.0 (the\n" +
               "# \"License\"); you may not use this file except in compliance\n" +
               "# with the License.  You may obtain a copy of the License at\n" +
               "#\n" +
               "#    http://www.apache.org/licenses/LICENSE-2.0\n" +
               "#\n" +
               "# Unless required by applicable law or agreed to in writing,\n" +
               "# software distributed under the License is distributed on an\n" +
               "# \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY\n" +
               "# KIND, either express or implied.  See the License for the\n" +
               "# specific language governing permissions and limitations\n" +
               "# under the License.\n" +
               "# ----------------------------------------------------------------------------\n" +
               "\n" +
               "# ... [le reste du contenu standard de mvnw] ...";
    }

    private String getMvnwCmdContent() {
        return "@REM ----------------------------------------------------------------------------\n" +
               "@REM Licensed to the Apache Software Foundation (ASF) under one\n" +
               "@REM or more contributor license agreements.  See the NOTICE file\n" +
               "@REM distributed with this work for additional information\n" +
               "@REM regarding copyright ownership.  The ASF licenses this file\n" +
               "@REM to you under the Apache License, Version 2.0 (the\n" +
               "@REM \"License\"); you may not use this file except in compliance\n" +
               "@REM with the License.  You may obtain a copy of the License at\n" +
               "@REM\n" +
               "@REM    http://www.apache.org/licenses/LICENSE-2.0\n" +
               "@REM\n" +
               "@REM Unless required by applicable law or agreed to in writing,\n" +
               "@REM software distributed under the License is distributed on an\n" +
               "@REM \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY\n" +
               "@REM KIND, either express or implied.  See the License for the\n" +
               "@REM specific language governing permissions and limitations\n" +
               "@REM under the License.\n" +
               "@REM ----------------------------------------------------------------------------\n" +
               "\n" +
               "@REM ... [le reste du contenu standard de mvnw.cmd] ...";
    }

    private String getMavenWrapperPropertiesContent() {
        return "distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip\n" +
               "wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar";
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

    private void addFileToZip(ZipOutputStream zipOut, String filePath, byte[] content) throws IOException {
        ZipEntry zipEntry = new ZipEntry(filePath);
        zipOut.putNextEntry(zipEntry);
        zipOut.write(content);
        zipOut.closeEntry();
    }
}