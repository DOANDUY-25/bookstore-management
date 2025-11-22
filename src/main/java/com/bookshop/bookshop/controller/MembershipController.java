package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.dto.MembershipCreateDTO;
import com.bookshop.bookshop.entity.Membership;
import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.service.MembershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/membership/api")
public class MembershipController {

    private final MembershipService membershipService;
    private final UserRepository userRepository;

    public MembershipController(MembershipService membershipService, UserRepository userRepository) {
        this.membershipService = membershipService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerMembership(
            @RequestBody MembershipCreateDTO dto,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập để đăng ký thẻ thành viên");
                return ResponseEntity.ok(response);
            }

            String username = authentication.getName();
            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Check if user already has an active membership
            Optional<Membership> existingMembership = membershipService.getActiveMembership(user.getUserId());
            if (existingMembership.isPresent()) {
                response.put("success", false);
                response.put("message", "Bạn đã có thẻ thành viên đang hoạt động");
                return ResponseEntity.ok(response);
            }

            Membership membership = membershipService.createMembership(user.getUserId(), dto);
            
            response.put("success", true);
            response.put("message", "Đăng ký thẻ thành viên thành công");
            response.put("membership", convertToDTO(membership));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentMembership(Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Optional<Membership> membership = membershipService.getActiveMembership(user.getUserId());
        
        if (membership.isPresent()) {
            return ResponseEntity.ok(convertToDTO(membership.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{membershipId}/extend")
    public ResponseEntity<Map<String, Object>> extendMembership(
            @PathVariable Integer membershipId,
            @RequestParam Integer months,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập");
                return ResponseEntity.ok(response);
            }

            Membership membership = membershipService.extendMembership(membershipId, months);
            
            response.put("success", true);
            response.put("message", "Gia hạn thẻ thành viên thành công");
            response.put("membership", convertToDTO(membership));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    private Map<String, Object> convertToDTO(Membership membership) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", membership.getId());
        dto.put("membershipType", membership.getMembershipType());
        dto.put("months", membership.getMonths());
        dto.put("bankType", membership.getBankType());
        dto.put("discountPercent", membership.getDiscountPercent());
        dto.put("totalAmount", membership.getTotalAmount());
        dto.put("createdAt", membership.getCreatedAt());
        dto.put("expiresAt", membership.getExpiresAt());
        dto.put("status", membership.getStatus());
        return dto;
    }
}
