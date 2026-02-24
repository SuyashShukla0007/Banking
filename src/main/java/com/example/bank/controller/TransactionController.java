package com.example.bank.controller;

import com.example.bank.DTO.Transaction.DepositRequest;
import com.example.bank.DTO.Transaction.TransferRequest;
import com.example.bank.DTO.Transaction.WithdrawRequest;
import com.example.bank.model.Transaction;
import com.example.bank.services.account.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public String deposit(@RequestBody DepositRequest request) {
        transactionService.deposit(request.getAccountId(), request.getAmount());
        return "Deposit successful";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody WithdrawRequest request) {
        transactionService.withdraw(request.getAccountId(), request.getAmount());
        return "Withdrawal successful";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request) {
        transactionService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
        return "Transfer successful";
    }

    @GetMapping("/{accountId}/history")
    public List<Transaction> getTransactionHistory(@PathVariable Long accountId) {
        return transactionService.getTransactionHistory(accountId);
    }

    @GetMapping("/{transactionId}")
    public Transaction getTransactionById(@PathVariable Long transactionId) {
        return transactionService.getTransactionById(transactionId);
    }

    @GetMapping("/all")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
}
