package com.example.bank.controller.account;

import com.example.bank.DTO.Branch.BranchCreateRequest;
import com.example.bank.DTO.Branch.BranchUpdateRequest;
import com.example.bank.DTO.response.ApiResponse;
import com.example.bank.model.BankBranch;
import com.example.bank.utility.BranchService;
import jakarta.validation.Valid;
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
    public ApiResponse<Void> createBranch(@Valid @RequestBody BranchCreateRequest request) {
        branchService.createBranch(
                request.getBranchName(),
                request.getBranchCode(),
                request.getAddress(),
                request.getCity(),
                request.getState());
        return ApiResponse.success("Branch created successfully");
    }

    @GetMapping("/all")
    public ApiResponse<List<BankBranch>> getAllBranches() {
        List<BankBranch> branches = branchService.getAllBranches();
        return ApiResponse.success("All branches fetched", branches);
    }

    @GetMapping("/{branchId}")
    public ApiResponse<BankBranch> getBranchById(@PathVariable Long branchId) {
        BankBranch branch = branchService.getBranchById(branchId);
        return ApiResponse.success("Branch fetched", branch);
    }

    @PutMapping("/{branchId}")
    public ApiResponse<BankBranch> updateBranch(@PathVariable Long branchId, @RequestBody BranchUpdateRequest request) {
        request.setBranchId(branchId);
        BankBranch branch = branchService.updateBranch(request);
        return ApiResponse.success("Branch updated successfully", branch);
    }

    @DeleteMapping("/{branchId}")
    public ApiResponse<Void> deleteBranch(@PathVariable Long branchId) {
        branchService.deleteBranch(branchId);
        return ApiResponse.success("Branch deleted successfully");
    }
}
