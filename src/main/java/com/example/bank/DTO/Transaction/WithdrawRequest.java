package com.example.bank.DTO.Transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawRequest {
    private Long accountId;
    private Double amount;
}
