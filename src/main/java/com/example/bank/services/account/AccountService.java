package com.example.bank.services.account;

import com.example.bank.DTO.Account.AccountCreation;
import com.example.bank.model.Account;
import com.example.bank.model.AccountApproval;

import org.springframework.stereotype.Service;

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

    public AccountService(
            AccountCreationService accountCreationService,
            AccountQueryService accountQueryService,
            AccountApprovalService accountApprovalService) {
        this.accountCreationService = accountCreationService;
        this.accountQueryService = accountQueryService;
        this.accountApprovalService = accountApprovalService;
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

    // Method: updateAccountStatus(Long accountId, String status)
    // - Find account by ID
    // - Validate account exists
    // - Update account status (ACTIVE/INACTIVE/SUSPENDED/CLOSED)
    // - Save and return account
    public void updateAccountStatus(Long accountId, Long adminId) {
        // Implementation goes here
        accountApprovalService.approveAccountRequest(accountId, adminId);
    }

    // Method: getAccountsByStatus(String status)
    // - Find all accounts with given status
    // - Return list of accounts

    public List<AccountApproval> getAccountsByStatus(String status) {
        return accountApprovalService.getApprovalsByStatus(status);
    }

    // Method: transferBetweenOwnAccounts(Long userId, Long fromAccountId, Long
    // toAccountId, BigDecimal amount)
    // - Validate both accounts belong to same user
    // - Validate sufficient balance in source account
    // - Call TransactionService.transfer
    // - Return transfer confirmation

}
