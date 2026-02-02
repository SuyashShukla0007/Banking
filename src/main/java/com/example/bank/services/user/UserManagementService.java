package com.example.bank.services.user;

import com.example.bank.repo.userRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user management operations (CRUD)
 */
@Service
public class UserManagementService {

    private final userRepository userRepository;

    public UserManagementService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Delete user by ID
     * 
     * @param id User ID
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
