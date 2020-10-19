package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Transaction;
import com.restbank.service.TransactionService;
import com.restbank.utils.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(Statics.API_1_0_TRANSACTIONS)
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping("/account2account")
    public GenericResponse transferBetweenAccounts(@Valid @RequestBody Transaction transaction){
        return transactionService.transferBetweenAccounts(transaction);
    }

    @PostMapping("/account2creditcard")
    public GenericResponse transferAccountToCreditCard(){
        return null;
    }

    @PostMapping("/creditcard2account")
    public GenericResponse transferCreditCardToAccount(){
        return null;
    }
}
