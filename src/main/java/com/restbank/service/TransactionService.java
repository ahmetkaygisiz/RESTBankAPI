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

    public GenericResponse transferAccountToCreditCard(Transaction transaction){

        Account from = accountService.getAccountByAccountNumber(transaction.getTransferFrom());
        from.setBalance(from.getBalance().subtract(transaction.getAmount()));

        // check from account balance succificient
        isBalanceSuccificient(transaction.getAmount(), from.getBalance());

        CreditCard to = creditCardService.getCreditCardByCardNumber(transaction.getTransferTo());

        // Subtract usedAmount in creditCard
        to.setUsedAmount(to.getUsedAmount().subtract(transaction.getAmount()));

        accountService.updateAccount(from);
        creditCardService.updateCreditCard(to);
        transactionRepository.save(transaction);

        return new GenericResponse("Transfer completed.");
    }

    public GenericResponse transferCreditCardToAccount(Transaction transaction){
        CreditCard from = creditCardService.getCreditCardByCardNumber(transaction.getTransferFrom());
        from.setUsedAmount(from.getUsedAmount().add(transaction.getAmount()));

        // check from creditCard maxLimit succificient
        isBalanceSuccificient(from.getUsedAmount(), from.getMaxLimit());

        Account to = accountService.getAccountByAccountNumber(transaction.getTransferTo());
        to.setBalance(to.getBalance().add(transaction.getAmount()));

        creditCardService.updateCreditCard(from);
        accountService.updateAccount(to);
        transactionRepository.save(transaction);

        return new GenericResponse("Transfer completed.");
    }

    private void isBalanceSuccificient(BigDecimal amount, BigDecimal limit){
        if (amount.compareTo(limit) == 1) // compare to method returns 1 if amount > limit else 0
            throw new InvalidTransactionException("Insufficient balance/limit");
    }

    private void isFromAndToSame(String from, String to){
        if (from.equalsIgnoreCase(to))
            throw new InvalidTransactionException("Receiver and sender account numbers cannot be the same");
    }
}

