package com.example.bank.services.user;

import org.springframework.stereotype.Service;

@Service
public class NewUserApprovalService {

    // Inject NewUsers repository

    // Inject UserRepository

    // Inject PasswordEncoder

    // Method: createUserApprovalRequest(UserRegisterRequest request)
    // - Check if email already exists in users or pending approvals
    // - Create NewUserApproval with status "PENDING"
    // - Encode and set password
    // - Set requestedAt timestamp
    // - Save and return approval request

    // Method: getAllPendingUserApprovals()
    // - Find all user approvals with status "PENDING"
    // - Return list of pending user approvals

    // Method: approveUserRegistration(Long approvalId, Long adminId)
    // - Find approval by ID
    // - Validate approval exists and status is "PENDING"
    // - Create actual User from approval data
    // - Set user role to "USER"
    // - Save user
    // - Update approval status to "APPROVED"
    // - Set reviewedAt timestamp
    // - Save and return approval details

    // Method: rejectUserRegistration(Long approvalId, Long adminId, String reason)
    // - Find approval by ID
    // - Validate approval exists and status is "PENDING"
    // - Update status to "REJECTED"
    // - Set reviewedAt timestamp
    // - Save rejection reason
    // - Save and return approval details

    // Method: getApprovalById(Long approvalId)
    // - Find user approval by ID
    // - Return approval details

    // Method: getApprovalByEmail(String email)
    // - Find user approval by email
    // - Return approval if exists

    // Method: getApprovalsByStatus(String status)
    // - Find all user approvals with given status (PENDING/APPROVED/REJECTED)
    // - Return list of approvals

    // Method: deleteRejectedApprovals()
    // - Find all rejected approvals older than certain period
    // - Delete old rejected approval records
    // - Return count of deleted records

}
