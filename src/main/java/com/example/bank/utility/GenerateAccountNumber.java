package com.example.bank.utility;

import org.springframework.stereotype.Component;

@Component
public class GenerateAccountNumber {

    // Method to generate a unique account number for a new bank account
    public  String generate() {
        //complex method
        return "PNB" + System.currentTimeMillis();
    }

}
