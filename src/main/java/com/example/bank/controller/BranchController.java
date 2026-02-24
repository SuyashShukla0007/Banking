package com.example.bank.controller;

import com.example.bank.DTO.Branch.BranchCreateRequest;
import com.example.bank.DTO.Branch.BranchUpdateRequest;
import com.example.bank.model.BankBranch;
import com.example.bank.utility.BranchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("/create")
    public String createBranch(@RequestBody BranchCreateRequest request) {
        branchService.createBranch(
                request.getBranchName(),
                request.getBranchCode(),
                request.getAddress(),
                request.getCity(),
                request.getState());
        return "Branch created successfully";
    }

    @GetMapping("/all")
    public List<BankBranch> getAllBranches() {
        return branchService.getAllBranches();
    }

    @GetMapping("/{branchId}")
    public BankBranch getBranchById(@PathVariable Long branchId) {
        return branchService.getBranchById(branchId);
    }

    @PutMapping("/{branchId}")
    public BankBranch updateBranch(@PathVariable Long branchId, @RequestBody BranchUpdateRequest request) {
        request.setBranchId(branchId);
        return branchService.updateBranch(request);
    }

    @DeleteMapping("/{branchId}")
    public String deleteBranch(@PathVariable Long branchId) {
        branchService.deleteBranch(branchId);
        return "Branch deleted successfully";
    }
}
