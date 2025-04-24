package com.example.training.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EnvironmentLogger {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private final BuildProperties buildProperties;

    public EnvironmentLogger(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @PostConstruct
    public void logEnvironmentInfo() {
        String environment = "Unknown";
        if (activeProfile.contains("prod")) {
            environment = "PRODUCTION";
        } else if (activeProfile.contains("dev")) {
            environment = "DEVELOPMENT";
        } else if (activeProfile.contains("test")) {
            environment = "TEST";
        } else if (activeProfile.contains("local")) {
            environment = "LOCAL DEVELOPMENT";
        }

        log.info("=============================================================");
        log.info("Application Name: {}", buildProperties.getName());
        log.info("Application Version: {}", buildProperties.getVersion());
        log.info("Build Time: {}", buildProperties.getTime());
        log.info("Active Profile: {}", activeProfile);
        log.info("Environment: {}", environment);
        log.info("=============================================================");
    }
}
