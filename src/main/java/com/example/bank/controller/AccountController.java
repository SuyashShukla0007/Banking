package com.example.bank.controller;

import com.example.bank.services.AccountService;

public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService)
    {
        this.accountService=accountService;
    }




}
