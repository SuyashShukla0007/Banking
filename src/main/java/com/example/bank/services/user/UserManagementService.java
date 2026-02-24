package com.example.bank.services.user;

import com.example.bank.model.Account;
import com.example.bank.model.User;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service responsible for user management operations (profile updates,
 * deactivation, deletion)
 */
@Service
public class UserManagementService {

    private final UserRepository userRepository;
    private final AccountRepo accountRepo;

    public UserManagementService(UserRepository userRepository, AccountRepo accountRepo) {
        this.userRepository = userRepository;
        this.accountRepo = accountRepo;
    }

    /**
     * Delete user by ID
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Update user profile (name and/or email)
     */
    @Transactional
    public void updateUserProfile(Long userId, String name, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (email != null && !email.equals(user.getEmail())) {
            if (userRepository.findByEmail(email).isPresent()) {
                throw new RuntimeException("Email already in use by another user");
            }
            user.setEmail(email);
        }

        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }

        userRepository.save(user);
    }

    /**
     * Deactivate user and all their accounts
     */
    @Transactional
    public void deactivateAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user exists with this UserId"));

        user.setStatus("DEACTIVATED");

        List<Account> accounts = accountRepo.findByUserId(userId);
        for (Account account : accounts) {
            account.setStatus("INACTIVE");
        }

        accountRepo.saveAll(accounts);
        userRepository.save(user);
    }
}
