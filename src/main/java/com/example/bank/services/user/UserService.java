package com.example.bank.services.user;

import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.model.NewUserApproval;
import com.example.bank.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Facade service that delegates to specialized user sub-services.
 * Controllers should only interact with this class.
 */
@Service
public class UserService {

    private final UserRegistrationService userRegistrationService;
    private final UserAuthenticationService userAuthenticationService;
    private final UserManagementService userManagementService;
    private final UserQueryService userQueryService;
    private final NewUserApprovalService newUserApprovalService;

    public UserService(
            UserRegistrationService userRegistrationService,
            UserAuthenticationService userAuthenticationService,
            UserManagementService userManagementService,
            UserQueryService userQueryService,
            NewUserApprovalService newUserApprovalService) {
        this.userRegistrationService = userRegistrationService;
        this.userAuthenticationService = userAuthenticationService;
        this.userManagementService = userManagementService;
        this.userQueryService = userQueryService;
        this.newUserApprovalService = newUserApprovalService;
    }

    // ========== Registration ==========

    public NewUserApproval createUser(UserRegisterRequest request) {
        return userRegistrationService.createUser(request);
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

    // ========== User Approvals ==========

    public List<NewUserApproval> getAllPendingUserApprovals() {
        return newUserApprovalService.getAllPendingUserApprovals();
    }

    public User approveUserRegistration(Long approvalId, Long adminId) {
        return newUserApprovalService.approveUserRegistration(approvalId, adminId);
    }

    public void rejectUserRegistration(Long approvalId, Long adminId, String reason) {
        newUserApprovalService.rejectUserRegistration(approvalId, adminId, reason);
    }

    public NewUserApproval getApprovalById(Long approvalId) {
        return newUserApprovalService.getApprovalById(approvalId);
    }

    public NewUserApproval getApprovalByEmail(String email) {
        return newUserApprovalService.getApprovalByEmail(email);
    }
}
