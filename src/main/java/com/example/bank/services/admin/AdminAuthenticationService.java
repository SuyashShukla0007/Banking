package com.example.bank.services.admin;

import com.example.bank.exception.InvalidPasswordException;
import com.example.bank.model.Admin;
import com.example.bank.repo.AdminRepo;
import jakarta.persistence.EntityNotFoundException;
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

    public void changePassword(Long adminId, String oldPassword, String newPassword) {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + adminId + " not found"));

        if (!passwordEncoder.matches(oldPassword, admin.getPassword())) {
            throw new InvalidPasswordException("Old password is incorrect");
        }

        admin.setPassword(passwordEncoder.encode(newPassword));
        adminRepo.save(admin);
    }
}