package com.example.bank.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    // Inject AdminService

    // Constructor injection

    // POST /admin/create
    // Request Body: { "name": String, "email": String, "password": String }
    // - Validate email format
    // - Validate password strength (optional for v1)
    // - Call adminService.createAdmin(name, email, password)
    // - Return success message or created admin details (without password)
    // - Handle DuplicateEmailException

    // GET /admin/{adminId}
    // Path Variable: adminId
    // - Call adminService.getAdminById(adminId)
    // - Return admin details (without password)
    // - Handle EntityNotFoundException

    // PUT /admin/{adminId}
    // Path Variable: adminId
    // Request Body: { "name": String, "email": String }
    // - Validate input (at least one field should be provided)
    // - Call adminService.updateAdmin(adminId, name, email)
    // - Return updated admin details
    // - Handle EntityNotFoundException, DuplicateEmailException

    // DELETE /admin/{adminId}
    // Path Variable: adminId
    // - Call adminService.deleteAdmin(adminId)
    // - Return success message
    // - Handle EntityNotFoundException
    // - Optional: Prevent deleting last admin

    // POST /admin/{adminId}/change-password
    // Path Variable: adminId
    // Request Body: { "oldPassword": String, "newPassword": String }
    // - Validate new password strength
    // - Call adminService.changePassword(adminId, oldPassword, newPassword)
    // - Return success message
    // - Handle InvalidPasswordException, EntityNotFoundException

    // GET /admin/users
    // - Call adminService.getAllUsers()
    // - Return list of all users in the system
    // - Optional: Add pagination parameters (page, size)
    // - Optional: Filter by status (ACTIVE, SUSPENDED, PENDING)

    // GET /admin/users/{userId}
    // Path Variable: userId
    // - Call adminService.getUserById(userId)
    // - Return user details with associated accounts
    // - Handle EntityNotFoundException

    // POST /admin/users/{userId}/suspend
    // Path Variable: userId
    // - Call adminService.suspendUser(userId)
    // - This suspends the user and all their accounts
    // - Return success message
    // - Handle EntityNotFoundException

    // POST /admin/users/{userId}/activate
    // Path Variable: userId
    // - Call adminService.activateUser(userId)
    // - This activates a suspended user
    // - Return success message
    // - Handle EntityNotFoundException

    // GET /admin/users/{userId}/accounts
    // Path Variable: userId
    // - Call adminService.getAccountsByUserId(userId)
    // - Return list of all accounts for the user
    // - Handle EntityNotFoundException

    // GET /admin/transactions
    // - Call adminService.viewAllTransactions()
    // - Return list of all transactions in the system
    // - Optional: Add pagination (page, size)
    // - Optional: Add filters (date range, type, amount range)

    // GET /admin/reports/system
    // - Call adminService.generateSystemReport()
    // - Return SystemReportDTO containing:
    // - Total number of users
    // - Total number of accounts
    // - Total number of transactions
    // - Total balance across all accounts
    // - Useful for admin dashboard overview

}
