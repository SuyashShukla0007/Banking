package com.example.bank.DTO.User;

import lombok.Getter;

@Getter
public class UserRegisterRequest {
    private String name;
    private String email;
    private String password;
}

