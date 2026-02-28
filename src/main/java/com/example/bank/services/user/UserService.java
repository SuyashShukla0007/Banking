package com.example.bank.services.user;

import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.model.NewUserApproval;
import com.example.bank.model.User;
import org.springframework.stereotype.Service;

/**
 * Facade service that delegates to specialized user sub-services.
 * Controllers should only interact with this class.
 */
@Service
public class UserService {

    private final UserAuthenticationService userAuthenticationService;
    private final UserManagementService userManagementService;
    private final UserQueryService userQueryService;
    private final NewUserApprovalService newUserApprovalService;

    public UserService(
            UserAuthenticationService userAuthenticationService,
            UserManagementService userManagementService,
            UserQueryService userQueryService,
            NewUserApprovalService newUserApprovalService) {
        this.userAuthenticationService = userAuthenticationService;
        this.userManagementService = userManagementService;
        this.userQueryService = userQueryService;
        this.newUserApprovalService = newUserApprovalService;
    }

    // ========== Registration ==========

    public NewUserApproval createUser(UserRegisterRequest request) {
        return newUserApprovalService.createUserApprovalRequest(request);
    }

    // ========== Authentication ==========

    public Long login(String email, String password) {
        return userAuthenticationService.login(email, password);
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        userAuthenticationService.changePassword(userId, oldPassword, newPassword);
    }

    // ========== Query ==========

    public User getUserById(Long id) {
        return userQueryService.getUserById(id);
    }

    // ========== Management ==========

    public void deleteUser(Long id) {
        userManagementService.deleteUser(id);
    }

    public void updateUserProfile(Long userId, String name, String email) {
        userManagementService.updateUserProfile(userId, name, email);
    }

    public void deactivateAccount(Long userId) {
        userManagementService.deactivateAccount(userId);
    }
}
