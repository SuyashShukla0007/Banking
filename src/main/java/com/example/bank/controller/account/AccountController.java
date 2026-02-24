package com.example.bank.controller.account;

import com.example.bank.DTO.Account.AccountCreation;
import com.example.bank.model.Account;
import com.example.bank.model.AccountApproval;
import com.example.bank.services.account.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // ========== Account CRUD ==========

    @PostMapping("/accounts/create/{userId}")
    public Account createAccount(@RequestBody AccountCreation accountCreation,
            @PathVariable Long userId) {
        return accountService.createAccount(accountCreation, userId);
    }

    @GetMapping("/accounts/user/{userId}")
    public List<Long> getAccounts(@PathVariable Long userId) {
        return accountService.getAccounts(userId);
    }

    @GetMapping("/accounts/all")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/accounts/{accountId}")
    public Account getAccountById(@PathVariable Long accountId) {
        return accountService.getAccountById(accountId);
    }

    @GetMapping("/accounts/{accountId}/balance")
    public Double getAccountBalance(@PathVariable Long accountId) {
        return accountService.getAccountBalance(accountId);
    }

    // ========== Account Status ==========

    @PutMapping("/account-status/{accountId}/activate")
    public String activateAccount(@PathVariable Long accountId, @RequestParam Long adminId) {
        accountService.reactivateAccount(accountId, adminId);
        return "Account activated successfully";
    }

    @PutMapping("/account-status/{accountId}/suspend")
    public String suspendAccount(@PathVariable Long accountId, @RequestParam Long adminId) {
        accountService.suspendAccount(accountId, adminId);
        return "Account suspended successfully";
    }

    @PutMapping("/account-status/{accountId}/close")
    public String closeAccount(@PathVariable Long accountId, @RequestParam Long adminId) {
        accountService.closeAccount(accountId, adminId);
        return "Account closed successfully";
    }

    // ========== Account Approvals ==========

    @GetMapping("/account-approvals/pending")
    public List<AccountApproval> getPendingApprovals() {
        return accountService.getAllPendingApprovals();
    }

    @GetMapping("/account-approvals/{approvalId}")
    public AccountApproval getApprovalById(@PathVariable Long approvalId) {
        return accountService.getApprovalById(approvalId);
    }

    @PostMapping("/account-approvals/{approvalId}/approve")
    public String approveAccountRequest(@PathVariable Long approvalId, @RequestParam Long adminId) {
        accountService.approveAccountRequest(approvalId, adminId);
        return "Account approval request approved successfully";
    }

    @PostMapping("/account-approvals/{approvalId}/reject")
    public String rejectAccountRequest(@PathVariable Long approvalId, @RequestParam Long adminId,
            @RequestParam String reason) {
        accountService.rejectAccountRequest(approvalId, adminId, reason);
        return "Account approval request rejected";
    }
}
