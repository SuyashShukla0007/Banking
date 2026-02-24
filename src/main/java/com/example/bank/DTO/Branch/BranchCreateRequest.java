package com.example.bank.DTO.Branch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchCreateRequest {
    private String branchName;
    private String branchCode;
    private String address;
    private String city;
    private String state;
}
