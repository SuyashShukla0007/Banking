package com.example.bank.DTO.Admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private Long adminId;
    private String oldPassword;
    private String newPassword;
}
