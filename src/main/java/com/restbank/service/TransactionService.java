package com.restbank.service;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.domain.Transaction;
import com.restbank.error.exceptions.InvalidTransactionException;
import com.restbank.repository.AccountRepository;
import com.restbank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CreditCardService creditCardService;

    @Autowired
    TransactionRepository transactionRepository;

    public GenericResponse transferBetweenAccounts(Transaction transaction){
        // check account numbers
        isFromAndToSame(transaction.getTransferFrom(), transaction.getTransferTo());

        Account from = accountService.getAccountByAccountNumber(transaction.getTransferFrom());
        from.setBalance(from.getBalance().subtract(transaction.getAmount()));

        // check from account balance succificient
        isBalanceSuccificient(transaction.getAmount(), from.getBalance());

        Account to = accountService.getAccountByAccountNumber(transaction.getTransferTo());
        to.setBalance(to.getBalance().add(transaction.getAmount()));

        accountService.updateAccount(from);
        accountService.updateAccount(to);
        transactionRepository.save(transaction);

        return new GenericResponse("Transfer completed.");
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

    // controls
    private void isBalanceSuccificient(BigDecimal amount, BigDecimal accountBalance){
        if (amount.compareTo(accountBalance) == 1) // compare to method returns 1 if amount > accountBalance else 0
            throw new InvalidTransactionException("Insufficient balance");
    }

    private void isFromAndToSame(String from, String to){
        if (from.equalsIgnoreCase(to))
            throw new InvalidTransactionException("Receiver and sender account numbers cannot be the same");
    }
}

