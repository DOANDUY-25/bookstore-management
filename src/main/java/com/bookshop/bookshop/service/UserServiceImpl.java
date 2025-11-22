package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.UserRegistrationDTO;
import com.bookshop.bookshop.dto.UserUpdateDTO;
import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.exception.DuplicateUserException;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public User registerUser(UserRegistrationDTO dto) {
        log.info("Registering new user: {}", dto.getUserName());
        
        // Check if username already exists
        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new DuplicateUserException(dto.getUserName(), true);
        }
        
        // Create new user
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setRole("USER");
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUserName());
        
        return savedUser;
    }
    
    @Override
    @Transactional(readOnly = true)
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userName", userName));
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }
    
    @Override
    public User updateUser(Integer userId, UserUpdateDTO dto) {
        log.info("Updating user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        if (dto.getFullName() != null) {
            user.setFullName(dto.getFullName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getAddress() != null) {
            user.setAddress(dto.getAddress());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getUserId());
        
        return updatedUser;
    }
    
    @Override
    public User updateUser(User user) {
        log.info("Updating user entity: {}", user.getUserId());
        User updatedUser = userRepository.save(user);
        log.info("User entity updated successfully: {}", updatedUser.getUserId());
        return updatedUser;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }
    
    @Override
    public User updateUserRole(Integer userId, String role) {
        log.info("Updating user role: {} to {}", userId, role);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setRole(role);
        
        User updatedUser = userRepository.save(user);
        log.info("User role updated successfully: {}", updatedUser.getUserId());
        
        return updatedUser;
    }
}
