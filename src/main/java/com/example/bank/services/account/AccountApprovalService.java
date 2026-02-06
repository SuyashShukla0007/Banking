package com.example.bank.services.account;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.bank.model.AccountApproval;
import com.example.bank.model.User;
import com.example.bank.repo.AccountApprovals;
import com.example.bank.repo.UserRepository;

@Service
public class AccountApprovalService {

    // Inject AccountApprovals repository
    private final AccountApprovals accountApprovals;
    // Inject UserRepository
    private final UserRepository userRepository;

    public AccountApprovalService(AccountApprovals accountApprovals, UserRepository userRepository) {
        this.accountApprovals = accountApprovals;
        this.userRepository = userRepository;
    }

    // Method: requestAccountApproval(Long userId, String accountType)
    // - Validate user exists
    // - Check if user already has pending approval for same account type
    // - Create new AccountApproval with status "PENDING"
    // - Set requestedAt timestamp
    // - Save and return approval request

    public void requestAccountApproval(Long userId, String accountType) {
        // Implementation goes here
        if (userRepository.findById(userId).isEmpty()) {
            return;
        } else {
            User user = userRepository.findById(userId).orElse(null);
            // Check for existing pending approval logic here
            List<AccountApproval> existingApprovals = accountApprovals.findByUserId(userId);
            
            for (AccountApproval approval : existingApprovals) {
                if (approval.getAccountType().equals(accountType) && approval.getStatus().equals("PENDING")) {
                    return; // Existing pending approval found
                }
            }
            AccountApproval newApproval = new AccountApproval();
            newApproval.setUser(user);
            newApproval.setAccountType(accountType);
            newApproval.setStatus("PENDING");
            newApproval.setRequestedAt(LocalDateTime.now());
            accountApprovals.save(newApproval);
        }
    }

    // Method: getAllPendingApprovals()
    // - Find all approvals with status "PENDING"
    // - Return list of pending approvals

    // Method: getApprovalsByUserId(Long userId)
    // - Find all approval requests for given user
    // - Return list of approvals

    // Method: approveAccountRequest(Long approvalId, Long adminId)
    // - Find approval by ID
    // - Validate approval exists and status is "PENDING"
    // - Update status to "APPROVED"
    // - Set reviewedAt timestamp
    // - Create actual Account for the user
    // - Save approval and account
    // - Return approval details

    // Method: rejectAccountRequest(Long approvalId, Long adminId, String reason)
    // - Find approval by ID
    // - Validate approval exists and status is "PENDING"
    // - Update status to "REJECTED"
    // - Set reviewedAt timestamp
    // - Save rejection reason (add field if needed)
    // - Save and return approval details

    // Method: getApprovalById(Long approvalId)
    // - Find approval by ID
    // - Return approval details

    // Method: cancelApprovalRequest(Long approvalId, Long userId)
    // - Find approval by ID
    // - Validate approval belongs to user
    // - Validate status is "PENDING"
    // - Update status to "CANCELLED"
    // - Save and return approval

    // Method: getApprovalsByStatus(String status)
    // - Find all approvals with given status (PENDING/APPROVED/REJECTED/CANCELLED)
    // - Return list of approvals

}
