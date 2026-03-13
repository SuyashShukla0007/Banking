package com.example.bank.DTO.Account;

import com.example.bank.model.Account;
import com.example.bank.model.Transaction;

import java.util.List;

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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public double getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(double closingBalance) {
        this.closingBalance = closingBalance;
    }

}