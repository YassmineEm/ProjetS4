package com.example.project_generator.configuration;

import com.example.project_generator.model.CustomProjectDescription;
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
public class DockerFileContributors implements ProjectContributor {

    @Autowired
    private CustomProjectDescription description;

    @Autowired
    private Configuration freemarkerConfig;

    @Override
    public void contribute(Path projectRoot) throws IOException {
        if (description instanceof CustomProjectDescription) {
            CustomProjectDescription customDesc = (CustomProjectDescription) description;
            if (customDesc.isGenerateDocker()) {
                // Données pour le template
                Map<String, Object> model = new HashMap<>();
                model.put("artifactId", description.getArtifactId());
                model.put("javaVersion", customDesc.getJavaVersion());


                // Générer le Dockerfile
                Path dockerfilePath = projectRoot.resolve("Dockerfile");
                generateFromTemplate("Dockerfile.ftl", model, dockerfilePath);
            }
        }
    }

    private void generateFromTemplate(String templateName, Map<String, Object> model, Path outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputPath)))) {
            Template template = freemarkerConfig.getTemplate(templateName);
            template.process(model, writer);
        } catch (TemplateException e) {
            throw new IOException("Erreur lors du rendu du template FreeMarker : " + templateName, e);
        }
    }
}
