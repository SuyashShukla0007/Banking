package com.example.bank.DTO.Account;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountNonSensitive {
//    private String accountNumber;
    private long balance;
    private String accountType;
    private String branchName;
}

