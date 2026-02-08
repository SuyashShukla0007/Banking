package com.example.bank.services.account;

import java.util.List;
import java.time.LocalDateTime;
import com.example.bank.DTO.Account.AccountStatement;
import org.springframework.stereotype.Service;

import com.example.bank.model.Transaction;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.TransactionRepo;

import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    // Inject TransactionRepo
    private final TransactionRepo transactionRepo;

    // Inject AccountRepo
    private final AccountRepo accountRepo;

    public TransactionService(TransactionRepo transactionRepo, AccountRepo accountRepo) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
    }

    // Method: deposit(Long accountId, BigDecimal amount)
    // - Find account by ID
    // - Validate account exists
    // - Add amount to account balance
    // - Save updated account
    // - Create transaction record with type "DEPOSIT"
    // - Save and return transaction

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

        // Create transaction record
        Transaction transaction = new Transaction(null, account, amount, "DEPOSIT");
        transaction.setDescription("Deposit to account " + accountId);
        transaction.setReferenceNumber("DEP-" + System.currentTimeMillis());
        transactionRepo.save(transaction);
    }

    // Method: withdraw(Long accountId, Double amount)
    // - Find account by ID
    // - Validate account exists
    // - Validate sufficient balance
    // - Subtract amount from account balance
    // - Save updated account
    // - Create transaction record with type "WITHDRAWAL"
    // - Save and return transaction
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

        // Create transaction record
        Transaction transaction = new Transaction(account, null, amount, "WITHDRAWAL");
        transaction.setDescription("Withdrawal from account " + accountId);
        transaction.setReferenceNumber("WD-" + System.currentTimeMillis());
        transactionRepo.save(transaction);
    }

    // Method: transfer(Long fromAccountId, Long toAccountId, BigDecimal amount)
    // - Validate both accounts exist
    // - Validate sufficient balance in source account
    // - Call withdraw on source account
    // - Call deposit on destination account
    // - Create transaction records for both accounts with type "TRANSFER"
    // - Return both transactions or confirmation

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

        // Create transaction record
        Transaction transaction = new Transaction(fromAccount, toAccount, amount, "TRANSFER");
        transaction.setDescription("Transfer from account " + fromAccountId + " to account " + toAccountId);
        transaction.setReferenceNumber("TRF-" + System.currentTimeMillis());
        transactionRepo.save(transaction);
    }

    // Method: getTransactionHistory(Long accountId)
    // - Find all transactions for given account ID
    // - Return list of transactions ordered by timestamp descending
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(Long accountId) {
        // goes here
        return transactionRepo.findByFromAccountIdOrToAccountIdOrderByTimestampDesc(accountId, accountId);
    }

    // Method: getTransactionById(Long transactionId)
    // - Find transaction by ID
    // - Return transaction details
    public Transaction getTransactionById(Long transactionId) {
        return transactionRepo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));
    }

    // Method: getTransactionsByDateRange(Long accountId, LocalDateTime startDate,
    // LocalDateTime endDate)
    // - Find transactions for account within date range
    // - Return filtered list of transactions

    public List<Transaction> getTransactionsByDateRange(Long accountId, LocalDateTime startDate,
            LocalDateTime endDate) {
        return transactionRepo.findByFromAccountIdOrToAccountIdAndTimestampBetween(accountId, accountId, startDate,
                endDate);
    }

    // Method: getTransactionsByType(Long accountId, String type)
    // - Find all transactions for account filtered by type
    // (DEPOSIT/WITHDRAWAL/TRANSFER)
    // - Return filtered list of transactions
    public List<Transaction> getTransactionsByType(Long accountId, String type) {
        return transactionRepo.findByFromAccountIdOrToAccountIdAndType(accountId, accountId, type);
    }

    // Method: getTotalDeposits(Long accountId)
    // - Find all deposits for account
    // - Calculate sum of all deposit amounts
    // - Return total deposit amount
    public Double getTotalDeposits(Long accountId) {
        List<Transaction> deposits = transactionRepo.findByFromAccountIdOrToAccountIdAndType(accountId, accountId,
                "DEPOSIT");
        return deposits.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum();
    }

    // Method: getTotalWithdrawals(Long accountId)
    // - Find all withdrawals for account
    // - Calculate sum of all withdrawal amounts
    // - Return total withdrawal amount
    public Double getTotalWithdrawals(Long accountId) {
        List<Transaction> withdrawals = transactionRepo.findByFromAccountIdOrToAccountIdAndType(accountId, accountId,
                "WITHDRAWAL");
        return withdrawals.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum();
    }

    // Method: getAccountStatement(Long accountId, LocalDateTime start,
    // LocalDateTime end)
    // - Get account details
    // - Get all transactions within date range
    // - Calculate opening and closing balance
    // - Return detailed statement with transactions
    public AccountStatement getAccountStatement(Long accountId, LocalDateTime start, LocalDateTime end) {
        var account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        List<Transaction> transactions = transactionRepo
                .findByFromAccountIdOrToAccountIdAndTimestampBetween(accountId, accountId, start, end);
        Double openingBalance = account.getBalance()
                - transactions.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum();
        Double closingBalance = account.getBalance();
        return new AccountStatement(account, transactions, openingBalance, closingBalance);
    }

    // Method: transferBetweenOwnAccounts(Long userId, Long fromAccountId, Long
    // toAccountId, BigDecimal amount)
    // - Validate both accounts belong to same user
    // - Validate sufficient balance in source account
    // - Call TransactionService.transfer
    // - Return transfer confirmation

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

        // Create transaction record
        Transaction transaction = new Transaction(fromAccount, toAccount, amount, "TRANSFER");
        transaction.setDescription("Transfer between own accounts");
        transaction.setReferenceNumber("OWN-" + System.currentTimeMillis());
        transactionRepo.save(transaction);
    }

}
