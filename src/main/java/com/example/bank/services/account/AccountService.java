package com.example.bank.services.account;

import com.example.bank.DTO.Account.AccountCreation;
import com.example.bank.exception.InvalidOperationException;
import com.example.bank.model.Account;
import com.example.bank.model.AccountApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Facade service that delegates to specialized account sub-services.
 * Controllers should only interact with this class.
 */
@Service
public class AccountService {

    private final AccountCreationService accountCreationService;
    private final AccountQueryService accountQueryService;
    private final AccountApprovalService accountApprovalService;
    private final AccountStatusService accountStatusService;

    public AccountService(
            AccountCreationService accountCreationService,
            AccountQueryService accountQueryService,
            AccountApprovalService accountApprovalService,
            AccountStatusService accountStatusService) {
        this.accountCreationService = accountCreationService;
        this.accountQueryService = accountQueryService;
        this.accountApprovalService = accountApprovalService;
        this.accountStatusService = accountStatusService;
    }

    // ========== Creation ==========

    public Account createAccount(AccountCreation accountCreation, Long userID) {
        throw new InvalidOperationException("Direct account creation is disabled. Use the approval flow instead.");
    }

    // ========== Query ==========

    public List<Long> getAccounts(Long userID) {
        return accountQueryService.getAccountsByUserId(userID);
    }

    public List<Account> getAllAccounts() {
        return accountQueryService.getAllAccounts();
    }

    public Page<Account> getAllAccounts(Pageable pageable) {
        return accountQueryService.getAllAccounts(pageable);
    }

    public Account getAccountById(Long accountId) {
        return accountQueryService.getAccountById(accountId);
    }

    public Double getAccountBalance(Long accountId) {
        return accountQueryService.getAccountBalance(accountId);
    }

    // ========== Status Management ==========

    public void closeAccount(Long accountId, Long adminId) {
        accountStatusService.closeAccount(accountId, adminId);
    }

    public void suspendAccount(Long accountId, Long adminId) {
        accountStatusService.suspendAccount(accountId, adminId);
    }

    public void reactivateAccount(Long accountId, Long adminId) {
        accountStatusService.reactivateAccount(accountId, adminId);
    }

    public void updateAccountStatus(Long accountId, String status, Long adminId) {
        accountStatusService.updateAccountStatus(accountId, status, adminId);
    }

    // ========== Approvals ==========

    public com.example.bank.model.AccountApproval requestAccountApproval(
            com.example.bank.DTO.Account.AccountCreation accountCreation, Long userId) {
        return accountApprovalService.requestAccountApproval(userId, accountCreation.getAccountType(),
                accountCreation.getBranchId());
    }

    public List<AccountApproval> getAllPendingApprovals() {
        return accountApprovalService.getAllPendingApprovals();
    }

    public AccountApproval getApprovalById(Long approvalId) {
        return accountApprovalService.getApprovalById(approvalId);
    }

    public void approveAccountRequest(Long approvalId, Long adminId) {
        accountApprovalService.approveAccountRequest(approvalId, adminId);
    }

    public void rejectAccountRequest(Long approvalId, Long adminId, String reason) {
        accountApprovalService.rejectAccountRequest(approvalId, adminId, reason);
    }

    public List<AccountApproval> getAccountsByStatus(String status) {
        return accountApprovalService.getApprovalsByStatus(status);
    }
}
