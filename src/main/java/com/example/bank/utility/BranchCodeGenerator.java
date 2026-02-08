package com.example.bank.utility;

public class BranchCodeGenerator {
    // Method to generate unique branch code
    public String generateBranchCode(String bankName, String city) {
        String code = bankName.substring(0, 3).toUpperCase() + city.substring(0, 3).toUpperCase();
        code += (int) (Math.random() * 1000);
        return code;
    }
}