package com.example.bank.services;

import com.example.bank.DTO.Account.AccountCreation;
import com.example.bank.model.Account;
import com.example.bank.services.account.AccountCreationService;
import com.example.bank.services.account.AccountQueryService;
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

    public AccountService(
            AccountCreationService accountCreationService,
            AccountQueryService accountQueryService) {
        this.accountCreationService = accountCreationService;
        this.accountQueryService = accountQueryService;
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
}
