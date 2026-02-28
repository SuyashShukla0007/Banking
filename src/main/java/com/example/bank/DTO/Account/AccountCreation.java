package com.example.bank.DTO.Account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreation {

    @NotBlank(message = "Account type is required")
    private String accountType;

    @NotNull(message = "Branch ID is required")
    private Long branchId;
}
