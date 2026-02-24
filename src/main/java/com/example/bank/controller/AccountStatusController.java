package com.example.bank.controller;

import com.example.bank.services.account.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account-status")
public class AccountStatusController {

    private final AccountService accountService;

    public AccountStatusController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping("/{accountId}/activate")
    public String activateAccount(@PathVariable Long accountId, @RequestParam Long adminId) {
        accountService.reactivateAccount(accountId, adminId);
        return "Account activated successfully";
    }

    @PutMapping("/{accountId}/suspend")
    public String suspendAccount(@PathVariable Long accountId, @RequestParam Long adminId) {
        accountService.suspendAccount(accountId, adminId);
        return "Account suspended successfully";
    }

    @PutMapping("/{accountId}/close")
    public String closeAccount(@PathVariable Long accountId, @RequestParam Long adminId) {
        accountService.closeAccount(accountId, adminId);
        return "Account closed successfully";
    }
}
