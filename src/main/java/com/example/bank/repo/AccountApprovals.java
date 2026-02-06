package com.example.bank.repo;

import com.example.bank.model.AccountApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountApprovals extends JpaRepository<AccountApproval, Long> {

}
