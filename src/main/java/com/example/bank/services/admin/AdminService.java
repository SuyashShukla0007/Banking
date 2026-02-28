package com.example.bank.services.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

import com.example.bank.DTO.Admin.AdminRegisterRequest;
import com.example.bank.DTO.Admin.AdminUpdateRequest;
import com.example.bank.DTO.SystemReportDTO;
import com.example.bank.model.Account;
import com.example.bank.model.Admin;
import com.example.bank.model.NewUserApproval;
import com.example.bank.model.Transaction;
import com.example.bank.model.User;
import com.example.bank.services.user.NewUserApprovalService;

/**
 * Facade service that delegates to specialized admin sub-services.
 * Controllers should only interact with this class.
 */
@Service
public class AdminService {

    private final AdminRegistrationService adminRegistrationService;
    private final AdminAuthenticationService adminAuthenticationService;
    private final AdminQueryService adminQueryService;
    private final AdminManagementService adminManagementService;
    private final AdminUserManagementService adminUserManagementService;
    private final AdminReportService adminReportService;
    private final NewUserApprovalService newUserApprovalService;

    public AdminService(
            AdminRegistrationService adminRegistrationService,
            AdminAuthenticationService adminAuthenticationService,
            AdminQueryService adminQueryService,
            AdminManagementService adminManagementService,
            AdminUserManagementService adminUserManagementService,
            AdminReportService adminReportService,
            NewUserApprovalService newUserApprovalService) {
        this.adminRegistrationService = adminRegistrationService;
        this.adminAuthenticationService = adminAuthenticationService;
        this.adminQueryService = adminQueryService;
        this.adminManagementService = adminManagementService;
        this.adminUserManagementService = adminUserManagementService;
        this.adminReportService = adminReportService;
        this.newUserApprovalService = newUserApprovalService;
    }

    // ========== Registration ==========

    public Admin createAdmin(AdminRegisterRequest request) {
        return adminRegistrationService.createAdmin(request);
    }

    // ========== Authentication ==========

    public Long login(String email, String password) {
        return adminAuthenticationService.login(email, password);
    }

    public void changePassword(Long adminId, String oldPassword, String newPassword) {
        adminAuthenticationService.changePassword(adminId, oldPassword, newPassword);
    }

    // ========== Query ==========

    public Admin getAdminById(Long adminId) {
        return adminQueryService.getAdminById(adminId);
    }

    // ========== Management ==========

    public Admin updateAdmin(Long adminId, AdminUpdateRequest request) {
        return adminManagementService.updateAdmin(adminId, request);
    }

    public void deleteAdmin(Long adminId) {
        adminManagementService.deleteAdmin(adminId);
    }

    // ========== User Management ==========

    public List<User> getAllUsers() {
        return adminUserManagementService.getAllUsers();
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return adminUserManagementService.getAllUsers(pageable);
    }

    public User getUserById(Long userId) {
        return adminUserManagementService.getUserById(userId);
    }

    public void suspendUser(Long userId) {
        adminUserManagementService.suspendUser(userId);
    }

    public void activateUser(Long userId) {
        adminUserManagementService.activateUser(userId);
    }

    public List<Account> getAccountsByUserId(Long userId) {
        return adminUserManagementService.getAccountsByUserId(userId);
    }

    // ========== Reports ==========

    public List<Transaction> viewAllTransactions() {
        return adminReportService.viewAllTransactions();
    }

    public SystemReportDTO generateSystemReport() {
        return adminReportService.generateSystemReport();
    }

    // ========== User Registration Approvals ==========

    public List<NewUserApproval> getAllPendingUserApprovals() {
        return newUserApprovalService.getAllPendingUserApprovals();
    }

    public User approveUserRegistration(Long approvalId, Long adminId) {
        return newUserApprovalService.approveUserRegistration(approvalId, adminId);
    }

    public void rejectUserRegistration(Long approvalId, Long adminId, String reason) {
        newUserApprovalService.rejectUserRegistration(approvalId, adminId, reason);
    }

    public NewUserApproval getUserApprovalById(Long approvalId) {
        return newUserApprovalService.getApprovalById(approvalId);
    }

    public NewUserApproval getUserApprovalByEmail(String email) {
        return newUserApprovalService.getApprovalByEmail(email);
    }
}
