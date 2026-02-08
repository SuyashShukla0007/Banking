package com.example.bank.DTO.Branch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchUpdateRequest {
    public Long branchId;
    private String branchName;
    private String address;
    private String city;
    private String state;
}