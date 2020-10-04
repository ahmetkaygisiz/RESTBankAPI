package com.restbank;

import com.restbank.domain.Account;
import com.restbank.domain.Role;
import com.restbank.domain.User;

import java.math.BigDecimal;
public class TestUtil {

    public static User createValidUser(){
        User user = new User();

        user.setFirstName("tmp");
        user.setLastName("tmp");
        user.setEmail("tmpUser@mail.com");
        user.setPassword("P4ssword");
        user.setPhoneNumber("1231321212");
        user.setActive(true);

        return user;
    }

    public static Role createRole(String name){
        Role role = new Role();
        role.setName(name);

        return role;
    }

    public static Account createValidAccount() {
        Account account = new Account();
        account.setBalance(new BigDecimal("23212.22"));

        return account;
    }
}
