package com.example.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import  com.example.bank.model.BankBranch;
public interface BranchRepo extends JpaRepository<BankBranch, Long>
{
}
