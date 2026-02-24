package com.example.bank.services.admin;

import com.example.bank.model.Admin;
import com.example.bank.repo.AdminRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminQueryService {

    private final AdminRepo adminRepo;

    public AdminQueryService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public Admin getAdminById(Long adminId) {
        return adminRepo.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + adminId + " not found"));
    }
}
