package com.example.bank.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BankBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @Column(name = "branch_code", nullable = false, unique = true)
    private String branchCode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @OneToMany(mappedBy = "branch")
    private List<Account> accounts;
}
