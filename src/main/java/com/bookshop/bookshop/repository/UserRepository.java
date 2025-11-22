package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    /**
     * Find user by username
     * @param userName the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUserName(String userName);
    
    /**
     * Check if username already exists
     * @param userName the username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUserName(String userName);
    
    /**
     * Find user by email
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
}
