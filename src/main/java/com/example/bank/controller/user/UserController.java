package com.example.bank.controller.user;

import com.example.bank.DTO.User.UserLoginRequest;
import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.DTO.response.ApiResponse;
import com.example.bank.model.NewUserApproval;
import com.example.bank.model.User;
import com.example.bank.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ========== User CRUD ==========

    @GetMapping("/users/")
    public ApiResponse<String> test() {
        return ApiResponse.success("hello");
    }

    @PostMapping("/users/create")
    public ApiResponse<NewUserApproval> createUser(@Valid @RequestBody UserRegisterRequest request) {
        NewUserApproval approval = userService.createUser(request);
        return ApiResponse.success("Registration submitted for approval", approval);
    }

    @PostMapping("/users/login")
    public ApiResponse<Long> userLogin(@Valid @RequestBody UserLoginRequest request) {
        Long userId = userService.login(request.getEmail(), request.getPassword());
        return ApiResponse.success("Login successful", userId);
    }

    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success("User deleted successfully");
    }

    @PutMapping("/users/{userId}/profile")
    public ApiResponse<Void> updateProfile(@PathVariable Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        userService.updateUserProfile(userId, name, email);
        return ApiResponse.success("Profile updated successfully");
    }

    @PostMapping("/users/{userId}/change-password")
    public ApiResponse<Void> changePassword(@PathVariable Long userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userService.changePassword(userId, oldPassword, newPassword);
        return ApiResponse.success("Password changed successfully");
    }

    @PostMapping("/users/{userId}/deactivate")
    public ApiResponse<Void> deactivateAccount(@PathVariable Long userId) {
        userService.deactivateAccount(userId);
        return ApiResponse.success("Account deactivated successfully");
    }
}
