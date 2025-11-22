package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAuth(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                String username = auth.getName();
                User user = userRepository.findByUserName(username).orElse(null);
                
                if (user != null) {
                    response.put("authenticated", true);
                    response.put("userId", user.getUserId());
                    response.put("username", user.getUserName());
                    response.put("fullName", user.getFullName());
                    response.put("email", user.getEmail());
                    response.put("role", user.getRole());
                    return ResponseEntity.ok(response);
                }
            }
            
            response.put("authenticated", false);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("authenticated", false);
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                response.put("success", false);
                response.put("message", "Not authenticated");
                return ResponseEntity.status(401).body(response);
            }
            
            String username = auth.getName();
            User user = userService.findByUserName(username);
            
            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.status(404).body(response);
            }
            
            response.put("userId", user.getUserId());
            response.put("userName", user.getUserName());
            response.put("fullName", user.getFullName());
            response.put("email", user.getEmail());
            response.put("phone", user.getPhone());
            response.put("address", user.getAddress());
            response.put("role", user.getRole());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, String> updateData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                response.put("success", false);
                response.put("message", "Not authenticated");
                return ResponseEntity.status(401).body(response);
            }
            
            String username = auth.getName();
            User user = userService.findByUserName(username);
            
            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.status(404).body(response);
            }
            
            // Update user fields
            if (updateData.containsKey("fullName")) {
                user.setFullName(updateData.get("fullName"));
            }
            if (updateData.containsKey("email")) {
                user.setEmail(updateData.get("email"));
            }
            if (updateData.containsKey("phone")) {
                user.setPhone(updateData.get("phone"));
            }
            if (updateData.containsKey("address")) {
                user.setAddress(updateData.get("address"));
            }
            
            // Save updated user
            User updatedUser = userService.updateUser(user);
            
            response.put("success", true);
            response.put("message", "Profile updated successfully");
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", updatedUser.getUserId());
            userData.put("userName", updatedUser.getUserName());
            userData.put("fullName", updatedUser.getFullName());
            userData.put("email", updatedUser.getEmail());
            userData.put("phone", updatedUser.getPhone());
            userData.put("address", updatedUser.getAddress());
            userData.put("role", updatedUser.getRole());
            
            response.put("user", userData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
