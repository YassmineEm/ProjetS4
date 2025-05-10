package com.example.project_generator;

import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ProjectGeneratorApplicationTests {

    @MockBean
    private InitializrMetadataProvider metadataProvider;
    
    @MockBean
    private ProjectGenerationInvoker<?> projectGenerationInvoker;

    @Test
    void contextLoads() {
    }
}