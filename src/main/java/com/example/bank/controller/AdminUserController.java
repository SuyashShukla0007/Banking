package com.example.bank.controller;

import com.example.bank.model.Account;
import com.example.bank.model.User;
import com.example.bank.services.admin.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminService adminService;

    public AdminUserController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Get all users in the system
     * GET /api/admin/users/all
     */
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    /**
     * Get user details by ID
     * GET /api/admin/users/{userId}
     */
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        try {
            return adminService.getUserById(userId);
        } catch (RuntimeException e) {
            throw new RuntimeException("User not found: " + e.getMessage());
        }
    }

    /**
     * Suspend a user account
     * POST /api/admin/users/{userId}/suspend
     */
    @PostMapping("/{userId}/suspend")
    public String suspendUser(@PathVariable Long userId) {
        try {
            adminService.suspendUser(userId);
            return "User suspended successfully. User ID: " + userId;
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to suspend user: " + e.getMessage());
        }
    }

    /**
     * 🎓 LEARNING EXERCISE: Implement the activate user endpoint
     * POST /api/admin/users/{userId}/activate
     * 
     * TODO: Follow the pattern from suspendUser() above
     * Hints:
     * 1. Add @PostMapping annotation with "/{userId}/activate"
     * 2. Call adminService.activateUser(userId)
     * 3. Return success message
     * 4. Handle exceptions in try-catch
     */
    @PostMapping("/{userId}/activate")
    public String activateUser(@PathVariable Long userId) {
        try {
            adminService.activateUser(userId);
            return "User activated successfully. User ID: " + userId;
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to activate user: " + e.getMessage());
        }
    }

    /**
     * Get all accounts for a specific user
     * GET /api/admin/users/{userId}/accounts
     */
    @GetMapping("/{userId}/accounts")
    public List<Account> getUserAccounts(@PathVariable Long userId) {
        try {
            return adminService.getAccountsByUserId(userId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to fetch user accounts: " + e.getMessage());
        }
    }
}
