package com.example.bank.services.user;

import com.example.bank.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.enums.ApprovalStatus;
import com.example.bank.enums.UserRole;
import com.example.bank.enums.UserStatus;
import com.example.bank.exception.DuplicateEmailException;
import com.example.bank.model.NewUserApproval;
import com.example.bank.model.User;
import com.example.bank.repo.NewUsers;
import com.example.bank.repo.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewUserApprovalService {

    private final NewUsers newUsers;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public NewUserApprovalService(NewUsers newUsers, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.newUsers = newUsers;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public NewUserApproval createUserApprovalRequest(UserRegisterRequest request) throws DuplicateEmailException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email is already registered");
        }
        if (newUsers.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("A registration request for this email is already pending");
        }

        NewUserApproval approval = new NewUserApproval();
        approval.setEmail(request.getEmail());
        approval.setName(request.getName());
        approval.setPassword(passwordEncoder.encode(request.getPassword()));
        approval.setRole(UserRole.USER);
        // status and requestedAt are set by @PrePersist

        return newUsers.save(approval);
    }

    public List<NewUserApproval> getAllPendingUserApprovals() {
        return newUsers.findByStatus(ApprovalStatus.PENDING);
    }

    @Transactional
    public User approveUserRegistration(Long approvalId, Long adminId) {
        NewUserApproval approval = newUsers.findById(approvalId)
                .orElseThrow(() -> new ResourceNotFoundException("Approval request not found with ID: " + approvalId));

        if (approval.getStatus() != ApprovalStatus.PENDING) {
            throw new IllegalArgumentException("Approval request is not in PENDING status");
        }

        // Create actual User from approval
        User user = new User();
        user.setName(approval.getName());
        user.setEmail(approval.getEmail());
        user.setPassword(approval.getPassword()); // Already encoded
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        // Update approval status
        approval.setStatus(ApprovalStatus.APPROVED);
        approval.setReviewedAt(LocalDateTime.now());
        newUsers.save(approval);

        return savedUser;
    }

    @Transactional
    public void rejectUserRegistration(Long approvalId, Long adminId, String reason) {
        NewUserApproval approval = newUsers.findById(approvalId)
                .orElseThrow(() -> new ResourceNotFoundException("Approval request not found with ID: " + approvalId));

        if (approval.getStatus() != ApprovalStatus.PENDING) {
            throw new IllegalArgumentException("Approval request is not in PENDING status");
        }

        approval.setStatus(ApprovalStatus.REJECTED);
        approval.setReviewedAt(LocalDateTime.now());
        approval.setRejectionReason(reason);
        newUsers.save(approval);
    }

    public NewUserApproval getApprovalById(Long approvalId) {
        return newUsers.findById(approvalId)
                .orElseThrow(() -> new ResourceNotFoundException("Approval not found with ID: " + approvalId));
    }

    public NewUserApproval getApprovalByEmail(String email) {
        return newUsers.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No approval found for email: " + email));
    }

    public List<NewUserApproval> getApprovalsByStatus(ApprovalStatus status) {
        return newUsers.findByStatus(status);
    }

    @Transactional
    public int deleteRejectedApprovals() {
        List<NewUserApproval> rejected = newUsers.findByStatus(ApprovalStatus.REJECTED);
        int count = rejected.size();
        newUsers.deleteAll(rejected);
        return count;
    }
}
