package com.example.bank.services.account;

import com.example.bank.DTO.Account.AccountCreation;
import com.example.bank.model.Account;
import com.example.bank.model.User;
import com.example.bank.repo.AccountRepo;
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

    public AccountCreationService(AccountRepo accountRepo, UserRepository userRepository) {
        this.accountRepo = accountRepo;
        this.userRepository = userRepository;
    }

    /**
     * Create a new account for a user
     * 
     * @param accountCreation Account creation details
     * @param userId          User ID
     * @return Created Account entity
     * @throws RuntimeException if user not found
     */
    public Account createAccount(AccountCreation accountCreation, Long userId) {
        // Fetch user from database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate account number
        String accountNumber = new GenerateAccountNumber().generate();

        // Create new account object
        Account account = new Account();
        account.setAccountType(accountCreation.getAccountType());
        account.setBalance(0.0);
        account.setAccountNumber(accountNumber);
        account.setUser(user);
        account.setStatus("ACTIVE");

        // Extract last 4 digits of account number
        if (accountNumber.length() >= 4) {
            account.setLast4AccountNumber(
                    Long.parseLong(accountNumber.substring(accountNumber.length() - 4)));
        }

        // TODO: Set branch from accountCreation or default branch
        // account.setBranch(branchId);

        return accountRepo.save(account);
    }
}
