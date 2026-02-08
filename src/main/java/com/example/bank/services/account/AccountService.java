package com.example.bank.services.account;

import com.example.bank.DTO.Account.AccountCreation;
import com.example.bank.model.Account;
import com.example.bank.model.AccountApproval;
import com.example.bank.repo.AccountRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Facade service that delegates to specific account services
 * This maintains backward compatibility while following SRP
 */
@Service
public class AccountService {

    private final AccountCreationService accountCreationService;
    private final AccountQueryService accountQueryService;
    private final AccountApprovalService accountApprovalService;
    private final AccountRepo accountRepo;

    public AccountService(
            AccountCreationService accountCreationService,
            AccountQueryService accountQueryService,
            AccountApprovalService accountApprovalService,
            AccountRepo accountRepo) {
        this.accountCreationService = accountCreationService;
        this.accountQueryService = accountQueryService;
        this.accountApprovalService = accountApprovalService;
        this.accountRepo = accountRepo;
    }

    /**
     * Create a new account
     * 
     * @param accountCreation Account creation details
     * @param userID          User ID
     * @return Created Account entity
     */
    public Account createAccount(AccountCreation accountCreation, Long userID) {
        return accountCreationService.createAccount(accountCreation, userID);
    }

    /**
     * Get all accounts for a user
     * 
     * @param userID User ID
     * @return List of account IDs
     */
    public List<Long> getAccounts(Long userID) {
        return accountQueryService.getAccountsByUserId(userID);
    }

    /**
     * Approve an account request
     * 
     * @param approvalId Account approval request ID
     * @param adminId    Admin performing the approval
     */
    public void approveAccount(Long approvalId, Long adminId) {
        accountApprovalService.approveAccountRequest(approvalId, adminId);
    }

    /**
     * Get account approvals by status
     * 
     * @param status Approval status
     * @return List of account approvals
     */
    public List<AccountApproval> getAccountsByStatus(String status) {
        return accountApprovalService.getApprovalsByStatus(status);
    }

    /**
     * Close an account
     * 
     * @param accountId Account ID to close
     * @param adminId   Admin performing the action
     */
    @Transactional
    public void closeAccount(Long accountId, Long adminId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        if ("CLOSED".equals(account.getStatus())) {
            throw new RuntimeException("Account is already closed");
        }

        if (account.getBalance() != 0) {
            throw new RuntimeException("Cannot close account with non-zero balance: " + account.getBalance());
        }

        account.setStatus("CLOSED");
        accountRepo.save(account);
    }

    /**
     * Suspend an account
     * 
     * @param accountId Account ID to suspend
     * @param adminId   Admin performing the action
     */
    @Transactional
    public void suspendAccount(Long accountId, Long adminId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        if ("CLOSED".equals(account.getStatus())) {
            throw new RuntimeException("Cannot suspend a closed account");
        }

        if ("SUSPENDED".equals(account.getStatus())) {
            throw new RuntimeException("Account is already suspended");
        }

        account.setStatus("SUSPENDED");
        accountRepo.save(account);
    }

    /**
     * Reactivate a suspended account
     * 
     * @param accountId Account ID to reactivate
     * @param adminId   Admin performing the action
     */
    @Transactional
    public void reactivateAccount(Long accountId, Long adminId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        if ("CLOSED".equals(account.getStatus())) {
            throw new RuntimeException("Cannot reactivate a closed account");
        }

        if ("ACTIVE".equals(account.getStatus())) {
            throw new RuntimeException("Account is already active");
        }

        account.setStatus("ACTIVE");
        accountRepo.save(account);
    }

    /**
     * Update account status
     * 
     * @param accountId Account ID
     * @param status    New status (ACTIVE/SUSPENDED/CLOSED)
     * @param adminId   Admin performing the action
     */
    @Transactional
    public void updateAccountStatus(Long accountId, String status, Long adminId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        if (!status.matches("ACTIVE|SUSPENDED|CLOSED")) {
            throw new RuntimeException("Invalid status. Must be ACTIVE, SUSPENDED, or CLOSED");
        }

        if ("CLOSED".equals(status) && account.getBalance() != 0) {
            throw new RuntimeException("Cannot close account with non-zero balance");
        }

        account.setStatus(status);
        accountRepo.save(account);
    }

}
