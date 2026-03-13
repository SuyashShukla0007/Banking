package com.example.bank.DTO.Account;

import com.example.bank.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreation {

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Branch ID is required")
    private Long branchId;

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

}
