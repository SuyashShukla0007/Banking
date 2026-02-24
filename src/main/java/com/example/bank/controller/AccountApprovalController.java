package com.example.bank.controller;

import com.example.bank.model.AccountApproval;
import com.example.bank.services.account.AccountApprovalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account-approvals")
public class AccountApprovalController {

    private final AccountApprovalService accountApprovalService;

    public AccountApprovalController(AccountApprovalService accountApprovalService) {
        this.accountApprovalService = accountApprovalService;
    }

    @GetMapping("/pending")
    public List<AccountApproval> getPendingApprovals() {
        return accountApprovalService.getAllPendingApprovals();
    }

    @GetMapping("/{approvalId}")
    public AccountApproval getApprovalById(@PathVariable Long approvalId) {
        return accountApprovalService.getApprovalById(approvalId);
    }

    @PostMapping("/{approvalId}/approve")
    public String approveAccountRequest(@PathVariable Long approvalId, @RequestParam Long adminId) {
        accountApprovalService.approveAccountRequest(approvalId, adminId);
        return "Account approval request approved successfully";
    }

    @PostMapping("/{approvalId}/reject")
    public String rejectAccountRequest(@PathVariable Long approvalId, @RequestParam Long adminId,
            @RequestParam String reason) {
        accountApprovalService.rejectAccountRequest(approvalId, adminId, reason);
        return "Account approval request rejected";
    }
}
