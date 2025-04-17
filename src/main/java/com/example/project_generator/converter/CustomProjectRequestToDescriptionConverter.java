package com.example.project_generator.converter;

import com.example.project_generator.model.CustomProjectRequest;
import com.example.project_generator.model.CustomProjectDescription;
import org.springframework.stereotype.Component;

@Component
public class CustomProjectRequestToDescriptionConverter {
    public CustomProjectDescription convert(CustomProjectRequest request) {
        CustomProjectDescription description = new CustomProjectDescription();
        description.setArchitectureType(request.getArchitectureType());
        description.setGenerateDocker(request.isGenerateDocker());
        description.setGenerateKubernetes(request.isGenerateKubernetes());
        description.setGenerateCLCG(request.isGenerateCLCG());
        description.setEntities(request.getEntities());
        return description;
    }
}