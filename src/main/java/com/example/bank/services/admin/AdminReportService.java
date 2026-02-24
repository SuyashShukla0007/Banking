package com.example.bank.services.admin;

import com.example.bank.DTO.SystemReportDTO;
import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.TransactionRepo;
import com.example.bank.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminReportService {

    private final TransactionRepo transactionRepo;
    private final UserRepository userRepository;
    private final AccountRepo accountRepo;

    public AdminReportService(TransactionRepo transactionRepo, UserRepository userRepository,
            AccountRepo accountRepo) {
        this.transactionRepo = transactionRepo;
        this.userRepository = userRepository;
        this.accountRepo = accountRepo;
    }

    public List<Transaction> viewAllTransactions() {
        return transactionRepo.findAll();
    }

    public SystemReportDTO generateSystemReport() {
        long totalUsers = userRepository.count();
        long totalAccounts = accountRepo.count();
        long totalTransactions = transactionRepo.count();

        List<Account> accounts = accountRepo.findAll();
        double totalBalance = accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();

        return new SystemReportDTO(totalUsers, totalAccounts, totalTransactions, totalBalance);
    }
}
