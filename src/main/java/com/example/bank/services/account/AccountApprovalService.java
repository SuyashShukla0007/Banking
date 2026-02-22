package com.example.bank.services.account;

import com.example.bank.utility.GenerateAccountNumber;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.model.Account;
import com.example.bank.model.AccountApproval;
import com.example.bank.model.User;
import com.example.bank.repo.AccountApprovals;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.UserRepository;

@Service

public class AccountApprovalService {

    private final AccountApprovals accountApprovals;
    private final UserRepository userRepository;
    private final AccountRepo accounts;
    private final GenerateAccountNumber generateAccountNumber;

    private static final List<String> VALID_ACCOUNT_TYPES = Arrays.asList("SAVINGS", "CHECKING", "BUSINESS");

    public AccountApprovalService(AccountApprovals accountApprovals, UserRepository userRepository,
            AccountRepo accounts, GenerateAccountNumber generateAccountNumber) {
        this.accountApprovals = accountApprovals;
        this.userRepository = userRepository;
        this.accounts = accounts;
        this.generateAccountNumber = generateAccountNumber;
    }

    @Transactional
    public void requestAccountApproval(Long userId, String accountType) {
        // Validate account type
        if (!VALID_ACCOUNT_TYPES.contains(accountType)) {
            throw new RuntimeException("Invalid account type. Must be one of: " + VALID_ACCOUNT_TYPES);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Check for existing pending approvals
        List<AccountApproval> existingApprovals = accountApprovals.findByUserId(userId);
        for (AccountApproval approval : existingApprovals) {
            if (approval.getAccountType().equals(accountType) && approval.getStatus().equals("PENDING")) {
                throw new RuntimeException("User already has a pending approval for " + accountType + " account");
            }
        }

        // Check if user already has this account type
        List<Account> existingAccounts = accounts.findByUserId(userId);
        for (Account account : existingAccounts) {
            if (account.getAccountType().equals(accountType)) {
                throw new RuntimeException("User already has a " + accountType + " account");
            }
        }

        AccountApproval newApproval = new AccountApproval();
        newApproval.setUser(user);
        newApproval.setAccountType(accountType);
        newApproval.setStatus("PENDING");
        newApproval.setRequestedAt(LocalDateTime.now());
        accountApprovals.save(newApproval);
    }

    public List<AccountApproval> getAllPendingApprovals() {
        return accountApprovals.findByStatus("PENDING");
    }

    public List<AccountApproval> getApprovalsByUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        return accountApprovals.findByUserId(userId);
    }

    @Transactional
    public void approveAccountRequest(Long approvalId, Long adminId) {
        // Validate admin exists
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        AccountApproval approval = accountApprovals.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found with ID: " + approvalId));

        if (!"PENDING".equals(approval.getStatus())) {
            throw new RuntimeException(
                    "Only PENDING approvals can be approved. Current status: " + approval.getStatus());
        }

        // Validate branch is set
        if (approval.getBranchId() == null) {
            throw new RuntimeException("Branch ID is required for account approval");
        }

        // Double check user doesn't already have this account type
        List<Account> existingAccounts = accounts.findByUserId(approval.getUser().getId());
        for (Account account : existingAccounts) {
            if (account.getAccountType().equals(approval.getAccountType())) {
                throw new RuntimeException("User already has a " + approval.getAccountType() + " account");
            }
        }

        // Create new account
        Account newAccount = new Account();
        String accountNumber = generateAccountNumber.generate();
        newAccount.setAccountNumber(accountNumber);
        newAccount.setAccountType(approval.getAccountType());
        newAccount.setBalance(0);
        newAccount.setStatus("ACTIVE");
        newAccount.setUser(approval.getUser());

        if (accountNumber.length() >= 4) {
            newAccount.setLast4AccountNumber(
                    Long.parseLong(accountNumber.substring(accountNumber.length() - 4)));
        }

        accounts.save(newAccount);

        // Update approval with reviewer info
        approval.setStatus("APPROVED");
        approval.setReviewedAt(LocalDateTime.now());
        // approval.setReviewerId(adminId);
        accountApprovals.save(approval);

        // TODO: Send notification to user about approval
    }

    @Transactional
    public void rejectAccountRequest(Long approvalId, Long adminId, String reason) {
        // Validate admin exists
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        AccountApproval approval = accountApprovals.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found with ID: " + approvalId));

        if (!"PENDING".equals(approval.getStatus())) {
            throw new RuntimeException(
                    "Only PENDING approvals can be rejected. Current status: " + approval.getStatus());
        }

        approval.setStatus("REJECTED");
        approval.setReviewedAt(LocalDateTime.now());
        // approval.setReviewerId(adminId);
        approval.setRejectionReason(reason); // Assuming this field exists in AccountApproval
        accountApprovals.save(approval);

        // TODO: Send notification to user about rejection with reason
    }

    public AccountApproval getApprovalById(Long approvalId) {
        return accountApprovals.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found with ID: " + approvalId));
    }

    @Transactional
    public void cancelApprovalRequest(Long approvalId, Long userId) {
        AccountApproval approval = accountApprovals.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found with ID: " + approvalId));

        if (!approval.getUser().getId().equals(userId)) {
            throw new RuntimeException("Approval does not belong to user with ID: " + userId);
        }

        if (!"PENDING".equals(approval.getStatus())) {
            throw new RuntimeException(
                    "Only PENDING approvals can be cancelled. Current status: " + approval.getStatus());
        }

        approval.setStatus("CANCELLED");
        accountApprovals.save(approval);
    }

    public List<AccountApproval> getApprovalsByStatus(String status) {
        return accountApprovals.findByStatus(status);
    }

}
