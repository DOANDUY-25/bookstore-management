package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.MembershipCreateDTO;
import com.bookshop.bookshop.entity.Membership;
import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.repository.MembershipRepository;
import com.bookshop.bookshop.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MembershipServiceImpl implements MembershipService {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MembershipServiceImpl.class);
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

    public MembershipServiceImpl(MembershipRepository membershipRepository, UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public Membership createMembership(Integer userId, MembershipCreateDTO dto) {
        log.info("Creating membership for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setMembershipType(dto.getMembershipType());
        membership.setMonths(dto.getMonths());
        membership.setBankType(dto.getBankType());
        
        // Set discount and base price based on membership type
        BigDecimal discountPercent;
        BigDecimal basePrice;
        
        switch (dto.getMembershipType().toUpperCase()) {
            case "SILVER":
                discountPercent = BigDecimal.valueOf(5);
                basePrice = BigDecimal.valueOf(200000);
                break;
            case "GOLD":
                discountPercent = BigDecimal.valueOf(10);
                basePrice = BigDecimal.valueOf(400000);
                break;
            case "PLATINUM":
                discountPercent = BigDecimal.valueOf(15);
                basePrice = BigDecimal.valueOf(600000);
                break;
            default:
                discountPercent = BigDecimal.ZERO;
                basePrice = BigDecimal.valueOf(200000);
        }
        
        membership.setDiscountPercent(discountPercent);
        
        // Calculate total amount based on months with discount
        BigDecimal totalAmount = basePrice;
        
        // Apply discount for longer periods
        if (dto.getMonths() == 6) {
            totalAmount = totalAmount.multiply(BigDecimal.valueOf(0.9)); // 10% discount
        } else if (dto.getMonths() == 12) {
            totalAmount = totalAmount.multiply(BigDecimal.valueOf(0.8)); // 20% discount
        }
        
        membership.setTotalAmount(totalAmount);
        
        membership.setCreatedAt(LocalDateTime.now());
        membership.setExpiresAt(LocalDateTime.now().plusMonths(dto.getMonths()));
        membership.setStatus("ACTIVE");
        
        return membershipRepository.save(membership);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Membership> getActiveMembership(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return membershipRepository.findByUserAndStatus(user, "ACTIVE");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }
    
    @Override
    public Membership updateMembershipStatus(Integer membershipId, String status) {
        log.info("Updating membership {} status to {}", membershipId, status);
        
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership", "id", membershipId));
        
        membership.setStatus(status);
        return membershipRepository.save(membership);
    }
    
    @Override
    public Membership extendMembership(Integer membershipId, Integer additionalMonths) {
        log.info("Extending membership {} by {} months", membershipId, additionalMonths);
        
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership", "id", membershipId));
        
        membership.setExpiresAt(membership.getExpiresAt().plusMonths(additionalMonths));
        membership.setStatus("ACTIVE");
        
        return membershipRepository.save(membership);
    }
    
    @Override
    @Scheduled(cron = "0 0 0 * * *") // Run daily at midnight
    public void checkAndExpireMemberships() {
        log.info("Checking for expired memberships");
        
        List<Membership> activeMemberships = membershipRepository.findByStatus("ACTIVE");
        LocalDateTime now = LocalDateTime.now();
        
        for (Membership membership : activeMemberships) {
            if (membership.getExpiresAt().isBefore(now)) {
                membership.setStatus("EXPIRED");
                membershipRepository.save(membership);
                log.info("Membership {} expired", membership.getId());
            }
        }
    }
    
    @Override
    public void deleteMembership(Integer membershipId) {
        log.info("Deleting membership {}", membershipId);
        
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership", "id", membershipId));
        
        membershipRepository.delete(membership);
        log.info("Membership {} deleted successfully", membershipId);
    }
}
