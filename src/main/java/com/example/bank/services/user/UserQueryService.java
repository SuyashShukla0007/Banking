package com.example.bank.services.user;

import com.example.bank.model.User;
import com.example.bank.repo.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user query operations
 */
@Service
public class UserQueryService {

    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get user by ID
     * 
     * @param id User ID
     * @return User entity
     * @throws jakarta.persistence.EntityNotFoundException if user not found
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found with ID: " + id));
    }
}
