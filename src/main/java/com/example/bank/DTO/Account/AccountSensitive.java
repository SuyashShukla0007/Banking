package com.example.bank.DTO.Account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountSensitive {
    private String accountNumber;
    private double balance;
    private String accountType;
    private String branchName;
}
