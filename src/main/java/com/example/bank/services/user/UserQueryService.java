package com.example.bank.services.user;

import com.example.bank.model.User;
import com.example.bank.repo.userRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user query operations
 */
@Service
public class UserQueryService {

    private final userRepository userRepository;

    public UserQueryService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get user by ID
     * 
     * @param id User ID
     * @return User entity or null if not found
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
