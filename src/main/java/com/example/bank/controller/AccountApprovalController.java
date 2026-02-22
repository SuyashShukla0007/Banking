package com.example.bank.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account-approvals")
public class AccountApprovalController {

    // Inject AccountApprovalService

    // Constructor injection

    // POST /account-approvals/request
    // Request Body: { "userId": Long, "accountType": String }
    // - Validate accountType is one of: SAVINGS, CHECKING, BUSINESS
    // - Call accountApprovalService.requestAccountApproval(userId, accountType)
    // - Creates a new AccountApproval with status PENDING
    // - Return success message or approval request details
    // - Handle exceptions:
    // - User not found
    // - User already has pending approval for this account type
    // - User already has this account type

    // GET /account-approvals/pending
    // - Call accountApprovalService.getAllPendingApprovals()
    // - Return list of all account approval requests with PENDING status
    // - Used by admins to see what needs approval
    // - Optional: Add pagination

    // GET /account-approvals/user/{userId}
    // Path Variable: userId
    // - Call accountApprovalService.getApprovalsByUserId(userId)
    // - Return list of all approval requests for the user
    // - Shows all statuses: PENDING, APPROVED, REJECTED, CANCELLED
    // - Handle exception if user not found

    // GET /account-approvals/{approvalId}
    // Path Variable: approvalId
    // - Call accountApprovalService.getApprovalById(approvalId)
    // - Return specific approval request details
    // - Handle exception if approval not found

    // GET /account-approvals/status/{status}
    // Path Variable: status
    // - Validate status is one of: PENDING, APPROVED, REJECTED, CANCELLED
    // - Call accountApprovalService.getApprovalsByStatus(status)
    // - Return list of approval requests with the specified status
    // - Optional: Add pagination

    // POST /account-approvals/{approvalId}/approve
    // Path Variable: approvalId
    // Request Body: { "adminId": Long }
    // Optional Request Body fields: { "branchId": Long } if not already set
    // - Validate admin exists
    // - Call accountApprovalService.approveAccountRequest(approvalId, adminId)
    // - This creates the actual account and updates approval status to APPROVED
    // - Return success message or created account details
    // - Handle exceptions:
    // - Approval not found
    // - Admin not found
    // - Approval not in PENDING status
    // - Branch ID not set
    // - User already has this account type (double-check)

    // POST /account-approvals/{approvalId}/reject
    // Path Variable: approvalId
    // Request Body: { "adminId": Long, "reason": String }
    // - Validate admin exists
    // - Validate reason is provided (optional but recommended)
    // - Call accountApprovalService.rejectAccountRequest(approvalId, adminId,
    // reason)
    // - Updates approval status to REJECTED and stores rejection reason
    // - Return success message
    // - Handle exceptions:
    // - Approval not found
    // - Admin not found
    // - Approval not in PENDING status

    // DELETE /account-approvals/{approvalId}/cancel
    // Path Variable: approvalId
    // Request Body: { "userId": Long }
    // - Validate the approval belongs to the user
    // - Call accountApprovalService.cancelApprovalRequest(approvalId, userId)
    // - User cancels their own pending approval request
    // - Updates status to CANCELLED
    // - Return success message
    // - Handle exceptions:
    // - Approval not found
    // - Approval doesn't belong to user
    // - Approval not in PENDING status

}
