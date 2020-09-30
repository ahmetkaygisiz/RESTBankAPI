package com.restbank;

import com.restbank.domain.Account;
import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.repository.RoleRepository;
import com.restbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestUtil {

    private static final String API_1_0_ACCOUNTS = "/api/1.0/accounts";
    private static final String API_1_0_USERS = "/api/1.0/users";

    public static Role createValidRole(){
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        return role;
    }

    public static Role createValidRole(String name){
        Role role = new Role();
        role.setName(name);

        return role;
    }

    public static User createValidUser(){
        User user = new User();

        user.setFirstName("Ahmet");
        user.setLastName("TestUser");
        user.setEmail("ahmetkaygisiz@gmail.com");
        user.setPassword("P4ssw4rDd");
        user.setPhoneNumber("5066758941");
        user.setActive(true);

        return user;
    }

    public static Account createValidAccount() {
        Account account = new Account();
        account.setBalance(new BigDecimal("23212.22"));

        return account;
    }
}
