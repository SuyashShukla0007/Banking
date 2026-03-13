package com.example.bank.repo;

import com.example.bank.enums.ApprovalStatus;
import com.example.bank.model.AccountApproval;
import com.example.bank.enums.AccountType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountApprovals extends JpaRepository<AccountApproval, Long> {
    List<AccountApproval> findByUserId(Long userId);

    List<AccountApproval> findByStatus(ApprovalStatus status);

    boolean existsByUserIdAndAccountTypeAndStatus(Long userId, AccountType accountType, ApprovalStatus status);

    List<AccountApproval> findByUserIdAndStatus(Long userId, ApprovalStatus status);
}
