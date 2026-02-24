package com.example.bank.services.admin;

import com.example.bank.model.Admin;
import com.example.bank.repo.AdminRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthenticationService {

    private final AdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminAuthenticationService(AdminRepo adminRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Long login(String email, String password) {
        Admin admin = adminRepo.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return admin.getId();
    }
}