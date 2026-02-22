package com.example.bank.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts/status")
public class AccountStatusController {

    // Inject AccountService

    // Constructor injection

    // POST /accounts/status/{accountId}/close
    // Path Variable: accountId
    // Request Body: { "adminId": Long }
    // - Validate admin exists (optional for v1)
    // - Call accountService.closeAccount(accountId, adminId)
    // - Closes the account (requires zero balance)
    // - Status changes from ACTIVE/SUSPENDED to CLOSED
    // - Closed accounts cannot be reopened
    // - Return success message
    // - Handle exceptions:
    // - Account not found
    // - Account already closed
    // - Account has non-zero balance

    // POST /accounts/status/{accountId}/suspend
    // Path Variable: accountId
    // Request Body: { "adminId": Long }
    // - Validate admin exists (optional for v1)
    // - Call accountService.suspendAccount(accountId, adminId)
    // - Suspends the account (no transactions allowed)
    // - Status changes from ACTIVE to SUSPENDED
    // - Return success message
    // - Handle exceptions:
    // - Account not found
    // - Account is closed (cannot suspend closed account)
    // - Account already suspended

    // POST /accounts/status/{accountId}/reactivate
    // Path Variable: accountId
    // Request Body: { "adminId": Long }
    // - Validate admin exists (optional for v1)
    // - Call accountService.reactivateAccount(accountId, adminId)
    // - Reactivates a suspended account
    // - Status changes from SUSPENDED to ACTIVE
    // - Return success message
    // - Handle exceptions:
    // - Account not found
    // - Account is closed (cannot reactivate closed account)
    // - Account already active

    // PUT /accounts/status/{accountId}
    // Path Variable: accountId
    // Request Body: { "status": String, "adminId": Long }
    // - Validate status is one of: ACTIVE, SUSPENDED, CLOSED
    // - Call accountService.updateAccountStatus(accountId, status, adminId)
    // - Generic status update endpoint
    // - Enforces business rules:
    // - Cannot close account with non-zero balance
    // - Cannot change status of already closed account
    // - Return updated account details
    // - Handle exceptions:
    // - Account not found
    // - Invalid status value
    // - Business rule violations

    // GET /accounts/status/{accountId}
    // Path Variable: accountId
    // - Call accountService or accountQueryService to get account details
    // - Return current account status
    // - Return as JSON: { "accountId": Long, "status": String, "lastUpdated":
    // LocalDateTime }
    // - Handle exception if account not found

}
