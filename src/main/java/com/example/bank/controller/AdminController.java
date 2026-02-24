package com.example.bank.controller;

import com.example.bank.DTO.Admin.AdminLoginRequest;
import com.example.bank.DTO.Admin.AdminRegisterRequest;
import com.example.bank.DTO.Admin.AdminUpdateRequest;
import com.example.bank.DTO.Admin.ChangePasswordRequest;
import com.example.bank.DTO.SystemReportDTO;
import com.example.bank.model.Admin;
import com.example.bank.services.admin.AdminAuthenticationService;
import com.example.bank.services.admin.AdminRegistrationService;
import com.example.bank.services.admin.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final AdminRegistrationService adminRegistrationService;
    private final AdminAuthenticationService adminAuthenticationService;

    public AdminController(AdminService adminService,
            AdminRegistrationService adminRegistrationService,
            AdminAuthenticationService adminAuthenticationService) {
        this.adminService = adminService;
        this.adminRegistrationService = adminRegistrationService;
        this.adminAuthenticationService = adminAuthenticationService;
    }

    @PostMapping("/create")
    public Admin createAdmin(@RequestBody AdminRegisterRequest request) {
        return adminRegistrationService.createAdmin(request);
    }

    @PostMapping("/login")
    public Long loginAdmin(@RequestBody AdminLoginRequest request) {
        return adminAuthenticationService.login(request.getEmail(), request.getPassword());
    }

    @GetMapping("/{adminId}")
    public Admin getAdminById(@PathVariable Long adminId) {
        return adminService.getAdminById(adminId);
    }

    @PutMapping("/{adminId}")
    public Admin updateAdmin(@PathVariable Long adminId, @RequestBody AdminUpdateRequest request) {
        return adminService.updateAdmin(adminId, request);
    }

    @DeleteMapping("/{adminId}")
    public String deleteAdmin(@PathVariable Long adminId) {
        adminService.deleteAdmin(adminId);
        return "Admin deleted successfully";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request) {
        adminService.changePassword(request.getAdminId(), request.getOldPassword(), request.getNewPassword());
        return "Password changed successfully";
    }

    @GetMapping("/report")
    public SystemReportDTO generateSystemReport() {
        return adminService.generateSystemReport();
    }
}
