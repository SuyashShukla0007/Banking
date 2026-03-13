package com.example.bank.repo;

import com.example.bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<com.example.bank.model.User, Long> {

    Optional<User> findByEmail(String email);
}
