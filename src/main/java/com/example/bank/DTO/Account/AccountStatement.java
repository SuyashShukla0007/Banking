package com.example.bank.DTO.Account;

import com.example.bank.model.Account;
import com.example.bank.model.Transaction;

import lombok.Getter;
import java.util.List;
import lombok.Setter;

@Getter
@Setter
public class AccountStatement {

    // new AccountStatement(account, transactions, openingBalance, closingBalance);

    private Account account;
    private List<Transaction> transactions;
    private double openingBalance;
    private double closingBalance;

    public AccountStatement(Account account, List<Transaction> transactions, double openingBalance,
            double closingBalance) {
        this.account = account;
        this.transactions = transactions;
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
    }

}