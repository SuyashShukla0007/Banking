package com.example.bank.services.account;

import com.example.bank.model.Transaction;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.TransactionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for transaction operations (deposit, withdraw, transfer)
 */
@Service
public class TransactionOperationService {

    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;

    public TransactionOperationService(TransactionRepo transactionRepo, AccountRepo accountRepo) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
    }

    @Transactional
    public void deposit(Long accountId, Double amount) {
        if (amount <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }

        var account = accountRepo.findByIdForUpdate(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new RuntimeException("Cannot deposit to account with status: " + account.getStatus());
        }

        account.setBalance(account.getBalance() + amount);
        accountRepo.save(account);

        Transaction transaction = new Transaction(null, account, amount, "DEPOSIT");
        transaction.setDescription("Deposit to account " + accountId);
        transaction.setReferenceNumber("DEP-" + System.currentTimeMillis());
        transactionRepo.save(transaction);
    }

    @Transactional
    public void withdraw(Long accountId, Double amount) {
        if (amount <= 0) {
            throw new RuntimeException("Withdrawal amount must be positive");
        }

        var account = accountRepo.findByIdForUpdate(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new RuntimeException("Cannot withdraw from account with status: " + account.getStatus());
        }

        if (account.getBalance() < amount) {
            throw new RuntimeException(
                    "Insufficient balance. Available: " + account.getBalance() + ", Requested: " + amount);
        }

        account.setBalance(account.getBalance() - amount);
        accountRepo.save(account);

        Transaction transaction = new Transaction(account, null, amount, "WITHDRAWAL");
        transaction.setDescription("Withdrawal from account " + accountId);
        transaction.setReferenceNumber("WD-" + System.currentTimeMillis());
        transactionRepo.save(transaction);
    }

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, Double amount) {
        if (amount <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }

        if (fromAccountId.equals(toAccountId)) {
            throw new RuntimeException("Cannot transfer to the same account");
        }

        var fromAccount = accountRepo.findByIdForUpdate(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found with ID: " + fromAccountId));

        var toAccount = accountRepo.findByIdForUpdate(toAccountId)
                .orElseThrow(() -> new RuntimeException("Destination account not found with ID: " + toAccountId));

        if (!"ACTIVE".equals(fromAccount.getStatus())) {
            throw new RuntimeException("Cannot transfer from account with status: " + fromAccount.getStatus());
        }

        if (!"ACTIVE".equals(toAccount.getStatus())) {
            throw new RuntimeException("Cannot transfer to account with status: " + toAccount.getStatus());
        }

        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException(
                    "Insufficient balance. Available: " + fromAccount.getBalance() + ", Requested: " + amount);
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepo.save(fromAccount);
        accountRepo.save(toAccount);

        Transaction transaction = new Transaction(fromAccount, toAccount, amount, "TRANSFER");
        transaction.setDescription("Transfer from account " + fromAccountId + " to account " + toAccountId);
        transaction.setReferenceNumber("TRF-" + System.currentTimeMillis());
        transactionRepo.save(transaction);
    }

    @Transactional
    public void transferBetweenOwnAccounts(Long userId, Long fromAccountId, Long toAccountId, Double amount) {
        if (amount <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }

        if (fromAccountId.equals(toAccountId)) {
            throw new RuntimeException("Cannot transfer to the same account");
        }

        var fromAccount = accountRepo.findByIdForUpdate(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found with ID: " + fromAccountId));

        var toAccount = accountRepo.findByIdForUpdate(toAccountId)
                .orElseThrow(() -> new RuntimeException("Destination account not found with ID: " + toAccountId));

        if (!fromAccount.getUser().getId().equals(userId)) {
            throw new RuntimeException("Source account does not belong to user");
        }

        if (!toAccount.getUser().getId().equals(userId)) {
            throw new RuntimeException("Destination account does not belong to user");
        }

        if (!"ACTIVE".equals(fromAccount.getStatus())) {
            throw new RuntimeException("Cannot transfer from account with status: " + fromAccount.getStatus());
        }

        if (!"ACTIVE".equals(toAccount.getStatus())) {
            throw new RuntimeException("Cannot transfer to account with status: " + toAccount.getStatus());
        }

        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException(
                    "Insufficient balance. Available: " + fromAccount.getBalance() + ", Requested: " + amount);
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepo.save(fromAccount);
        accountRepo.save(toAccount);

        Transaction transaction = new Transaction(fromAccount, toAccount, amount, "TRANSFER");
        transaction.setDescription("Transfer between own accounts");
        transaction.setReferenceNumber("OWN-" + System.currentTimeMillis());
        transactionRepo.save(transaction);
    }
}
