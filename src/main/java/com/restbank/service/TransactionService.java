package com.restbank.service;

import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    @Autowired
    AccountService accountService;

    @Autowired
    CreditCardService creditCardService;

    public void transferBetweenAccounts(Account from, Account to, BigDecimal amount, String description){
        from.setBalance(from.getBalance().subtract(amount));
        from.getTransactionList().add(new Transaction(description,amount.negate()));

        to.setBalance(to.getBalance().add(amount));
        to.getTransactionList().add(new Transaction(description,amount));

        accountService.updateAccount(from);
        accountService.updateAccount(to);
    }

    public void transferAccountToCreditCard(Account from, CreditCard to, BigDecimal amount){
        from.setBalance(from.getBalance().subtract(amount));

        to.setAvaiableBalance(to.getAvaiableBalance().add(amount));

        // update accounts.
    }

    public void transferCreditCardToAccount(CreditCard from, Account to, BigDecimal amount){
        from.setAvaiableBalance(from.getAvaiableBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        // update accounts.
    }
}
