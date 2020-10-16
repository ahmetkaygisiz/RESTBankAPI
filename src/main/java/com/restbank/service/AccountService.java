package com.restbank.service;

import com.restbank.api.GenericResponse;
import com.restbank.api.Info;
import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.domain.User;
import com.restbank.error.NotFoundException;
import com.restbank.repository.AccountRepository;
import com.restbank.utils.Statics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public Account create(Account account){
        return accountRepository.save(account);
    }

    public GenericResponse<Account> getAccountByAccountNumber(String accountNumber){
        Account accountInDB = accountRepository.findAccountByAccountNumber(accountNumber);

        if( accountInDB == null){
            log.error("getAccountByAccountNumber ## Account not found with account number = " + accountNumber);
            throw new NotFoundException("Account not found with account number = " + accountNumber);
        }

        return new GenericResponse<>(accountInDB);
    }

    public GenericResponse<List<Account>> getAccountLists(Pageable page){
         Page<Account> pageAccounts = accountRepository.findAll(page);
         Info info = new Info(pageAccounts, Statics.API_1_0_ACCOUNTS);

         return new GenericResponse<>(info, pageAccounts.getContent());
    }

    public GenericResponse deleteAccount(String accountNumber){
        try {
            accountRepository.deleteByAccountNumber(accountNumber);
        }
        catch (IllegalArgumentException | EmptyResultDataAccessException e){
            log.error("deleteAccount ## Account not found with account number =" + accountNumber);
            throw new NotFoundException("Account not exists with account number =" + accountNumber);
        }
        return new GenericResponse("Account deleted.");
    }

    public GenericResponse<Account> updateAccount(Account account){
        Account accountInDB = accountRepository.findAccountByAccountNumber(account.getAccountNumber());

        accountInDB.setBalance(account.getBalance());
        accountInDB.setUser(account.getUser());
        accountInDB.setCreditCard(account.getCreditCard());
        accountInDB.setTransactionList(account.getTransactionList());

        accountRepository.save(accountInDB);

        return new GenericResponse<>("Account updated");
    }
}
