package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.domain.Transaction;
import com.restbank.domain.User;
import com.restbank.domain.annotation.OnUpdate;
import com.restbank.domain.dto.UserVM;
import com.restbank.service.CustomerService;
import com.restbank.utils.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(Statics.API_1_0_CUSTOMERS)
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public GenericResponse<UserVM> getCustomerInfo(Principal principal){
        return customerService.getUserVMByEmail(principal.getName());
    }

    @PutMapping
    public GenericResponse updateUser(Principal principal, @Validated(OnUpdate.class) @RequestBody User user) throws InvocationTargetException, IllegalAccessException {
        return customerService.updateUser(principal.getName(), user);
    }

    // GET customer/AccountId/CreditCard
    // Transactions
    // Get Transactions

    @GetMapping("/accounts")
    public GenericResponse<List<Account>> getCustomerAccount(Principal principal){
        return customerService.getCustomerAccountList(principal.getName());
    }

    @GetMapping("/{accountNumber}/credit-card")
    public GenericResponse<CreditCard> getCustomerCreditCardInfo(@PathVariable("accountNumber") String accountNumber,
                                                                 Principal principal){
        return customerService.
                getCustomerCreditCardByAccountNumber(accountNumber, principal.getName());
    }

    @GetMapping("/{accountNumber}/transactions")
    public GenericResponse<List<Transaction>> getCustomerAccountTransactions(@PathVariable("accountNumber") String accountNumber,
                                                                             Principal principal){
        return customerService.
                getAccountTransactionByAccountNumber(accountNumber, principal.getName());
    }
}
