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
public class GitLabCIContributor implements ProjectContributor {

    @Autowired
    private Configuration freemarkerConfig;

    @Override
    public void contribute(Path projectRoot) throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("artifactId", "your-artifact-id"); // À remplacer par la valeur réelle
        model.put("javaVersion", "17"); // À remplacer par la valeur réelle
        model.put("dockerRepository", "your-docker-repo"); // À remplacer par la valeur réelle

        Path gitlabCiPath = projectRoot.resolve(".gitlab-ci.yml");
        generateFromTemplate("gitlab-ci.yml.ftl", model, gitlabCiPath);
    }

    private void generateFromTemplate(String templateName, Map<String, Object> model, Path outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputPath)))) {
            Template template = freemarkerConfig.getTemplate(templateName);
            template.process(model, writer);
        } catch (TemplateException e) {
            throw new IOException("Erreur lors de la génération du template GitLab CI", e);
        }
    }
}