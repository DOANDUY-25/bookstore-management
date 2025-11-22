package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       HttpSession session) {
        
        User user = userRepository.findByUserName(username).orElse(null);
        
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUserName());
            session.setAttribute("role", user.getRole());
            return "redirect:/index.html";
        }
        
        return "redirect:/login.html?error";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String email,
                          @RequestParam String fullName,
                          @RequestParam(required = false) String phoneNumber,
                          @RequestParam(required = false) String address) {
        
        try {
            // Check if username already exists
            if (userRepository.findByUserName(username).isPresent()) {
                return "redirect:/register.html?error=" + java.net.URLEncoder.encode("Tên đăng nhập đã tồn tại!", "UTF-8");
            }
            
            // Check if email already exists
            if (userRepository.findByEmail(email).isPresent()) {
                return "redirect:/register.html?error=" + java.net.URLEncoder.encode("Email đã được sử dụng!", "UTF-8");
            }
            
            // Create new user
            User newUser = new User();
            newUser.setUserName(username);
            newUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            newUser.setEmail(email);
            newUser.setFullName(fullName);
            newUser.setPhone(phoneNumber);
            newUser.setAddress(address);
            newUser.setRole("USER");
            
            userRepository.save(newUser);
            
            return "redirect:/login.html?success=true";
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return "redirect:/register.html?error=" + java.net.URLEncoder.encode("Đã xảy ra lỗi: " + e.getMessage(), "UTF-8");
            } catch (Exception ex) {
                return "redirect:/register.html?error=Unknown+error";
            }
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login.html?logout";
    }
}
