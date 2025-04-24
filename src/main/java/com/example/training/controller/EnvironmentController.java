package com.example.training.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/environment")
public class EnvironmentController {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private final BuildProperties buildProperties;

    public EnvironmentController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getEnvironmentInfo() {
        Map<String, String> environmentInfo = new HashMap<>();
        environmentInfo.put("activeProfile", activeProfile);
        environmentInfo.put("version", buildProperties.getVersion());
        environmentInfo.put("buildTime", buildProperties.getTime().toString());
        
        // Add a human-readable environment name
        String environment = "Unknown";
        if (activeProfile.contains("prod")) {
            environment = "Production";
        } else if (activeProfile.contains("dev")) {
            environment = "Development";
        } else if (activeProfile.contains("test")) {
            environment = "Test";
        } else if (activeProfile.contains("local")) {
            environment = "Local Development";
        }
        environmentInfo.put("environment", environment);
        
        return ResponseEntity.ok(environmentInfo);
    }
}
