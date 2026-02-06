package com.example.bank.services;

import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.model.NewUserApproval;
import com.example.bank.model.User;
import com.example.bank.services.user.UserAuthenticationService;
import com.example.bank.services.user.UserManagementService;
import com.example.bank.services.user.UserQueryService;
import com.example.bank.services.user.UserRegistrationService;
import org.springframework.stereotype.Service;

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

    public UserService(
            UserRegistrationService userRegistrationService,
            UserAuthenticationService userAuthenticationService,
            UserManagementService userManagementService,
            UserQueryService userQueryService) {
        this.userRegistrationService = userRegistrationService;
        this.userAuthenticationService = userAuthenticationService;
        this.userManagementService = userManagementService;
        this.userQueryService = userQueryService;
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

    // Method: changePassword(Long userId, String oldPassword, String newPassword)
    // - Find user by ID
    // - Verify old password matches
    // - Encode new password
    // - Update user password
    // - Save user

    // Method: deactivateAccount(Long userId)
    // - Find user by ID
    // - Update user status to "DEACTIVATED"
    // - Mark all user accounts as inactive
    // - Save changes

}
