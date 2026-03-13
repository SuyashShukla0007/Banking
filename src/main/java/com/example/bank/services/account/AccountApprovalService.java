package com.example.bank.services.account;

import com.example.bank.enums.AccountStatus;
import com.example.bank.enums.AccountType;
import com.example.bank.enums.ApprovalStatus;
import com.example.bank.exception.InvalidOperationException;
import com.example.bank.exception.ResourceAlreadyExistsException;
import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.exception.UnauthorizedAccessException;
import com.example.bank.DTO.Account.AccountCreation;
import com.example.bank.services.account.AccountCreationService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.model.Account;
import com.example.bank.model.AccountApproval;
import com.example.bank.model.BankBranch;
import com.example.bank.model.User;
import com.example.bank.repo.AccountApprovals;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.AdminRepo;
import com.example.bank.repo.BranchRepo;
import com.example.bank.repo.UserRepository;

@Service

public class AccountApprovalService {

    private final AccountApprovals accountApprovals;
    private final UserRepository userRepository;
    private final AdminRepo adminRepo;
    private final AccountRepo accounts;
    private final BranchRepo branchRepo;
    private final AccountCreationService accountCreationService;

    private static final List<AccountType> VALID_ACCOUNT_TYPES = Arrays.asList(AccountType.values());

    public AccountApprovalService(AccountApprovals accountApprovals, UserRepository userRepository,
            AdminRepo adminRepo, AccountRepo accounts, BranchRepo branchRepo,
            AccountCreationService accountCreationService) {
        this.accountApprovals = accountApprovals;
        this.userRepository = userRepository;
        this.adminRepo = adminRepo;
        this.accounts = accounts;
        this.branchRepo = branchRepo;
        this.accountCreationService = accountCreationService;
    }

    @Transactional
    public com.example.bank.model.AccountApproval requestAccountApproval(Long userId, AccountType accountType,
            Long branchId) {
        AccountType type = accountType;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Check for existing pending approvals (use repo-level exists if available)
        if (accountApprovals.existsByUserIdAndAccountTypeAndStatus(userId, type, ApprovalStatus.PENDING)) {
            throw new ResourceAlreadyExistsException("User already has a pending approval for " + type + " account");
        }

        // Check if user already has this account type
        List<Account> existingAccounts = accounts.findByUserId(userId);
        for (Account account : existingAccounts) {
            if (account.getAccountType() == type) {
                throw new ResourceAlreadyExistsException("User already has a " + accountType + " account");
            }
        }

        AccountApproval newApproval = new AccountApproval();
        newApproval.setUser(user);
        newApproval.setAccountType(type);
        newApproval.setStatus(ApprovalStatus.PENDING);
        newApproval.setRequestedAt(LocalDateTime.now());
        newApproval.setBranchId(branchId);
        return accountApprovals.save(newApproval);
    }

    public List<AccountApproval> getAllPendingApprovals() {
        return accountApprovals.findByStatus(ApprovalStatus.PENDING);
    }

    public List<AccountApproval> getApprovalsByUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return accountApprovals.findByUserId(userId);
    }

    @Transactional
    public void approveAccountRequest(Long approvalId, Long adminId) {
        // Validate admin exists (admins are in AdminRepo, not UserRepository)
        adminRepo.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));

        AccountApproval approval = accountApprovals.findById(approvalId)
                .orElseThrow(() -> new ResourceNotFoundException("Approval request not found with ID: " + approvalId));

        if (ApprovalStatus.PENDING != approval.getStatus()) {
            throw new InvalidOperationException(
                    "Only PENDING approvals can be approved. Current status: " + approval.getStatus());
        }

        // Validate branch is set
        if (approval.getBranchId() == null) {
            throw new IllegalArgumentException("Branch ID is required for account approval");
        }

        // Fetch branch entity
        BankBranch branch = branchRepo.findById(approval.getBranchId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Branch not found with ID: " + approval.getBranchId()));

        // Double check user doesn't already have this account type
        List<Account> existingAccounts = accounts.findByUserId(approval.getUser().getId());
        for (Account account : existingAccounts) {
            if (account.getAccountType() == approval.getAccountType()) {
                throw new ResourceAlreadyExistsException(
                        "User already has a " + approval.getAccountType() + " account");
            }
        }

        // Delegate actual account creation to AccountCreationService to centralize
        // logic
        AccountCreation accountCreation = new AccountCreation();
        accountCreation.setAccountType(approval.getAccountType());
        accountCreation.setBranchId(approval.getBranchId());

        accountCreationService.createAccount(accountCreation, approval.getUser().getId());

        // Update approval with reviewer info
        approval.setStatus(ApprovalStatus.APPROVED);
        approval.setReviewedAt(LocalDateTime.now());
        accountApprovals.save(approval);
    }

    @Transactional
    public void rejectAccountRequest(Long approvalId, Long adminId, String reason) {
        // Validate admin exists (admins are in AdminRepo, not UserRepository)
        adminRepo.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));

        AccountApproval approval = accountApprovals.findById(approvalId)
                .orElseThrow(() -> new ResourceNotFoundException("Approval request not found with ID: " + approvalId));

        if (ApprovalStatus.PENDING != approval.getStatus()) {
            throw new InvalidOperationException(
                    "Only PENDING approvals can be rejected. Current status: " + approval.getStatus());
        }

        approval.setStatus(ApprovalStatus.REJECTED);
        approval.setReviewedAt(LocalDateTime.now());
        // approval.setReviewerId(adminId);
        approval.setRejectionReason(reason); // Assuming this field exists in AccountApproval
        accountApprovals.save(approval);

        // TODO: Send notification to user about rejection with reason
    }

    public AccountApproval getApprovalById(Long approvalId) {
        return accountApprovals.findById(approvalId)
                .orElseThrow(() -> new ResourceNotFoundException("Approval request not found with ID: " + approvalId));
    }

    @Transactional
    public void cancelApprovalRequest(Long approvalId, Long userId) {
        AccountApproval approval = accountApprovals.findById(approvalId)
                .orElseThrow(() -> new ResourceNotFoundException("Approval request not found with ID: " + approvalId));

        if (!approval.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Approval does not belong to user with ID: " + userId);
        }

        if (ApprovalStatus.PENDING != approval.getStatus()) {
            throw new InvalidOperationException(
                    "Only PENDING approvals can be cancelled. Current status: " + approval.getStatus());
        }

        approval.setStatus(ApprovalStatus.CANCELLED);
        accountApprovals.save(approval);
    }

    public List<AccountApproval> getApprovalsByStatus(String status) {
        return accountApprovals.findByStatus(ApprovalStatus.valueOf(status));
    }

}
