package com.example.bank.DTO.response;

public class AuthResponse {
    private String token;
    private String role;
    private Long id;

    public AuthResponse(String token, String role, Long id) {
        this.token = token;
        this.role = role;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public Long getId() {
        return id;
    }
}
