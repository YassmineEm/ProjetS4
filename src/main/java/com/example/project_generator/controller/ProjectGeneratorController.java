package com.example.projectgenerator.controller;

import com.example.projectgenerator.model.CustomProjectRequest;
import com.example.projectgenerator.model.CustomProjectDescription;
import com.example.projectgenerator.converter.CustomProjectRequestToDescriptionConverter;
import com.example.projectgenerator.contributors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/generate")
public class ProjectGeneratorController {

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

    @PostMapping
    public String generateProject(@RequestBody CustomProjectRequest request) {
        // Convertir la requête en description
        CustomProjectDescription description = converter.convert(request);

        // Configurer l'architecture
        architectureContributors.configureArchitecture(description.getArchitectureType());

        // Configurer les sockets
        projectSocketContributors.configureSockets();

        // Générer Dockerfile si nécessaire
        if (description.isGenerateDocker()) {
            dockerFileContributors.generateDockerFile();
            dockerComposeContributor.contribute(projectDirectory);
        }

        // Générer les manifestes Kubernetes si nécessaire
        if (description.isGenerateKubernetes()) {
            subconnectManifestContributors.generateKubernetesManifests();
        }

        // Configurer CI/CD si nécessaire
        if (description.isGenerateCLCG()) {
            cicrPluginInContributors.configureCI();
        }

        return "Project generated successfully!";
    }
}
