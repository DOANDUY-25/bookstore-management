package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.dto.UserUpdateDTO;
import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users/list";
    }
    
    @GetMapping("/{id}")
    public String viewUserDetails(@PathVariable Integer id, Model model) {
        User user = userService.findByUserName(
            userService.getAllUsers().stream()
                .filter(u -> u.getUserId().equals(id))
                .findFirst()
                .orElseThrow()
                .getUserName()
        );
        model.addAttribute("user", user);
        return "admin/users/details";
    }
    
    @PostMapping("/{id}/update-role")
    public String updateUserRole(@PathVariable Integer id,
                                 @RequestParam String role,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserRole(id, role);
            redirectAttributes.addFlashAttribute("success", "User role updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users/" + id;
    }
    
    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable Integer id,
                            @Valid @ModelAttribute UserUpdateDTO dto,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Validation failed");
            return "redirect:/admin/users/" + id;
        }
        
        try {
            userService.updateUser(id, dto);
            redirectAttributes.addFlashAttribute("success", "User updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users/" + id;
    }
}
