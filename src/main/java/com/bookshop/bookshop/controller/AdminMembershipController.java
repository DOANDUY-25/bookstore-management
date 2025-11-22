package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.entity.Membership;
import com.bookshop.bookshop.service.MembershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/memberships")
public class AdminMembershipController {
    
    private final MembershipService membershipService;

    public AdminMembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }
    
    @GetMapping
    public String listMemberships(Model model) {
        List<Membership> memberships = membershipService.getAllMemberships();
        model.addAttribute("memberships", memberships);
        
        // Calculate statistics
        long activeCount = memberships.stream().filter(m -> "ACTIVE".equals(m.getStatus())).count();
        long expiredCount = memberships.stream().filter(m -> "EXPIRED".equals(m.getStatus())).count();
        
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("expiredCount", expiredCount);
        model.addAttribute("totalCount", memberships.size());
        
        return "admin/memberships/list";
    }
    
    @GetMapping("/{id}")
    public String viewMembershipDetails(@PathVariable Integer id, Model model) {
        Membership membership = membershipService.getAllMemberships().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow();
        model.addAttribute("membership", membership);
        return "admin/memberships/details";
    }
    
    @PostMapping("/{id}/update-status")
    public String updateMembershipStatus(@PathVariable Integer id,
                                        @RequestParam String status,
                                        RedirectAttributes redirectAttributes) {
        try {
            membershipService.updateMembershipStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Membership status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/memberships/" + id;
    }
    
    @PostMapping("/{id}/extend")
    public String extendMembership(@PathVariable Integer id,
                                  @RequestParam Integer months,
                                  RedirectAttributes redirectAttributes) {
        try {
            membershipService.extendMembership(id, months);
            redirectAttributes.addFlashAttribute("success", "Membership extended successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/memberships/" + id;
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteMembership(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            membershipService.deleteMembership(id);
            response.put("success", true);
            response.put("message", "Xóa thẻ thành viên thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
