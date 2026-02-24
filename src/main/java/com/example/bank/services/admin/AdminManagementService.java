package com.example.bank.services.admin;

import com.example.bank.DTO.Admin.AdminUpdateRequest;
import com.example.bank.model.Admin;
import com.example.bank.repo.AdminRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminManagementService {

    private final AdminRepo adminRepo;

    public AdminManagementService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public Admin updateAdmin(Long adminId, AdminUpdateRequest request) {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + adminId + " not found"));

        if (request.getEmail() != null && !request.getEmail().isEmpty())
            admin.setEmail(request.getEmail());
        if (request.getName() != null && !request.getName().isEmpty())
            admin.setName(request.getName());

        return adminRepo.save(admin);
    }

    public void deleteAdmin(Long adminId) {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + adminId + " not found"));
        adminRepo.delete(admin);
    }
}
