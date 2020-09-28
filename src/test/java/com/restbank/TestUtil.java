package com.restbank;

import com.restbank.domain.Account;
import com.restbank.domain.User;

import java.math.BigDecimal;

public class TestUtil {
    public static User createValidUser(){
        User user = new User();

        user.setFirstName("Ahmet");
        user.setLastName("TestUser");
        user.setEmail("ahmetkaygisiz@gmail.com");
        user.setPassword("P4ssw4rDd");
        user.setPhoneNumber("5066758941");
        user.setActive(true);

        //user.setRoles();
        //user.setAccountList();

        return user;
    }

    public static Account createValidAccount() {
        Account account = new Account();

        account.setAccountNumber("12345678");
        account.setBalance(23212.22);

        return account;
    }
}
