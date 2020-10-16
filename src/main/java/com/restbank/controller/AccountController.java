package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.api.Info;
import com.restbank.domain.Account;
import com.restbank.error.ApiError;
import com.restbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
        accountService.create(account);

        return new GenericResponse("Account created");
    }

    @GetMapping("/api/1.0/accounts")
    public GenericResponse getAccountLists(Pageable page){
        return accountService.getAccountLists(page);
    }

    @GetMapping("/api/1.0/accounts/{accountNumber}")
    public GenericResponse getAccountByAccountNumber(@PathVariable("accountNumber") String accountNumber){
        return accountService.getAccountByAccountNumber(accountNumber);
    }

    @PutMapping("/api/1.0/accounts/{accountNumber}")
    public GenericResponse updateUser(@Valid @RequestBody Account account){
        return accountService.updateAccount(account);
    }

    @DeleteMapping("/api/1.0/accounts/{accountNumber}")
    public GenericResponse deleteUser(@PathVariable("accountNumber") String accountNumber){
        return accountService.deleteAccount(accountNumber);
    }
}
