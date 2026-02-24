package com.example.bank.services.admin;

import com.example.bank.model.Account;
import com.example.bank.model.User;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserManagementService {

    private final UserRepository userRepository;
    private final AccountRepo accountRepo;

    public AdminUserManagementService(UserRepository userRepository, AccountRepo accountRepo) {
        this.userRepository = userRepository;
        this.accountRepo = accountRepo;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
    }

    @Transactional
    public void suspendUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        user.setStatus("SUSPENDED");

        List<Account> accounts = accountRepo.findByUserId(userId);
        accounts.forEach(acc -> acc.setStatus("SUSPENDED"));

        accountRepo.saveAll(accounts);
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        user.setStatus("ACTIVE");

        List<Account> accounts = accountRepo.findByUserId(userId);
        accounts.forEach(acc -> {
            if ("SUSPENDED".equals(acc.getStatus())) {
                acc.setStatus("ACTIVE");
            }
        });

        accountRepo.saveAll(accounts);
        userRepository.save(user);
    }

    public List<Account> getAccountsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
        return user.getAccounts();
    }
}
