package com.example.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bank.model.Account;
public interface AccountService extends JpaRepository<Account, Long> {
}
