package com.example.bank.controller;

import com.example.bank.DTO.Admin.AdminLoginRequest;
import com.example.bank.DTO.User.UserLoginRequest;
import com.example.bank.DTO.response.AuthResponse;
import com.example.bank.security.JwtTokenProvider;
import com.example.bank.services.admin.AdminAuthenticationService;
import com.example.bank.services.user.UserAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAuthenticationService userAuthService;
    private final AdminAuthenticationService adminAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserAuthenticationService userAuthService, AdminAuthenticationService adminAuthService,
            JwtTokenProvider jwtTokenProvider) {
        this.userAuthService = userAuthService;
        this.adminAuthService = adminAuthService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthResponse> userLogin(@Valid @RequestBody UserLoginRequest request) {
        Long id = userAuthService.login(request.getEmail(), request.getPassword());
        String token = jwtTokenProvider.generateToken(id.toString(), "USER");
        return ResponseEntity.ok(new AuthResponse(token, "USER", id));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> adminLogin(@Valid @RequestBody AdminLoginRequest request) {
        Long id = adminAuthService.login(request.getEmail(), request.getPassword());
        String token = jwtTokenProvider.generateToken(id.toString(), "ADMIN");
        return ResponseEntity.ok(new AuthResponse(token, "ADMIN", id));
    }
}
