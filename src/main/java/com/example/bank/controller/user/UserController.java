package com.example.bank.controller.user;

import com.example.bank.DTO.User.UserLoginRequest;
import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.services.user.UserAuthenticationService;
import com.example.bank.services.user.UserManagementService;
import com.example.bank.services.user.UserRegistrationService;
import com.example.bank.services.user.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRegistrationService userRegistrationService;
    private final UserAuthenticationService userAuthenticationService;
    private final UserManagementService userManagementService;
    private final UserService userService;

    public UserController(UserRegistrationService userRegistrationService,
            UserAuthenticationService userAuthenticationService,
            UserManagementService userManagementService,
            UserService userService) {
        this.userRegistrationService = userRegistrationService;
        this.userAuthenticationService = userAuthenticationService;
        this.userManagementService = userManagementService;
        this.userService = userService;
    }

    // testing
    @GetMapping("/")
    public String test() {
        return "hello";
    }

    // create user
    @PostMapping("/create")
    public String createUser(@RequestBody UserRegisterRequest request) {
        try {
            userRegistrationService.createUser(request);
            return "Successfull";
        } catch (RuntimeException e) {
            throw new RuntimeException("user email already exists " + e);
        }
    }

    // login user
    @PostMapping("/login")
    public String userLogin(@RequestBody UserLoginRequest request) {
        try {
            userAuthenticationService.login(request.getEmail(), request.getPassword());
            return "Login successful";
        } catch (RuntimeException e) {
            throw new RuntimeException("check credentials " + e);
        }
    }

    @GetMapping("/delete-user/{id}")
    public String delete(@PathVariable Long id) {
        try {
            userManagementService.deleteUser(id);
            return "user deleted successfully";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update user profile (name and/or email)
     * PUT /users/{userId}/profile?name=John&email=john@example.com
     */
    @PutMapping("/{userId}/profile")
    public String updateProfile(@PathVariable Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        try {
            userService.updateUserProfile(userId, name, email);
            return "Profile updated successfully";
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to update profile: " + e.getMessage());
        }
    }

    /**
     * Change user password
     * POST /users/{userId}/change-password?oldPassword=old123&newPassword=new456
     */
    @PostMapping("/{userId}/change-password")
    public String changePassword(@PathVariable Long userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            userService.changePassword(userId, oldPassword, newPassword);
            return "Password changed successfully";
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to change password: " + e.getMessage());
        }
    }

    /**
     * Deactivate user account
     * POST /users/{userId}/deactivate
     * 
     * NOTE: This endpoint calls the deactivateAccount method you need to implement
     * in UserService
     */
    @PostMapping("/{userId}/deactivate")
    public String deactivateAccount(@PathVariable Long userId) {
        try {
            userService.deactivateAccount(userId);
            return "Account deactivated successfully";
        } catch (UnsupportedOperationException e) {
            return "⚠️ TODO: Complete the deactivateAccount() method in UserService first!";
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to deactivate account: " + e.getMessage());
        }
    }

}
