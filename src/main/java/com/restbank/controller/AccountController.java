package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Account;
import com.restbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/api/1.0/accounts")
    public GenericResponse createAccount(@Valid @RequestBody Account account){
        accountService.save(account);

        return new GenericResponse("Account created");
    }
}
