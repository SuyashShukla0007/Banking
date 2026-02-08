package com.example.bank.services.user;

import com.example.bank.repo.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user management operations (CRUD)
 */
@Service
public class UserManagementService {

    private final UserRepository userRepository;

    public UserManagementService(UserRepository userRepository) {
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
