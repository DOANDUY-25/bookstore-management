package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.entity.Membership;
import com.bookshop.bookshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    
    /**
     * Find active membership for a specific user
     * @param user the user to find membership for
     * @param status the membership status (e.g., "ACTIVE")
     * @return Optional containing the membership if found
     */
    Optional<Membership> findByUserAndStatus(User user, String status);
    
    /**
     * Find all memberships by status
     * @param status the membership status to filter by
     * @return list of memberships with the specified status
     */
    List<Membership> findByStatus(String status);
}
