package com.example.bank.services.account;

import com.example.bank.enums.AccountStatus;
import com.example.bank.exception.InvalidOperationException;
import com.example.bank.exception.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));

        if (AccountStatus.CLOSED == account.getStatus()) {
            throw new InvalidOperationException("Account is already closed");
        }

        if (account.getBalance().compareTo(java.math.BigDecimal.ZERO) != 0) {
            throw new InvalidOperationException("Cannot close account with non-zero balance: " + account.getBalance());
        }

        account.setStatus(AccountStatus.CLOSED);
        accountRepo.save(account);
    }

    @Transactional
    public void suspendAccount(Long accountId, Long adminId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));

        if (AccountStatus.CLOSED == account.getStatus()) {
            throw new InvalidOperationException("Cannot suspend a closed account");
        }

        if (AccountStatus.SUSPENDED == account.getStatus()) {
            throw new InvalidOperationException("Account is already suspended");
        }

        account.setStatus(AccountStatus.SUSPENDED);
        accountRepo.save(account);
    }

    @Transactional
    public void reactivateAccount(Long accountId, Long adminId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));

        if (AccountStatus.CLOSED == account.getStatus()) {
            throw new InvalidOperationException("Cannot reactivate a closed account");
        }

        if (AccountStatus.ACTIVE == account.getStatus()) {
            throw new InvalidOperationException("Account is already active");
        }

        account.setStatus(AccountStatus.ACTIVE);
        accountRepo.save(account);
    }

    @Transactional
    public void updateAccountStatus(Long accountId, String status, Long adminId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));

        AccountStatus newStatus;
        try {
            newStatus = AccountStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Must be ACTIVE, SUSPENDED, or CLOSED");
        }

        if (AccountStatus.CLOSED == newStatus && account.getBalance().compareTo(java.math.BigDecimal.ZERO) != 0) {
            throw new InvalidOperationException("Cannot close account with non-zero balance");
        }

        account.setStatus(newStatus);
        accountRepo.save(account);
    }
}
