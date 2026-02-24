package com.example.bank.services.account;

import java.util.List;
import java.time.LocalDateTime;
import com.example.bank.DTO.Account.AccountStatement;
import com.example.bank.model.Transaction;
import org.springframework.stereotype.Service;

/**
 * Facade service that delegates to specialized transaction sub-services.
 * Controllers should only interact with this class.
 */
@Service
public class TransactionService {

    private final TransactionOperationService transactionOperationService;
    private final TransactionQueryService transactionQueryService;

    public TransactionService(TransactionOperationService transactionOperationService,
            TransactionQueryService transactionQueryService) {
        this.transactionOperationService = transactionOperationService;
        this.transactionQueryService = transactionQueryService;
    }

    // ========== Operations ==========

    public void deposit(Long accountId, Double amount) {
        transactionOperationService.deposit(accountId, amount);
    }

    public void withdraw(Long accountId, Double amount) {
        transactionOperationService.withdraw(accountId, amount);
    }

    public void transfer(Long fromAccountId, Long toAccountId, Double amount) {
        transactionOperationService.transfer(fromAccountId, toAccountId, amount);
    }

    public void transferBetweenOwnAccounts(Long userId, Long fromAccountId, Long toAccountId, Double amount) {
        transactionOperationService.transferBetweenOwnAccounts(userId, fromAccountId, toAccountId, amount);
    }

    // ========== Queries ==========

    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionQueryService.getTransactionHistory(accountId);
    }

    public Transaction getTransactionById(Long transactionId) {
        return transactionQueryService.getTransactionById(transactionId);
    }

    public List<Transaction> getTransactionsByDateRange(Long accountId, LocalDateTime startDate,
            LocalDateTime endDate) {
        return transactionQueryService.getTransactionsByDateRange(accountId, startDate, endDate);
    }

    public List<Transaction> getTransactionsByType(Long accountId, String type) {
        return transactionQueryService.getTransactionsByType(accountId, type);
    }

    public Double getTotalDeposits(Long accountId) {
        return transactionQueryService.getTotalDeposits(accountId);
    }

    public Double getTotalWithdrawals(Long accountId) {
        return transactionQueryService.getTotalWithdrawals(accountId);
    }

    public AccountStatement getAccountStatement(Long accountId, LocalDateTime start, LocalDateTime end) {
        return transactionQueryService.getAccountStatement(accountId, start, end);
    }

    public List<Transaction> getAllTransactions() {
        return transactionQueryService.getAllTransactions();
    }
}
