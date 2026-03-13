package com.example.bank.model;

import com.example.bank.enums.TransactionType;
import com.example.bank.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Source account (for WITHDRAWAL and TRANSFER; null for DEPOSIT)
    @ManyToOne(optional = true)
    @JoinColumn(name = "from_account_id", nullable = true)
    @JsonIgnoreProperties({ "user", "branch" })
    private Account fromAccount;

    // Destination account (for DEPOSIT and TRANSFER; null for WITHDRAWAL)
    @ManyToOne(optional = true)
    @JoinColumn(name = "to_account_id", nullable = true)
    @JsonIgnoreProperties({ "user", "branch" })
    private Account toAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String description;

    @Column
    private String referenceNumber;

    @Column
    private String statement;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    // Constructor for creating transactions
    public Transaction(Account fromAccount, Account toAccount, Double amount, TransactionType type) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = BigDecimal.valueOf(amount);
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.SUCCESS;
    }

}
