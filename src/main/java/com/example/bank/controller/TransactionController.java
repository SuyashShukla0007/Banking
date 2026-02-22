package com.example.bank.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    // Inject TransactionService

    // Constructor injection

    // POST /transactions/deposit
    // Request Body: { "accountId": Long, "amount": Double }
    // - Validate amount is positive
    // - Call transactionService.deposit(accountId, amount)
    // - Return success message or transaction details
    // - Handle exceptions (account not found, account not active)

    // POST /transactions/withdraw
    // Request Body: { "accountId": Long, "amount": Double }
    // - Validate amount is positive
    // - Call transactionService.withdraw(accountId, amount)
    // - Return success message or transaction details
    // - Handle exceptions (account not found, insufficient balance, account not
    // active)

    // POST /transactions/transfer
    // Request Body: { "fromAccountId": Long, "toAccountId": Long, "amount": Double
    // }
    // - Validate amount is positive
    // - Validate fromAccountId != toAccountId
    // - Call transactionService.transfer(fromAccountId, toAccountId, amount)
    // - Return success message or transaction details
    // - Handle exceptions (accounts not found, insufficient balance, accounts not
    // active)

    // POST /transactions/transfer-own-accounts
    // Request Body: { "userId": Long, "fromAccountId": Long, "toAccountId": Long,
    // "amount": Double }
    // - Validate amount is positive
    // - Validate both accounts belong to the user
    // - Call transactionService.transferBetweenOwnAccounts(userId, fromAccountId,
    // toAccountId, amount)
    // - Return success message or transaction details
    // - Handle exceptions (accounts don't belong to user, insufficient balance)

    // GET /transactions/account/{accountId}
    // Path Variable: accountId
    // - Call transactionService.getTransactionHistory(accountId)
    // - Return list of all transactions for the account
    // - Ordered by timestamp descending (newest first)

    // GET /transactions/{transactionId}
    // Path Variable: transactionId
    // - Call transactionService.getTransactionById(transactionId)
    // - Return transaction details
    // - Handle exception if transaction not found

    // GET /transactions/account/{accountId}/date-range
    // Path Variable: accountId
    // Query Params: startDate (LocalDateTime), endDate (LocalDateTime)
    // - Parse date parameters
    // - Call transactionService.getTransactionsByDateRange(accountId, startDate,
    // endDate)
    // - Return filtered list of transactions
    // - Handle invalid date formats

    // GET /transactions/account/{accountId}/type/{type}
    // Path Variables: accountId, type (DEPOSIT/WITHDRAWAL/TRANSFER)
    // - Validate type is one of: DEPOSIT, WITHDRAWAL, TRANSFER
    // - Call transactionService.getTransactionsByType(accountId, type)
    // - Return filtered list of transactions by type

    // GET /transactions/account/{accountId}/total-deposits
    // Path Variable: accountId
    // - Call transactionService.getTotalDeposits(accountId)
    // - Return sum of all deposit amounts for the account
    // - Return as JSON: { "accountId": Long, "totalDeposits": Double }

    // GET /transactions/account/{accountId}/total-withdrawals
    // Path Variable: accountId
    // - Call transactionService.getTotalWithdrawals(accountId)
    // - Return sum of all withdrawal amounts for the account
    // - Return as JSON: { "accountId": Long, "totalWithdrawals": Double }

    // GET /transactions/account/{accountId}/statement
    // Path Variable: accountId
    // Query Params: start (LocalDateTime), end (LocalDateTime)
    // - Parse date parameters
    // - Call transactionService.getAccountStatement(accountId, start, end)
    // - Return AccountStatement DTO with:
    // - Account details
    // - List of transactions in date range
    // - Opening balance
    // - Closing balance
    // - Handle invalid dates or account not found

}
