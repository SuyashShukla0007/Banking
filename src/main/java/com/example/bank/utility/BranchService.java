package com.example.bank.utility;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.bank.DTO.Branch.BranchUpdateRequest;
import com.example.bank.model.Account;
import com.example.bank.model.BankBranch;
import com.example.bank.repo.BranchRepo;

@Service
public class BranchService {

    // Inject BranchRepo
    private final BranchRepo branchRepo;

    public BranchService(BranchRepo branchRepo) {
        this.branchRepo = branchRepo;
    }

    // Method: createBranch(String branchName, String branchCode, String address,
    // String city, String state)
    // - Validate branch code is unique
    // - Create new BankBranch
    // - Set all branch details
    // - Save and return branch

    public void createBranch(String branchName, String branchCode, String address, String city, String state)
            throws RuntimeException {
        if (branchRepo.existsByBranchCode(branchCode)) {
            throw new RuntimeException("Branch code already exists");
        } else {
            BankBranch branch = new BankBranch();
            branch.setBranchCode(branchCode);
            branch.setBranchName(branchName);
            branch.setCity(city);
            branch.setState(state);
            branch.setAddress(address);
            branchRepo.save(branch);
        }
        return;
    }

    // Method: getBranchById(Long branchId)
    // - Find branch by ID
    // - Return branch details

    public BankBranch getBranchById(Long branchId) throws RuntimeException {
        return branchRepo.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found with ID: " + branchId));
    }

    // Method: getBranchByCode(String branchCode)
    // - Find branch by branch code
    // - Return branch details

    public BankBranch getBranchByCode(String branchCode) throws RuntimeException {
        return branchRepo.findByBranchCode(branchCode)
                .orElseThrow(() -> new RuntimeException("Branch not found with code: " + branchCode));
    }

    // Method: getAllBranches()
    // - Find all branches
    // - Return list of branches

    public List<BankBranch> getAllBranches() {
        return branchRepo.findAll();
    }

    // Method: updateBranch(Long branchId, String branchName, String address, String
    // city, String state)
    // - Find branch by ID
    // - Update branch details if provided
    // - Save and return updated branch

    public BankBranch updateBranch(BranchUpdateRequest updateRequest) throws RuntimeException {
        BankBranch branch = branchRepo.findById(updateRequest.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with ID: " + updateRequest.getBranchId()));
        if (updateRequest.getBranchName() != null && !updateRequest.getBranchName().isEmpty()) {
            branch.setBranchName(updateRequest.getBranchName());
        }
        if (updateRequest.getCity() != null && !updateRequest.getCity().isEmpty()) {
            branch.setCity(updateRequest.getCity());
        }
        if (updateRequest.getState() != null && !updateRequest.getState().isEmpty()) {
            branch.setState(updateRequest.getState());
        }
        return branchRepo.save(branch);
    }

    // Method: deleteBranch(Long branchId)
    // - Find branch by ID
    // - Check if branch has any accounts
    // - If no accounts, delete branch
    // - Return confirmation

    public void deleteBranch(Long branchId) throws RuntimeException {
        BankBranch branch = branchRepo.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found with ID: " + branchId));
        if (branch.getAccounts() != null && !branch.getAccounts().isEmpty()) {
            throw new RuntimeException("Cannot delete branch with active accounts");
        } else {
            branchRepo.delete(branch);
            return;
        }
    }

    // Method: getBranchesByCity(String city)
    // - Find all branches in given city
    // - Return list of branches
    public List<BankBranch> getBranchesByCity(String city) {
        return branchRepo.findAll().stream()
                .filter(branch -> branch.getCity().equalsIgnoreCase(city))
                .toList();
    }
    // Method: getAccountsByBranch(Long branchId)
    // - Find all accounts associated with branch
    // - Return list of accounts

    public List<Account> getAccountsByBranch(Long branchId) throws RuntimeException {
        BankBranch branch = branchRepo.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found with ID: " + branchId));
        return branch.getAccounts();
    }

    // Method: getBranchesByState(String state)
    // - Find all branches in given state
    // - Return list of branches

    public List<BankBranch> getBranchesByState(String state) {
        return branchRepo.findAll().stream()
                .filter(branch -> branch.getState().equalsIgnoreCase(state))
                .toList();
    }

    // Method: getAccountCountByBranch(Long branchId)
    // - Find branch by ID
    // - Count all accounts associated with branch
    // - Return account count

    public int getAccountCountByBranch(Long branchId) throws RuntimeException {
        BankBranch bankBranch = branchRepo.findById(branchId)
                .orElseThrow(() -> new RuntimeException("no branch found for this id"));
        return bankBranch.getAccounts().isEmpty() ? bankBranch.getAccounts().size() : 0;
    }

}
