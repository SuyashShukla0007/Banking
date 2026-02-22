package com.example.bank.controller;

import com.example.bank.model.NewUserApproval;
import com.example.bank.model.User;
import com.example.bank.services.user.NewUserApprovalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user-approvals")
public class UserApprovalController {

    private final NewUserApprovalService approvalService;

    public UserApprovalController(NewUserApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    /**
     * Get all pending user approval requests
     * GET /api/admin/user-approvals/pending
     */
    @GetMapping("/pending")
    public List<NewUserApproval> getPendingApprovals() {
        return approvalService.getAllPendingUserApprovals();
    }

    /**
     * Approve a user registration request
     * POST /api/admin/user-approvals/{approvalId}/approve?adminId=123
     */
    @PostMapping("/{approvalId}/approve")
    public String approveUser(@PathVariable Long approvalId, @RequestParam Long adminId) {
        try {
            User user = approvalService.approveUserRegistration(approvalId, adminId);
            return "User approved successfully. User ID: " + user.getId();
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to approve user: " + e.getMessage());
        }
    }

    /**
     * Reject a user registration request
     * POST
     * /api/admin/user-approvals/{approvalId}/reject?adminId=123&reason=duplicate
     */
    @PostMapping("/{approvalId}/reject")
    public String rejectUser(@PathVariable Long approvalId,
            @RequestParam Long adminId,
            @RequestParam String reason) {
        try {
            approvalService.rejectUserRegistration(approvalId, adminId, reason);
            return "User registration rejected. Reason: " + reason;
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to reject user: " + e.getMessage());
        }
    }

    /**
     * Get approval details by ID
     * GET /api/admin/user-approvals/{approvalId}
     */
    @GetMapping("/{approvalId}")
    public NewUserApproval getApprovalById(@PathVariable Long approvalId) {
        try {
            return approvalService.getApprovalById(approvalId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Approval not found: " + e.getMessage());
        }
    }

    /**
     * Get approval by email
     * GET /api/admin/user-approvals/by-email?email=user@example.com
     */
    @GetMapping("/by-email")
    public NewUserApproval getApprovalByEmail(@RequestParam String email) {
        try {
            return approvalService.getApprovalByEmail(email);
        } catch (RuntimeException e) {
            throw new RuntimeException("Approval not found: " + e.getMessage());
        }
    }
}
