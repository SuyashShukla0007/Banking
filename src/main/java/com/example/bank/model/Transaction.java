package com.example.bank.model;

import com.example.bank.enums.TransactionType;
import com.example.bank.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
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

    public Transaction() {
    }

    // Constructor for creating transactions
    public Transaction(Account fromAccount, Account toAccount, Double amount, TransactionType type) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = BigDecimal.valueOf(amount);
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.SUCCESS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

}
