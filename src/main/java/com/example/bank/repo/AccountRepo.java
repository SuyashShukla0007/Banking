package com.example.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bank.model.Account;

import java.util.List;

public interface AccountRepo extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);

}
