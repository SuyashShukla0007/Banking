package com.example.bank.controller.account;

import com.example.bank.DTO.Account.AccountCreation;
import com.example.bank.DTO.response.ApiResponse;
import com.example.bank.model.Account;
import com.example.bank.model.AccountApproval;
import com.example.bank.services.account.AccountService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ApiResponse<AccountApproval> createAccount(@Valid @RequestBody AccountCreation accountCreation,
            @PathVariable Long userId) {
        AccountApproval approval = accountService.requestAccountApproval(accountCreation, userId);
        return ApiResponse.success("Account creation requested for admin approval", approval);
    }

    @GetMapping("/accounts/user/{userId}")
    public ApiResponse<List<Long>> getAccounts(@PathVariable Long userId) {
        List<Long> accountIds = accountService.getAccounts(userId);
        return ApiResponse.success("User accounts fetched", accountIds);
    }

    @GetMapping("/accounts/all")
    public ApiResponse<Page<Account>> getAllAccounts(@PageableDefault(size = 20) Pageable pageable) {
        Page<Account> accounts = accountService.getAllAccounts(pageable);
        return ApiResponse.success("All accounts fetched", accounts);
    }

    @GetMapping("/accounts/{accountId}")
    public ApiResponse<Account> getAccountById(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return ApiResponse.success("Account fetched", account);
    }

    @GetMapping("/accounts/{accountId}/balance")
    public ApiResponse<Double> getAccountBalance(@PathVariable Long accountId) {
        Double balance = accountService.getAccountBalance(accountId);
        return ApiResponse.success("Balance fetched", balance);
    }

    // ========== Account Status ==========

    @PutMapping("/account-status/{accountId}/activate")
    public ApiResponse<Void> activateAccount(@PathVariable Long accountId, @RequestParam Long adminId) {
        accountService.reactivateAccount(accountId, adminId);
        return ApiResponse.success("Account activated successfully");
    }

    @PutMapping("/account-status/{accountId}/suspend")
    public ApiResponse<Void> suspendAccount(@PathVariable Long accountId, @RequestParam Long adminId) {
        accountService.suspendAccount(accountId, adminId);
        return ApiResponse.success("Account suspended successfully");
    }

    @PutMapping("/account-status/{accountId}/close")
    public ApiResponse<Void> closeAccount(@PathVariable Long accountId, @RequestParam Long adminId) {
        accountService.closeAccount(accountId, adminId);
        return ApiResponse.success("Account closed successfully");
    }

    // ========== Account Approvals ==========

    @GetMapping("/account-approvals/pending")
    public ApiResponse<List<AccountApproval>> getPendingApprovals() {
        List<AccountApproval> approvals = accountService.getAllPendingApprovals();
        return ApiResponse.success("Pending approvals fetched", approvals);
    }

    @GetMapping("/account-approvals/{approvalId}")
    public ApiResponse<AccountApproval> getApprovalById(@PathVariable Long approvalId) {
        AccountApproval approval = accountService.getApprovalById(approvalId);
        return ApiResponse.success("Approval fetched", approval);
    }

    @PostMapping("/account-approvals/{approvalId}/approve")
    public ApiResponse<Void> approveAccountRequest(@PathVariable Long approvalId, @RequestParam Long adminId) {
        accountService.approveAccountRequest(approvalId, adminId);
        return ApiResponse.success("Account approval request approved successfully");
    }

    @PostMapping("/account-approvals/{approvalId}/reject")
    public ApiResponse<Void> rejectAccountRequest(@PathVariable Long approvalId, @RequestParam Long adminId,
            @RequestParam String reason) {
        accountService.rejectAccountRequest(approvalId, adminId, reason);
        return ApiResponse.success("Account approval request rejected");
    }
}
