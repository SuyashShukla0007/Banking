package com.example.bank.repo;

import com.example.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    // Find all transactions where account is either source or destination
    List<Transaction> findByFromAccountIdOrToAccountIdOrderByTimestampDesc(Long fromAccountId, Long toAccountId);

    // Find transactions within date range where account is involved
    List<Transaction> findByFromAccountIdOrToAccountIdAndTimestampBetween(Long fromAccountId, Long toAccountId,
            LocalDateTime startDate, LocalDateTime endDate);

    // Find transactions by type where account is involved
    List<Transaction> findByFromAccountIdOrToAccountIdAndType(Long fromAccountId, Long toAccountId, String type);

}
