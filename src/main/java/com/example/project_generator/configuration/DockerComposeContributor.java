package com.example.project_generator.configuration;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class DockerComposeContributor implements ProjectContributor {

    @Autowired
    private Configuration freemarkerConfig;

    @Override
    public void contribute(Path projectRoot) throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("serviceName", "app");
        model.put("port", 8080);

        Path composePath = projectRoot.resolve("docker-compose.yml");
        generateFromTemplate("docker-compose.ftl", model, composePath);
    }

    private void generateFromTemplate(String templateName, Map<String, Object> model, Path outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputPath)))) {
            Template template = freemarkerConfig.getTemplate(templateName);
            template.process(model, writer);
        } catch (TemplateException e) {
            throw new IOException("Erreur lors de la génération du template docker-compose", e);
        }
    }
}
