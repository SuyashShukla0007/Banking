package com.example.bank.services.account;

import com.example.bank.DTO.Account.AccountCreation;
import com.example.bank.enums.AccountStatus;
import com.example.bank.enums.AccountType;
import com.example.bank.model.Account;
import com.example.bank.model.BankBranch;
import com.example.bank.model.User;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.BranchRepo;
import com.example.bank.repo.UserRepository;
import com.example.bank.utility.GenerateAccountNumber;
import org.springframework.stereotype.Service;

/**
 * Service responsible for account creation operations
 */
@Service
public class AccountCreationService {

    private final AccountRepo accountRepo;
    private final UserRepository userRepository;
    private final BranchRepo branchRepo;
    private final GenerateAccountNumber generateAccountNumber;

    public AccountCreationService(AccountRepo accountRepo, UserRepository userRepository, BranchRepo branchRepo,
            GenerateAccountNumber generateAccountNumber) {
        this.accountRepo = accountRepo;
        this.userRepository = userRepository;
        this.branchRepo = branchRepo;
        this.generateAccountNumber = generateAccountNumber;
    }

    /**
     * Create a new account for a user
     * 
     * @param accountCreation Account creation details
     * @param userId          User ID
     * @return Created Account entity
     * @throws RuntimeException if user not found or branch not found
     */
    public Account createAccount(AccountCreation accountCreation, Long userId) {
        // Fetch user from database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch branch from database
        BankBranch branch = branchRepo.findById(accountCreation.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with ID: " + accountCreation.getBranchId()));

        // Generate account number
        String accountNumber = generateAccountNumber.generate();

        // Create new account object
        Account account = new Account();
        account.setAccountType(AccountType.valueOf(accountCreation.getAccountType()));
        account.setBalance(0.0);
        account.setAccountNumber(accountNumber);
        account.setUser(user);
        account.setStatus(AccountStatus.ACTIVE);
        account.setBranch(branch);

        // Extract last 4 digits of account number
        if (accountNumber.length() >= 4) {
            account.setLast4AccountNumber(
                    Long.parseLong(accountNumber.substring(accountNumber.length() - 4)));
        }

        return accountRepo.save(account);
    }
}
