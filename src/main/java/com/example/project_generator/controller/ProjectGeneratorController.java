package com.example.project_generator.controller;

import com.example.project_generator.model.CustomProjectRequest;
import com.example.project_generator.model.CustomProjectDescription;
import com.example.project_generator.converter.CustomProjectRequestToDescriptionConverter;
import com.example.project_generator.configuration.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.nio.file.Path;
import java.io.IOException;

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

    @PostMapping
    public String generateProject(@RequestBody CustomProjectRequest request) throws IOException{
        CustomProjectDescription description = converter.convert(request);
        
        architectureContributors.configureArchitecture(description.getArchitectureType());
        projectSocketContributors.configureSockets();

        if (description.isGenerateDocker()) {
            dockerFileContributors.contribute(projectDirectory);
            dockerComposeContributor.contribute(projectDirectory);
        }

        if (description.isGenerateKubernetes()) {
            subconnectManifestContributors.generateKubernetesManifests();
        }

        if (description.isGenerateCLCG()) {
            cicrPluginInContributors.configureCI();
        }

        return "Project generated successfully!";
    }
}