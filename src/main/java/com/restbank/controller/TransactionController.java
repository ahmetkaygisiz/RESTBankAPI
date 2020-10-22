package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Transaction;
import com.restbank.service.TransactionService;
import com.restbank.utils.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
    public GenericResponse transferAccountToCreditCard(@Valid @RequestBody Transaction transaction){
        return transactionService.transferAccountToCreditCard(transaction);
    }

    @PostMapping("/creditcard2account")
    public GenericResponse transferCreditCardToAccount(@Valid @RequestBody Transaction transaction){
        return transactionService.transferCreditCardToAccount(transaction);
    }

    @GetMapping
    public GenericResponse getTransactions(Pageable pageable){
        return transactionService.getTransactionList(pageable);
    }

    @GetMapping("/{id}")
    public GenericResponse getTransactionById(@PathVariable("id") Long id){
        return transactionService.getTransactionById(id);
    }
}
