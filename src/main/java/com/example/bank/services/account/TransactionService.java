package com.example.bank.services.account;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.bank.model.Transaction;
import com.example.bank.model.AccountStatement;
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
    public boolean deposit(Long accountId, Double amount) {
        // Implementation goes here
        var accountOptional = accountRepo.findByIdForUpdate(accountId);
        if (accountOptional.isEmpty()) {
            return false;
        } else {
            var account = accountOptional.get();
            account.setBalance(account.getBalance() + amount);
            accountRepo.save(account);
            // Create transaction record logic here
            return true;
        }
    }

    // Method: withdraw(Long accountId, BigDecimal amount)
    // - Find account by ID
    // - Validate account exists
    // - Check if balance is sufficient
    // - Subtract amount from account balance
    // - Save updated account
    // - Create transaction record with type "WITHDRAWAL"
    // - Save and return transaction
    @Transactional
    public boolean withdraw(Long accountId,Double amount)
    {
        var accountOptional=accountRepo.findByIdForUpdate(accountId);
        if(accountOptional.isEmpty())
        return false;
        else
        {
            var account=accountOptional.get();
            if(account.getBalance()<amount)
            return false;
            else
            {
                account.setBalance(account.getBalance()-amount);
                accountRepo.save(account);
                // Create transaction record logic here
                return true;
            }
        }
    }

    // Method: transfer(Long fromAccountId, Long toAccountId, BigDecimal amount)
    // - Validate both accounts exist
    // - Validate sufficient balance in source account
    // - Call withdraw on source account
    // - Call deposit on destination account
    // - Create transaction records for both accounts with type "TRANSFER"
    // - Return both transactions or confirmation

    @Transactional
    public boolean transfer(Long fromAccountId, Long toAccountId, Double amount) {
        // Implementation goes here
        var fromAccountOptional = accountRepo.findByIdForUpdate(fromAccountId);
        var toAccountOptional = accountRepo.findByIdForUpdate(toAccountId);
        if (fromAccountOptional.isEmpty() || toAccountOptional.isEmpty()) {
            return false;
        } else {
            var fromAccount = fromAccountOptional.get();
            var toAccount = toAccountOptional.get();
            if (fromAccount.getBalance() < amount) {
                return false;
            } else {
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);
                accountRepo.save(fromAccount);
                accountRepo.save(toAccount);
                // Create transaction records logic here
                return true;
            }
        }
    }

    // Method: getTransactionHistory(Long accountId)
    // - Find all transactions for given account ID
    // - Return list of transactions ordered by timestamp descending
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(Long accountId) {
        //  goes here
        return transactionRepo.findByAccountIdOrderByTimestampDesc(accountId);
    }

    // Method: getTransactionById(Long transactionId)
    // - Find transaction by ID
    // - Return transaction details
    public Transaction getTransactionById(Long transactionId)
    {
        return transactionRepo.findById(transactionId).orElse(null);
    }

    // Method: getTransactionsByDateRange(Long accountId, LocalDateTime startDate,
    // LocalDateTime endDate)
    // - Find transactions for account within date range
    // - Return filtered list of transactions

    public List<Transaction> getTransactionsByDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepo.findByAccountIdAndTimestampBetween(accountId, startDate, endDate);
    }

    // Method: getTransactionsByType(Long accountId, String type)
    // - Find all transactions for account filtered by type
    // (DEPOSIT/WITHDRAWAL/TRANSFER)
    // - Return filtered list of transactions
    public List<Transaction> getTransactionsByType(Long accountId, String type) {
        return transactionRepo.findByAccountIdAndType(accountId, type);
    }

    // Method: getTotalDeposits(Long accountId)
    // - Find all deposits for account
    // - Calculate sum of all deposit amounts
    // - Return total deposit amount
    public Double getTotalDeposits(Long accountId) {
        List<Transaction> deposits = transactionRepo.findByAccountIdAndType(accountId, "DEPOSIT");
        return deposits.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum();
    }

    // Method: getTotalWithdrawals(Long accountId)
    // - Find all withdrawals for account
    // - Calculate sum of all withdrawal amounts
    // - Return total withdrawal amount
    public Double getTotalWithdrawals(Long accountId){
        List<Transaction> withdrawals=transactionRepo.findByAccountIdAndType(accountId, "WITHDRAWALS");
        return withdrawals.stream().mapToDouble(t->t.getAmount().doubleValue()).sum();
    }

    // Method: getAccountStatement(Long accountId, LocalDateTime start,
    // LocalDateTime end)
    // - Get account details
    // - Get all transactions within date range
    // - Calculate opening and closing balance
    // - Return detailed statement with transactions
    public AccountStatement getAccountStatement(Long accountId, LocalDateTime start, LocalDateTime end) {
        // Implementation goes here
        var accountOptional = accountRepo.findById(accountId);
        if (accountOptional.isEmpty()) {
            return null;
        } else {
            var account = accountOptional.get();
            var transactions = transactionRepo.findByAccountIdAndTimestampBetween(accountId, start, end);
            Double openingBalance = account.getBalance() - transactions.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum();
            Double closingBalance = account.getBalance();
            return new AccountStatement(account, transactions, openingBalance, closingBalance);
        }
    }

}
