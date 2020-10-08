package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.api.Info;
import com.restbank.domain.Account;
import com.restbank.error.ApiError;
import com.restbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/api/1.0/accounts")
    public GenericResponse createAccount(@Valid @RequestBody Account account){
        accountService.save(account);

        return new GenericResponse("Account created");
    }

    @GetMapping("/api/1.0/accounts")
    public List<Account> getAccountLists(){
        List<Account> accountList = accountService.getAccountLists();

        return accountList;
    }

}
