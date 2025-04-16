package com.example.projectgenerator.converter;

import com.example.projectgenerator.model.CustomProjectRequest;
import com.example.projectgenerator.model.CustomProjectDescription;
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
