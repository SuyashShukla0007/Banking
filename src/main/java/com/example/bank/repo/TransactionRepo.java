package com.example.bank.repo;

import com.example.bank.enums.TransactionType;
import com.example.bank.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    // Find all transactions where account is either source or destination
    List<Transaction> findByFromAccountIdOrToAccountIdOrderByTimestampDesc(Long fromAccountId, Long toAccountId);

    // Paginated version of the above
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount.id = :accountId OR t.toAccount.id = :accountId ORDER BY t.timestamp DESC")
    Page<Transaction> findByAccountIdPaged(@Param("accountId") Long accountId, Pageable pageable);

    // Find transactions within date range where account is involved
    // Using @Query because Spring Data derived query OR+AND precedence is
    // ambiguous:
    // "fromAccountId OR (toAccountId AND timestamp BETWEEN)" instead of the
    // intended
    // "(fromAccountId OR toAccountId) AND timestamp BETWEEN"
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount.id = :accountId OR t.toAccount.id = :accountId) " +
            "AND t.timestamp BETWEEN :startDate AND :endDate ORDER BY t.timestamp DESC")
    List<Transaction> findByAccountIdAndTimestampBetween(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Find transactions by type where account is involved
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount.id = :accountId OR t.toAccount.id = :accountId) " +
            "AND t.type = :type ORDER BY t.timestamp DESC")
    List<Transaction> findByAccountIdAndType(
            @Param("accountId") Long accountId,
            @Param("type") TransactionType type);
}
