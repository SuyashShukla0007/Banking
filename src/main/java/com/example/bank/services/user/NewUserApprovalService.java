package com.example.bank.services.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.exception.DuplicateEmailException;
import com.example.bank.model.NewUserApproval;
import com.example.bank.model.User;
import com.example.bank.repo.NewUsers;
import com.example.bank.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NewUserApprovalService {

    // Inject NewUsers repository
    private NewUsers newUsers;

    // Inject UserRepository
    private UserRepository userRepository;

    // Inject PasswordEncoder
    private final PasswordEncoder passwordEncoder;

    public NewUserApprovalService(NewUsers newUsers, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.newUsers = newUsers;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Method: createUserApprovalRequest(UserRegisterRequest request)
    // - Check if email already exists in users or pending approvals
    // - Create NewUserApproval with status "PENDING"
    // - Encode and set password
    // - Set requestedAt timestamp
    // - Save and return approval request

    @Transactional
    public void createUserApprovalRequest(UserRegisterRequest request) throws DuplicateEmailException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("email is being used by another user");
        } else if (newUsers.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("This email is under progress for registration");
        }

        NewUserApproval newUser = new NewUserApproval();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("USER");

        newUsers.save(newUser);

        return;

    }

    // Method: getAllPendingUserApprovals()
    // - Find all user approvals with status "PENDING"
    // - Return list of pending user approvals
    public List<NewUserApproval> getAllPendingUserApprovals() {
        return newUsers.findAll();
    }

    // Method: approveUserRegistration(Long approvalId, Long adminId)
    // - Find approval by ID
    // - Validate approval exists and status is "PENDING"
    // - Create actual User from approval data
    // - Set user role to "USER"
    // - Save user
    // - Update approval status to "APPROVED"
    // - Set reviewedAt timestamp
    // - Save and return approval details
    @Transactional
    public User approveUserRegistration(Long approvalId, Long adminId) {
        NewUserApproval approval = newUsers.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found with ID: " + approvalId));

        // Create actual User from approval
        User user = new User();
        user.setName(approval.getName());
        user.setEmail(approval.getEmail());
        user.setPassword(approval.getPassword()); // Already encoded
        user.setRole("USER");
        user.setStatus("ACTIVE");

        User savedUser = userRepository.save(user);

        // Delete the approval record after successful user creation
        newUsers.delete(approval);

        return savedUser;
    }

    // Method: rejectUserRegistration(Long approvalId, Long adminId, String reason)
    // - Find approval by ID
    // - Validate approval exists and status is "PENDING"
    // - Update status to "REJECTED"
    // - Set reviewedAt timestamp
    // - Save rejection reason
    // - Save and return approval details
    @Transactional
    public void rejectUserRegistration(Long approvalId, Long adminId, String reason) {
        NewUserApproval approval = newUsers.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval request not found with ID: " + approvalId));

        // Simply delete the rejected approval
        newUsers.delete(approval);
    }

    // Method: getApprovalById(Long approvalId)
    // - Find user approval by ID
    // - Return approval details
    public NewUserApproval getApprovalById(Long approvalId) {
        return newUsers.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval not found with ID: " + approvalId));
    }

    // Method: getApprovalByEmail(String email)
    // - Find user approval by email
    // - Return approval if exists
    public NewUserApproval getApprovalByEmail(String email) {
        return newUsers.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No approval found for email: " + email));
    }

    // Method: getApprovalsByStatus(String status)
    // - Find all user approvals with given status (PENDING/APPROVED/REJECTED)
    // - Return list of approvals
    public List<NewUserApproval> getApprovalsByStatus(String status) {
        // Since NewUserApproval doesn't have status field, all records are pending
        return newUsers.findAll();
    }

    // Method: deleteRejectedApprovals()
    // - Find all rejected approvals older than certain period
    // - Delete old rejected approval records
    // - Return count of deleted records
    @Transactional
    public int deleteRejectedApprovals() {
        // Since we delete rejections immediately, this is for cleanup
        // Could add timestamp field later for automated cleanup
        return 0;
    }

}
