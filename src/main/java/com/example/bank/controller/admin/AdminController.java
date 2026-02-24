package com.example.bank.controller.admin;

import com.example.bank.DTO.Admin.AdminLoginRequest;
import com.example.bank.DTO.Admin.AdminRegisterRequest;
import com.example.bank.DTO.Admin.AdminUpdateRequest;
import com.example.bank.DTO.Admin.ChangePasswordRequest;
import com.example.bank.DTO.SystemReportDTO;
import com.example.bank.model.Account;
import com.example.bank.model.Admin;
import com.example.bank.model.User;
import com.example.bank.services.admin.AdminService;
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
    public Admin createAdmin(@RequestBody AdminRegisterRequest request) {
        return adminService.createAdmin(request);
    }

    @PostMapping("/admin/login")
    public Long loginAdmin(@RequestBody AdminLoginRequest request) {
        return adminService.login(request.getEmail(), request.getPassword());
    }

    @GetMapping("/admin/{adminId}")
    public Admin getAdminById(@PathVariable Long adminId) {
        return adminService.getAdminById(adminId);
    }

    @PutMapping("/admin/{adminId}")
    public Admin updateAdmin(@PathVariable Long adminId, @RequestBody AdminUpdateRequest request) {
        return adminService.updateAdmin(adminId, request);
    }

    @DeleteMapping("/admin/{adminId}")
    public String deleteAdmin(@PathVariable Long adminId) {
        adminService.deleteAdmin(adminId);
        return "Admin deleted successfully";
    }

    @PostMapping("/admin/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request) {
        adminService.changePassword(request.getAdminId(), request.getOldPassword(), request.getNewPassword());
        return "Password changed successfully";
    }

    @GetMapping("/admin/report")
    public SystemReportDTO generateSystemReport() {
        return adminService.generateSystemReport();
    }

    // ========== User Management (by Admin) ==========

    @GetMapping("/api/admin/users/all")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/api/admin/users/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return adminService.getUserById(userId);
    }

    @PostMapping("/api/admin/users/{userId}/suspend")
    public String suspendUser(@PathVariable Long userId) {
        adminService.suspendUser(userId);
        return "User suspended successfully. User ID: " + userId;
    }

    @PostMapping("/api/admin/users/{userId}/activate")
    public String activateUser(@PathVariable Long userId) {
        adminService.activateUser(userId);
        return "User activated successfully. User ID: " + userId;
    }

    @GetMapping("/api/admin/users/{userId}/accounts")
    public List<Account> getUserAccounts(@PathVariable Long userId) {
        return adminService.getAccountsByUserId(userId);
    }
}
