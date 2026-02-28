package com.example.bank.controller;

import com.example.bank.DTO.Transaction.DepositRequest;
import com.example.bank.DTO.Transaction.TransferRequest;
import com.example.bank.DTO.Transaction.WithdrawRequest;
import com.example.bank.DTO.response.ApiResponse;
import com.example.bank.model.Transaction;
import com.example.bank.services.account.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ApiResponse<Void> deposit(@Valid @RequestBody DepositRequest request) {
        transactionService.deposit(request.getAccountId(), request.getAmount());
        return ApiResponse.success("Deposit successful");
    }

    @PostMapping("/withdraw")
    public ApiResponse<Void> withdraw(@Valid @RequestBody WithdrawRequest request) {
        transactionService.withdraw(request.getAccountId(), request.getAmount());
        return ApiResponse.success("Withdrawal successful");
    }

    @PostMapping("/transfer")
    public ApiResponse<Void> transfer(@Valid @RequestBody TransferRequest request) {
        transactionService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
        return ApiResponse.success("Transfer successful");
    }

    @GetMapping("/{accountId}/history")
    public ApiResponse<Page<Transaction>> getTransactionHistory(
            @PathVariable Long accountId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Transaction> transactions = transactionService.getTransactionHistory(accountId, pageable);
        return ApiResponse.success("Transaction history fetched", transactions);
    }

    @GetMapping("/{transactionId}")
    public ApiResponse<Transaction> getTransactionById(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return ApiResponse.success("Transaction fetched", transaction);
    }

    @GetMapping("/all")
    public ApiResponse<Page<Transaction>> getAllTransactions(@PageableDefault(size = 20) Pageable pageable) {
        Page<Transaction> transactions = transactionService.getAllTransactions(pageable);
        return ApiResponse.success("All transactions fetched", transactions);
    }
}
