package com.example.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import  com.example.bank.model.BankBranch;
public interface BranchService extends JpaRepository<BankBranch, Long> {
}
