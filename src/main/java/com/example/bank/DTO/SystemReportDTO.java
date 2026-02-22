package com.example.bank.DTO;

public class SystemReportDTO {
    private long totalUsers;
    private long totalAccounts;
    private long totalTransactions;
    private double totalBalance;

    public SystemReportDTO(long totalUsers, long totalAccounts, long totalTransactions, double totalBalance) {
        this.totalUsers = totalUsers;
        this.totalAccounts = totalAccounts;
        this.totalTransactions = totalTransactions;
        this.totalBalance = totalBalance;
    }

    // Getters
    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalAccounts() {
        return totalAccounts;
    }

    public long getTotalTransactions() {
        return totalTransactions;
    }

    public double getTotalBalance() {
        return totalBalance;
    }
}