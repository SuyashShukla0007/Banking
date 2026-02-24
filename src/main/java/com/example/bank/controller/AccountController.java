package com.example.bank.controller;

import com.example.bank.DTO.Account.AccountCreation;
import com.example.bank.model.Account;
import com.example.bank.services.account.AccountCreationService;
import com.example.bank.services.account.AccountQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountCreationService accountCreationService;
    private final AccountQueryService accountQueryService;

    public AccountController(AccountCreationService accountCreationService,
            AccountQueryService accountQueryService) {
        this.accountCreationService = accountCreationService;
        this.accountQueryService = accountQueryService;
    }

    @PostMapping("/create/{userId}")
    public Account createAccount(@RequestBody AccountCreation accountCreation,
            @PathVariable Long userId) {
        return accountCreationService.createAccount(accountCreation, userId);
    }

    @GetMapping("/user/{userId}")
    public List<Long> getAccounts(@PathVariable Long userId) {
        return accountQueryService.getAccountsByUserId(userId);
    }

    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        return accountQueryService.getAllAccounts();
    }

    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable Long accountId) {
        return accountQueryService.getAccountById(accountId);
    }

    @GetMapping("/{accountId}/balance")
    public Double getAccountBalance(@PathVariable Long accountId) {
        return accountQueryService.getAccountBalance(accountId);
    }
}
