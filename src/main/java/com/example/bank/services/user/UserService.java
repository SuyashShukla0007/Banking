package com.example.bank.services.user;

import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.model.Account;
import com.example.bank.model.NewUserApproval;
import com.example.bank.model.User;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Facade service that delegates to specific user services
 * This maintains backward compatibility while following SRP
 */
@Service
public class UserService {

    private final UserRegistrationService userRegistrationService;
    private final UserAuthenticationService userAuthenticationService;
    private final UserManagementService userManagementService;
    private final UserQueryService userQueryService;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepo accountRepo;
    private final UserRepository userRepository;

    public UserService(
            UserRegistrationService userRegistrationService,
            UserAuthenticationService userAuthenticationService,
            UserManagementService userManagementService,
            UserQueryService userQueryService,
            PasswordEncoder passwordEncoder,
            AccountRepo accountRepo,
            UserRepository userRepository) {
        this.userRegistrationService = userRegistrationService;
        this.userAuthenticationService = userAuthenticationService;
        this.userManagementService = userManagementService;
        this.userQueryService = userQueryService;
        this.passwordEncoder = passwordEncoder;
        this.accountRepo = accountRepo;
        this.userRepository = userRepository;
    }

    public NewUserApproval createUser(UserRegisterRequest request) {
        return userRegistrationService.createUser(request);
    }

    /**
     * Authenticate user
     * 
     * @param email    User email
     * @param password User password
     * @return User ID if authentication successful
     */
    public Long login(String email, String password) {
        return userAuthenticationService.login(email, password);
    }

    /**
     * Delete user by ID
     * 
     * @param id User ID
     */
    public void deleteUser(Long id) {
        userManagementService.deleteUser(id);
    }

    /**
     * Get user by ID
     * 
     * @param id User ID
     * @return User entity or null
     */
    public User getUserById(Long id) {
        return userQueryService.getUserById(id);
    }

    // Method: updateUserProfile(Long userId, String name, String email)
    // - Find user by ID
    // - Validate email is not already taken by another user
    // - Update name and email if provided
    // - Save and return updated user
    @Transactional
    public void updateUserProfile(Long userId, String name, String email) {
        User user = userQueryService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // Check if email is being changed and if new email is already taken
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

    // Method: changePassword(Long userId, String oldPassword, String newPassword)
    // - Find user by ID
    // - Verify old password matches
    // - Encode new password
    // - Update user password
    // - Save user
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userQueryService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // Verify old password using passwordEncoder.matches()
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Encode and set new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Method: deactivateAccount(Long userId)
    // - Find user by ID
    // - Update user status to "DEACTIVATED"
    // - Mark all user accounts as inactive
    // - Save changes

    // 🎓 LEARNING EXERCISE: Implement this method following the pattern above
    // TODO: Complete this method - Reference the suspendUser() method in
    // AdminService for similar logic
    // Hints:
    // 1. Find the user by ID (use userQueryService)
    // 2. Set user status to "DEACTIVATED"
    // 3. Find all accounts for this user (accountRepo.findByUserId)
    // 4. Set each account status to "INACTIVE"
    // 5. Save everything using saveAll() and save()
    @Transactional
    public void deactivateAccount(Long userId) {
       
        User user =userRepository.findById(userId).orElseThrow(()-> new RuntimeException("No user exists with this UserId"));
        
        user.setStatus("DEACTIVATED");
        
        List<Account> accounts = accountRepo.findByUserId(userId);

        for (Account account : accounts) {
            account.setStatus("INACTIVE");
        }

        accountRepo.saveAll(accounts);
        userRepository.save(user);


    }

}
