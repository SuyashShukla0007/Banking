package com.example.bank.services.account;

import com.example.bank.model.Account;
import com.example.bank.repo.AccountRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for account status operations (activate, suspend, close)
 */
@Service
public class AccountStatusService {

    private final AccountRepo accountRepo;

    public AccountStatusService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

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
