package com.example.bank.utility;

import org.springframework.stereotype.Component;

@Component
public class BranchCodeGenerator {

    /**
     * Generate a unique branch code from bank name and city.
     * Format: first 3 chars of bankName + first 3 chars of city + random 3-digit
     * number.
     * Example: "State Bank", "Mumbai" → "STAMUM742"
     */
    public String generateBranchCode(String bankName, String city) {
        if (bankName == null || bankName.length() < 3) {
            throw new IllegalArgumentException("Bank name must be at least 3 characters");
        }
        if (city == null || city.length() < 3) {
            throw new IllegalArgumentException("City must be at least 3 characters");
        }
        String code = bankName.substring(0, 3).toUpperCase() + city.substring(0, 3).toUpperCase();
        code += String.format("%03d", (int) (Math.random() * 1000));
        return code;
    }
}