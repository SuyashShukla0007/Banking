package com.example.bank.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bank.model.BankBranch;

public interface BranchRepo extends JpaRepository<BankBranch, Long> {
    boolean existsByBranchCode(String branchCode);

    Optional<BankBranch> findByBranchCode(String branchCode);
}
