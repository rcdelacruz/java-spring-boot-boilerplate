package com.example.training.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Value("${environment.name:Unknown}")
    private String environmentName;

    private final BuildProperties buildProperties;

    public SwaggerConfig(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public OpenAPI openAPI() {
        // Create environment badge based on active profile
        String environmentBadge = "";
        String badgeColor = "#3498db"; // Default blue

        if (activeProfile.contains("prod")) {
            environmentBadge = "PRODUCTION";
            badgeColor = "#e74c3c"; // Red
        } else if (activeProfile.contains("dev")) {
            environmentBadge = "DEVELOPMENT";
            badgeColor = "#2ecc71"; // Green
        } else if (activeProfile.contains("test")) {
            environmentBadge = "TEST";
            badgeColor = "#f39c12"; // Orange
        } else if (activeProfile.contains("local")) {
            environmentBadge = "LOCAL";
            badgeColor = "#3498db"; // Blue
        }

        // Create title with environment badge
        String title = "Spring Boot Training API";
        if (!environmentBadge.isEmpty()) {
            title = title + " - " + environmentBadge;
        }

        // Create description with environment details and colored badge
        String description = "Spring Boot Training Boilerplate API Documentation<br/><br/>" +
                "<div style='text-align: center; margin-bottom: 20px;'>" +
                "<span style='background-color: " + badgeColor + "; color: white; padding: 8px 15px; " +
                "border-radius: 4px; font-weight: bold; font-size: 16px; display: inline-block;'>" +
                "ENVIRONMENT: " + environmentBadge + "</span>" +
                "</div>" +
                "<div style='padding: 15px; background-color: #f0f0f0; border-radius: 5px; margin-bottom: 15px; border-left: 5px solid " + badgeColor + ";'>" +
                "<strong>Environment:</strong> " + environmentName + "<br/>" +
                "<strong>Profile:</strong> " + activeProfile + "<br/>" +
                "<strong>Version:</strong> " + buildProperties.getVersion() + "<br/>" +
                "<strong>Build Time:</strong> " + buildProperties.getTime() +
                "</div>" +
                (activeProfile.contains("prod") ?
                "<div style='padding: 10px; background-color: #ffebee; border-radius: 5px; border-left: 5px solid #e74c3c; margin-bottom: 10px;'>" +
                "<strong>⚠️ PRODUCTION ENVIRONMENT</strong><br/>" +
                "Be careful with any API calls you make here as they will affect production data!" +
                "</div>" : "");

        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(buildProperties.getVersion())
                        .contact(new Contact()
                                .name("Training Team")
                                .email("training@example.com")
                                .url("https://example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")));
    }
}
