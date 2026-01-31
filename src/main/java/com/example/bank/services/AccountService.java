package com.example.bank.services;

import com.example.bank.DTO.Account.AccountCreation;
import com.example.bank.DTO.Account.AccountNonSensitive;
import com.example.bank.model.Account;
import com.example.bank.model.User;
import com.example.bank.repo.AccountRepo;
import com.example.bank.repo.userRepository;
import com.example.bank.utility.GenerateAccountNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AccountService {


    private final AccountRepo accountRepo;
    private final userRepository userRepository;

    public AccountService(AccountRepo accountRepo,
                          userRepository userRepository) {
        this.accountRepo = accountRepo;
        this.userRepository = userRepository;
    }

    public Account createAccount(AccountCreation accountCreation, Long userID)
    {
        //fetch user from db
        User user=userRepository.findById(userID)
                .orElseThrow(()->new RuntimeException("User not found"));

        //Account number creation logic
            String accountNumber= new GenerateAccountNumber().generate();




        // create new account object
        Account account=new Account();
        account.setAccountType(accountCreation.getAccountType());
        account.setBalance(0.0);
        account.setAccountNumber(accountNumber);
        account.setUser(user);

        return accountRepo.save(account);

    }




    //get allAccounts ids for the signed in user
    public List<Long> getAccounts(Long userID)
    {
        List<Account> accounts=accountRepo.findByUserId(userID);
        return accounts.stream()
                                .map(Account::getId)
                                                    .toList();

    }





    // get non sensitive data for the account




    //get sensitive data for the account





}
