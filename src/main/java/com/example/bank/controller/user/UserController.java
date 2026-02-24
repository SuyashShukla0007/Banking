package com.example.bank.controller.user;

import com.example.bank.DTO.User.UserLoginRequest;
import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.model.NewUserApproval;
import com.example.bank.model.User;
import com.example.bank.services.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ========== User CRUD ==========

    @GetMapping("/users/")
    public String test() {
        return "hello";
    }

    @PostMapping("/users/create")
    public String createUser(@RequestBody UserRegisterRequest request) {
        try {
            userService.createUser(request);
            return "Successfull";
        } catch (RuntimeException e) {
            throw new RuntimeException("user email already exists " + e);
        }
    }

    @PostMapping("/users/login")
    public String userLogin(@RequestBody UserLoginRequest request) {
        try {
            userService.login(request.getEmail(), request.getPassword());
            return "Login successful";
        } catch (RuntimeException e) {
            throw new RuntimeException("check credentials " + e);
        }
    }

    @GetMapping("/users/delete-user/{id}")
    public String delete(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return "user deleted successfully";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/users/{userId}/profile")
    public String updateProfile(@PathVariable Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        try {
            userService.updateUserProfile(userId, name, email);
            return "Profile updated successfully";
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to update profile: " + e.getMessage());
        }
    }

    @PostMapping("/users/{userId}/change-password")
    public String changePassword(@PathVariable Long userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            userService.changePassword(userId, oldPassword, newPassword);
            return "Password changed successfully";
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to change password: " + e.getMessage());
        }
    }

    @PostMapping("/users/{userId}/deactivate")
    public String deactivateAccount(@PathVariable Long userId) {
        try {
            userService.deactivateAccount(userId);
            return "Account deactivated successfully";
        } catch (UnsupportedOperationException e) {
            return "TODO: Complete the deactivateAccount() method in UserService first!";
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to deactivate account: " + e.getMessage());
        }
    }

    // ========== User Approvals (Admin operations) ==========

    @GetMapping("/api/admin/user-approvals/pending")
    public List<NewUserApproval> getPendingApprovals() {
        return userService.getAllPendingUserApprovals();
    }

    @PostMapping("/api/admin/user-approvals/{approvalId}/approve")
    public String approveUser(@PathVariable Long approvalId, @RequestParam Long adminId) {
        User user = userService.approveUserRegistration(approvalId, adminId);
        return "User approved successfully. User ID: " + user.getId();
    }

    @PostMapping("/api/admin/user-approvals/{approvalId}/reject")
    public String rejectUser(@PathVariable Long approvalId,
            @RequestParam Long adminId,
            @RequestParam String reason) {
        userService.rejectUserRegistration(approvalId, adminId, reason);
        return "User registration rejected. Reason: " + reason;
    }

    @GetMapping("/api/admin/user-approvals/{approvalId}")
    public NewUserApproval getApprovalById(@PathVariable Long approvalId) {
        return userService.getApprovalById(approvalId);
    }

    @GetMapping("/api/admin/user-approvals/by-email")
    public NewUserApproval getApprovalByEmail(@RequestParam String email) {
        return userService.getApprovalByEmail(email);
    }
}
