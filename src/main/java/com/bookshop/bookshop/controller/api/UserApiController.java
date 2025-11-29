package com.bookshop.bookshop.controller.api;

import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/public/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserApiController {
    
    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getAllUsers());
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.findByUserName(username));
    }
}
