package com.example.bank.controller.admin;

import com.example.bank.DTO.Admin.AdminLoginRequest;
import com.example.bank.DTO.Admin.AdminRegisterRequest;
import com.example.bank.DTO.Admin.AdminUpdateRequest;
import com.example.bank.DTO.Admin.ChangePasswordRequest;
import com.example.bank.DTO.SystemReportDTO;
import com.example.bank.DTO.response.ApiResponse;
import com.example.bank.model.Account;
import com.example.bank.model.Admin;
import com.example.bank.model.NewUserApproval;
import com.example.bank.model.User;
import com.example.bank.services.admin.AdminService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ========== Admin CRUD ==========

    @PostMapping("/admin/create")
    public ApiResponse<Admin> createAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        Admin admin = adminService.createAdmin(request);
        return ApiResponse.success("Admin created successfully", admin);
    }

    @PostMapping("/admin/login")
    public ApiResponse<Long> loginAdmin(@Valid @RequestBody AdminLoginRequest request) {
        Long adminId = adminService.login(request.getEmail(), request.getPassword());
        return ApiResponse.success("Login successful", adminId);
    }

    @GetMapping("/admin/{adminId}")
    public ApiResponse<Admin> getAdminById(@PathVariable Long adminId) {
        Admin admin = adminService.getAdminById(adminId);
        return ApiResponse.success("Admin fetched", admin);
    }

    @PutMapping("/admin/{adminId}")
    public ApiResponse<Admin> updateAdmin(@PathVariable Long adminId, @Valid @RequestBody AdminUpdateRequest request) {
        Admin admin = adminService.updateAdmin(adminId, request);
        return ApiResponse.success("Admin updated successfully", admin);
    }

    @DeleteMapping("/admin/{adminId}")
    public ApiResponse<Void> deleteAdmin(@PathVariable Long adminId) {
        adminService.deleteAdmin(adminId);
        return ApiResponse.success("Admin deleted successfully");
    }

    @PostMapping("/admin/change-password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        adminService.changePassword(request.getAdminId(), request.getOldPassword(), request.getNewPassword());
        return ApiResponse.success("Password changed successfully");
    }

    @GetMapping("/admin/report")
    public ApiResponse<SystemReportDTO> generateSystemReport() {
        SystemReportDTO report = adminService.generateSystemReport();
        return ApiResponse.success("System report generated", report);
    }

    // ========== User Management (by Admin) ==========

    @GetMapping("/api/admin/users/all")
    public ApiResponse<Page<User>> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        Page<User> users = adminService.getAllUsers(pageable);
        return ApiResponse.success("All users fetched", users);
    }

    @GetMapping("/api/admin/users/{userId}")
    public ApiResponse<User> getUserById(@PathVariable Long userId) {
        User user = adminService.getUserById(userId);
        return ApiResponse.success("User fetched", user);
    }

    @PostMapping("/api/admin/users/{userId}/suspend")
    public ApiResponse<Void> suspendUser(@PathVariable Long userId) {
        adminService.suspendUser(userId);
        return ApiResponse.success("User suspended successfully");
    }

    @PostMapping("/api/admin/users/{userId}/activate")
    public ApiResponse<Void> activateUser(@PathVariable Long userId) {
        adminService.activateUser(userId);
        return ApiResponse.success("User activated successfully");
    }

    @GetMapping("/api/admin/users/{userId}/accounts")
    public ApiResponse<List<Account>> getUserAccounts(@PathVariable Long userId) {
        List<Account> accounts = adminService.getAccountsByUserId(userId);
        return ApiResponse.success("User accounts fetched", accounts);
    }

    // ========== User Registration Approvals ==========

    @GetMapping("/api/admin/user-approvals/pending")
    public ApiResponse<List<NewUserApproval>> getPendingApprovals() {
        List<NewUserApproval> approvals = adminService.getAllPendingUserApprovals();
        return ApiResponse.success("Pending approvals fetched", approvals);
    }

    @PostMapping("/api/admin/user-approvals/{approvalId}/approve")
    public ApiResponse<User> approveUser(@PathVariable Long approvalId, @RequestParam Long adminId) {
        User user = adminService.approveUserRegistration(approvalId, adminId);
        return ApiResponse.success("User approved successfully", user);
    }

    @PostMapping("/api/admin/user-approvals/{approvalId}/reject")
    public ApiResponse<Void> rejectUser(@PathVariable Long approvalId,
            @RequestParam Long adminId,
            @RequestParam String reason) {
        adminService.rejectUserRegistration(approvalId, adminId, reason);
        return ApiResponse.success("User registration rejected. Reason: " + reason);
    }

    @GetMapping("/api/admin/user-approvals/{approvalId}")
    public ApiResponse<NewUserApproval> getApprovalById(@PathVariable Long approvalId) {
        NewUserApproval approval = adminService.getUserApprovalById(approvalId);
        return ApiResponse.success("Approval fetched", approval);
    }

    @GetMapping("/api/admin/user-approvals/by-email")
    public ApiResponse<NewUserApproval> getApprovalByEmail(@RequestParam String email) {
        NewUserApproval approval = adminService.getUserApprovalByEmail(email);
        return ApiResponse.success("Approval fetched", approval);
    }
}
