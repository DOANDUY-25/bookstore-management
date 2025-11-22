package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/setup")
public class SetupController {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping("/create-admin")
    public Map<String, String> createAdmin() {
        Map<String, String> response = new HashMap<>();
        
        try {
            // Check if admin already exists
            if (userRepository.findByUserName("admin").isPresent()) {
                response.put("status", "exists");
                response.put("message", "Admin user already exists. Use /setup/reset-admin-password to reset password");
                return response;
            }
            
            // Create new admin user
            User admin = new User();
            admin.setUserName("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setFullName("Administrator");
            admin.setEmail("admin@bookshop.com");
            admin.setAddress("Hà Nội");
            admin.setPhone("0123456789");
            
            userRepository.save(admin);
            
            response.put("status", "success");
            response.put("message", "Admin user created successfully!");
            response.put("username", "admin");
            response.put("password", "admin123");
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error creating admin: " + e.getMessage());
        }
        
        return response;
    }
    
    @GetMapping("/reset-admin-password")
    public Map<String, String> resetAdminPassword() {
        Map<String, String> response = new HashMap<>();
        
        try {
            User admin = userRepository.findByUserName("admin")
                .orElseThrow(() -> new RuntimeException("Admin user not found"));
            
            // Reset password to admin123
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN"); // Ensure role is ADMIN
            userRepository.save(admin);
            
            response.put("status", "success");
            response.put("message", "Admin password reset successfully!");
            response.put("username", "admin");
            response.put("password", "admin123");
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error resetting password: " + e.getMessage());
        }
        
        return response;
    }
}
