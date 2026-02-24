package com.example.bank.services.account;

import com.example.bank.DTO.Account.AccountStatement;
import com.example.bank.model.Transaction;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.TransactionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsible for transaction query operations
 */
@Service
public class TransactionQueryService {

    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;

    public TransactionQueryService(TransactionRepo transactionRepo, AccountRepo accountRepo) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepo.findByFromAccountIdOrToAccountIdOrderByTimestampDesc(accountId, accountId);
    }

    public Transaction getTransactionById(Long transactionId) {
        return transactionRepo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));
    }

    public List<Transaction> getTransactionsByDateRange(Long accountId, LocalDateTime startDate,
            LocalDateTime endDate) {
        return transactionRepo.findByFromAccountIdOrToAccountIdAndTimestampBetween(accountId, accountId, startDate,
                endDate);
    }

    public List<Transaction> getTransactionsByType(Long accountId, String type) {
        return transactionRepo.findByFromAccountIdOrToAccountIdAndType(accountId, accountId, type);
    }

    public Double getTotalDeposits(Long accountId) {
        List<Transaction> deposits = transactionRepo.findByFromAccountIdOrToAccountIdAndType(accountId, accountId,
                "DEPOSIT");
        return deposits.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum();
    }

    public Double getTotalWithdrawals(Long accountId) {
        List<Transaction> withdrawals = transactionRepo.findByFromAccountIdOrToAccountIdAndType(accountId, accountId,
                "WITHDRAWAL");
        return withdrawals.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum();
    }

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

    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }
}
