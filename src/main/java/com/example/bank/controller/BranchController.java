package com.example.bank.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/branches")
public class BranchController {

    // Inject BranchService

    // Constructor injection

    // POST /branches/create
    // Request Body: { "branchCode": String, "branchName": String, "address":
    // String, "city": String, "state": String }
    // - Validate all required fields are present
    // - Validate branchCode format (optional)
    // - Call branchService.createBranch(branchName, branchCode, address, city,
    // state)
    // - Return created branch details
    // - Handle RuntimeException if branch code already exists

    // GET /branches/{branchId}
    // Path Variable: branchId
    // - Call branchService.getBranchById(branchId)
    // - Return branch details
    // - Handle RuntimeException if branch not found

    // GET /branches/code/{branchCode}
    // Path Variable: branchCode
    // - Call branchService.getBranchByCode(branchCode)
    // - Return branch details
    // - Handle RuntimeException if branch not found

    // GET /branches
    // - Call branchService.getAllBranches()
    // - Return list of all branches
    // - Optional: Add pagination (page, size)
    // - Optional: Add sorting by city, state, or branch name

    // PUT /branches/{branchId}
    // Path Variable: branchId
    // Request Body (BranchUpdateRequest): { "branchId": Long, "branchName": String,
    // "city": String, "state": String }
    // - Validate at least one field is provided for update
    // - Call branchService.updateBranch(updateRequest)
    // - Return updated branch details
    // - Handle RuntimeException if branch not found
    // - Note: Branch code cannot be updated (business rule)

    // DELETE /branches/{branchId}
    // Path Variable: branchId
    // - Call branchService.deleteBranch(branchId)
    // - Return success message
    // - Handle RuntimeException if:
    // - Branch not found
    // - Branch has active accounts (cannot delete)

    // GET /branches/city/{city}
    // Path Variable: city
    // - Call branchService.getBranchesByCity(city)
    // - Return list of branches in the specified city
    // - Case-insensitive search

    // GET /branches/state/{state}
    // Path Variable: state
    // - Call branchService.getBranchesByState(state)
    // - Return list of branches in the specified state
    // - Case-insensitive search

    // GET /branches/{branchId}/accounts
    // Path Variable: branchId
    // - Call branchService.getAccountsByBranch(branchId)
    // - Return list of all accounts associated with the branch
    // - Handle RuntimeException if branch not found

    // GET /branches/{branchId}/accounts/count
    // Path Variable: branchId
    // - Call branchService.getAccountCountByBranch(branchId)
    // - Return count of accounts in the branch
    // - Return as JSON: { "branchId": Long, "accountCount": Integer }
    // - Handle RuntimeException if branch not found

}
