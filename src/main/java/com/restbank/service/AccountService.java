package com.restbank.service;

import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.domain.User;
import com.restbank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public void save(Account account){
        accountRepository.save(account);
    }

    public void delete(Account account){
        accountRepository.delete(account);
    }

    public void updateAccount(){
        // bakcaz
    }

    public Account getAccountByNumber(String accountNumber){
        return accountRepository.findAccountByAccountNumber(accountNumber);
    }

    public Account getAccountByUser(User user){
        return accountRepository.findByUser(user);
    }

    public Account getAccountByCreditCard(CreditCard creditCard){
        return accountRepository.findByCreditCard(creditCard);
    }
}
