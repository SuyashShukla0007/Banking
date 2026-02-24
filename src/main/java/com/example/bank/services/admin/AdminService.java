package com.example.bank.services.admin;

import org.springframework.stereotype.Service;
import java.util.*;

import com.example.bank.DTO.Admin.AdminUpdateRequest;
import com.example.bank.DTO.SystemReportDTO;
import com.example.bank.exception.DuplicateEmailException;
import com.example.bank.exception.InvalidPasswordException;
import com.example.bank.model.Account;
import com.example.bank.model.Admin;
import com.example.bank.model.Transaction;
import com.example.bank.model.User;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.AdminRepo;
import com.example.bank.repo.TransactionRepo;
import com.example.bank.repo.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AdminService {

    // Inject AdminRepo
    private final AdminRepo adminRepo;
    // Inject PasswordEncoder

    // transaction repo
    private final TransactionRepo transactionRepo;

    // Inject UserRepository
    private final UserRepository userRepository;

    // Inject AccountRepo
    private final AccountRepo accountRepo;

    public AdminService(AdminRepo adminRepo, TransactionRepo transactionRepo, UserRepository userRepository,
            AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
        this.adminRepo = adminRepo;
        this.userRepository = userRepository;
    }

    // Method: createAdmin(String name, String email, String password)
    // - Validate email doesn't already exist
    // - Create new Admin
    // - Encode password
    // - Set role as "ADMIN"
    // - Save and return admin

    public void createAdmin(String name, String email, String password) {
        if (!adminRepo.findByEmail(email).isEmpty()) {
            throw new DuplicateEmailException("Admin with email '" + email + "' already exists");
        }

        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setName(name);
        admin.setPassword(password);
        adminRepo.save(admin);
    }

    // Method: getAdminById(Long adminId)
    // - Find admin by ID
    // - Return admin details

    public Admin getAdminById(Long adminId) {
        return adminRepo.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + adminId + " not found"));
    }

    // Method: updateAdmin(Long adminId, AdminUpdateRequest request)
    // - Find admin by ID
    // - Update name and email if provided
    // - Save and return updated admin

    public Admin updateAdmin(Long adminId, AdminUpdateRequest request) {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + adminId + " not found"));

        if (request.getEmail() != null && !request.getEmail().isEmpty())
            admin.setEmail(request.getEmail());
        if (request.getName() != null && !request.getName().isEmpty())
            admin.setName(request.getName());

        return adminRepo.save(admin);
    }

    // Method: deleteAdmin(Long adminId)
    // - Find admin by ID
    // - Delete admin
    // - Return confirmation

    public void deleteAdmin(Long adminId) {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + adminId + " not found"));
        adminRepo.delete(admin);
    }

    // Method: changePassword(Long adminId, String oldPassword, String newPassword)
    // - Find admin by ID
    // - Verify old password
    // - Encode and set new password
    // - Save admin

    public void changePassword(Long adminId, String oldPassword, String newPassword) {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + adminId + " not found"));

        if (!oldPassword.equals(admin.getPassword())) {
            throw new InvalidPasswordException("Old password is incorrect");
        }

        admin.setPassword(newPassword);
        adminRepo.save(admin);
    }

    // Method: getAllUsers()
    // - Find all users in system
    // - Return list of users

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Method: getUserById(Long userId)
    // - Find user by ID
    // - Return user details with accounts

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
    }

    // Method: suspendUser(Long userId)
    // - Find user by ID
    // - Update user status to "SUSPENDED"
    // - Deactivate all user accounts
    // - Save changes

    // inconsitent calls to db thrice must visit

    // @Transactional
    // public void suspendUser(Long userId)
    // {
    // if(userRepository.findById(userId).isPresent())
    // {
    // User user =userRepository.findById(userId).get();
    // user.setStatus("SUSPENDED");
    // List<Account> accounts=accountRepo.findByUserId(userId);

    // accounts.forEach(acc->{
    // acc.setStatus("SUSPENDED");
    // accountRepo.save(acc);
    // });
    // userRepository.save(user);
    // }
    // else
    // {
    // throw new RuntimeException("user does not exist");
    // }
    // }

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

    // Method: activateUser(Long userId)
    // - Find user by ID
    // - Update user status to "ACTIVE"
    // - Save changes
    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        user.setStatus("ACTIVE");

        // Reactivate accounts that were suspended
        List<Account> accounts = accountRepo.findByUserId(userId);
        accounts.forEach(acc -> {
            if ("SUSPENDED".equals(acc.getStatus())) {
                acc.setStatus("ACTIVE");
            }
        });

        accountRepo.saveAll(accounts);
        userRepository.save(user);
    }

    // Method: getAccountsByUserId(Long userId)
    // - Find user by ID
    // - Get all accounts associated with user
    // - Return list of accounts

    public List<Account> getAccountsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
        return user.getAccounts();
    }

    // leave it for the time being

    // Method: closeAccount(Long accountId)
    // - Find account by ID
    // - Update account status to "CLOSED"
    // - Save account

    // Method: viewAllTransactions()
    // - Retrieve all transactions from system
    // - Return list of all transactions

    public List<Transaction> viewAllTransactions() {
        return transactionRepo.findAll();
    }

    // Method: generateSystemReport()
    // - Count total users in system
    // - Count total accounts in system
    // - Count total transactions
    // - Calculate total balance across all accounts
    // - Return statistics report
    // create a custom class for this report and return that object instead of map

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
