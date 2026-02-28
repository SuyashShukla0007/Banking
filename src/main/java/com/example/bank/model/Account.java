package com.example.bank.model;

import com.example.bank.enums.AccountStatus;
import com.example.bank.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    // @Column(nullable = false)
    // private double interestRate;

    @Column(nullable = false)
    private double balance;

    @Column(nullable = false)
    private long last4AccountNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "accounts", "accountApprovals", "password" })
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnoreProperties("accounts")
    private BankBranch branch;
}
