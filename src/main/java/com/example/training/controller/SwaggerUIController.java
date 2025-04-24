package com.example.training.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerUIController {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;
    
    @Value("${environment.name:Unknown}")
    private String environmentName;
    
    @GetMapping("/custom-swagger-ui")
    public String swaggerUI(Model model) {
        model.addAttribute("activeProfile", activeProfile);
        model.addAttribute("environmentName", environmentName);
        
        // Determine environment type for styling
        String environmentType = "default";
        if (activeProfile.contains("prod")) {
            environmentType = "production";
        } else if (activeProfile.contains("dev")) {
            environmentType = "development";
        } else if (activeProfile.contains("test")) {
            environmentType = "test";
        } else if (activeProfile.contains("local")) {
            environmentType = "local";
        }
        
        model.addAttribute("environmentType", environmentType);
        
        return "swagger-ui";
    }
}
