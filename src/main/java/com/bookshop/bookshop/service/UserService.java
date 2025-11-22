package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.UserRegistrationDTO;
import com.bookshop.bookshop.dto.UserUpdateDTO;
import com.bookshop.bookshop.entity.User;

import java.util.List;

/**
 * Service interface for User operations
 */
public interface UserService {
    
    /**
     * Register a new user
     * @param dto user registration data
     * @return the created user
     */
    User registerUser(UserRegistrationDTO dto);
    
    /**
     * Find user by username
     * @param userName the username to search for
     * @return the user if found
     */
    User findByUserName(String userName);
    
    /**
     * Check if username exists
     * @param userName the username to check
     * @return true if exists, false otherwise
     */
    boolean existsByUserName(String userName);
    
    /**
     * Update user information
     * @param userId the user ID
     * @param dto user update data
     * @return the updated user
     */
    User updateUser(Integer userId, UserUpdateDTO dto);
    
    /**
     * Update user entity directly
     * @param user the user entity to update
     * @return the updated user
     */
    User updateUser(User user);
    
    /**
     * Get all users (admin only)
     * @return list of all users
     */
    List<User> getAllUsers();
    
    /**
     * Update user role (admin only)
     * @param userId the user ID
     * @param role the new role
     * @return the updated user
     */
    User updateUserRole(Integer userId, String role);
}
