package com.example.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bank.model.NewUserApproval;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewUsers extends JpaRepository<NewUserApproval, Long> {
    List<NewUserApproval> findByRole(String role);

    Optional<NewUserApproval> findByEmail(String email);
}
