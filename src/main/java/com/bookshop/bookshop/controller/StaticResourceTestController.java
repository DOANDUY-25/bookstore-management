package com.bookshop.bookshop.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;

@Controller
public class StaticResourceTestController {

    @GetMapping("/test-css")
    @ResponseBody
    public ResponseEntity<String> testCss() throws IOException {
        Resource resource = new ClassPathResource("static/css/style.css");
        if (resource.exists()) {
            String content = new String(Files.readAllBytes(resource.getFile().toPath()));
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("CSS file exists! Size: " + content.length() + " bytes\n\nFirst 500 chars:\n" + 
                          content.substring(0, Math.min(500, content.length())));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/test-static")
    @ResponseBody
    public String testStatic() {
        StringBuilder sb = new StringBuilder();
        sb.append("Static Resources Test:\n\n");
        
        String[] files = {
            "static/css/style.css",
            "static/css/custom.css",
            "static/css/animations.css",
            "static/css/responsive.css",
            "static/js/custom.js",
            "static/js/bookshop.js"
        };
        
        for (String file : files) {
            Resource resource = new ClassPathResource(file);
            sb.append(file).append(": ");
            if (resource.exists()) {
                try {
                    long size = resource.contentLength();
                    sb.append("✅ EXISTS (").append(size).append(" bytes)\n");
                } catch (IOException e) {
                    sb.append("❌ ERROR: ").append(e.getMessage()).append("\n");
                }
            } else {
                sb.append("❌ NOT FOUND\n");
            }
        }
        
        return sb.toString();
    }
}
