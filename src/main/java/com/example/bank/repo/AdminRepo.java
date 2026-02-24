package com.example.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bank.model.Admin;
import java.util.List;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {

    List<Admin> findByEmail(String email);

}
