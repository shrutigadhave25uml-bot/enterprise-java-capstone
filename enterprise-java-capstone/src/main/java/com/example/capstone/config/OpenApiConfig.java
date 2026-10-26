package com.example.capstone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI capstoneOpenAPI() {
        return new OpenAPI().info(new Info()
            .title("Enterprise Java Full Stack Capstone API")
            .version("1.0.0")
            .description("Demo REST API for an enterprise hospital management dashboard."));
    }
}
