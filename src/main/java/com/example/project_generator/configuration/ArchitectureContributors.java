package com.example.project_generator.configuration;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.project_generator.model.CustomProjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ArchitectureContributors {


    public void configureArchitecture(String architectureType, Path projectRoot, String groupId) throws IOException {
        System.out.println("Configuring architecture: " + architectureType);

        switch (architectureType.toLowerCase()) {
            case "en-couches" -> generateLayeredArchitecture(projectRoot, groupId);
            case "hexagonal" -> generateHexagonalArchitecture(projectRoot, groupId);
            default -> generateDefaultArchitecture(projectRoot);
        }
    }

    private void generateLayeredArchitecture(Path projectRoot, String groupId) throws IOException {
        String basePackage = groupId.replace(".", "/");  // Utilisation dynamique du groupId
        Path mainJavaPath = projectRoot.resolve("src/main/java/" + basePackage);

        createDirectories(mainJavaPath, "controller", "service", "repository", "model", "config");

        generateBaseClass(mainJavaPath, "controller", "BaseController", groupId);
        generateBaseClass(mainJavaPath, "service", "BaseService", groupId);
        generateBaseClass(mainJavaPath, "repository", "BaseRepository", groupId);
    }

    private void generateHexagonalArchitecture(Path projectRoot,String groupId) throws IOException {
        String basePackage = groupId.replace(".", "/");  // Utilisation dynamique du groupId
        Path mainJavaPath = projectRoot.resolve("src/main/java/" + basePackage);

        createDirectories(mainJavaPath, "application", "domain", "infrastructure", "interfaces", "config");

        generateBaseClass(mainJavaPath, "domain", "DomainEntity",groupId);
        generateBaseClass(mainJavaPath, "domain", "DomainService",groupId);
        generateBaseClass(mainJavaPath, "application", "ApplicationService",groupId);
        generateBaseClass(mainJavaPath, "interfaces", "RestController",groupId);
    }

    private void generateDefaultArchitecture(Path projectRoot) throws IOException {
        // Optionnel : générer une structure minimaliste ou vide
        System.out.println("No valid architecture selected, skipping structure generation.");
    }

    private void generateBaseClass(Path basePath, String packageName, String className, String groupId) throws IOException {
        Path packagePath = basePath.resolve(packageName);
        Files.createDirectories(packagePath);

        Path filePath = packagePath.resolve(className + ".java");
        if (!Files.exists(filePath)) {
            String content = "package " + groupId + "." + packageName + ";\n\n" +  
                             "public class " + className + " {\n" +
                             "    // TODO: Implement " + className + " functionality\n" +
                             "}\n";
            Files.write(filePath, content.getBytes());
        }
    }

    private void createDirectories(Path basePath, String... subDirs) throws IOException {
        for (String dir : subDirs) {
            Files.createDirectories(basePath.resolve(dir));
        }
    }
}
