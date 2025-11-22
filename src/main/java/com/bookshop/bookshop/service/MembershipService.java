package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.MembershipCreateDTO;
import com.bookshop.bookshop.entity.Membership;

import java.util.List;
import java.util.Optional;

public interface MembershipService {
    
    Membership createMembership(Integer userId, MembershipCreateDTO dto);
    
    Optional<Membership> getActiveMembership(Integer userId);
    
    List<Membership> getAllMemberships();
    
    Membership updateMembershipStatus(Integer membershipId, String status);
    
    Membership extendMembership(Integer membershipId, Integer additionalMonths);
    
    void checkAndExpireMemberships();
    
    void deleteMembership(Integer membershipId);
}
