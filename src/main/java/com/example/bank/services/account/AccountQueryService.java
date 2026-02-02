package com.example.bank.services.account;

import com.example.bank.model.Account;
import com.example.bank.repo.AccountRepo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for account query operations
 */
@Service
public class AccountQueryService {

    private final AccountRepo accountRepo;

    public AccountQueryService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    /**
     * Get all account IDs for a specific user
     * 
     * @param userId User ID
     * @return List of account IDs
     */
    public List<Long> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepo.findByUserId(userId);
        return accounts.stream()
                .map(Account::getId)
                .toList();
    }
}
