package com.example.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bank.model.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {

    String findByEmail(String email);

}
