package com.example.bank.services.admin;

import com.example.bank.DTO.Admin.AdminRegisterRequest;
import com.example.bank.exception.DuplicateEmailException;
import com.example.bank.model.Admin;
import com.example.bank.repo.AdminRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminRegistrationService {

    private final AdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminRegistrationService(AdminRepo adminRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Admin createAdmin(AdminRegisterRequest request) {
        if (adminRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Admin with email '" + request.getEmail() + "' already exists");
        }

        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        return adminRepo.save(admin);
    }
}
