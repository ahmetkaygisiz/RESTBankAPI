package com.restbank;

import com.restbank.domain.Account;
import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.domain.UserRole;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestUtil {

    public static User createValidUser(){
        User user = new User();

        user.setFirstName("tmp");
        user.setLastName("tmp");
        user.setEmail("tmpUser@mail.com");
        user.setPassword("P4ssword");
        user.setPhoneNumber("2341234232");
        user.setActive(true);

        return user;
    }

//    public static Map<User, Set<UserRole>> createValidUserAndUserRole(){
//        Map<User,  Set<UserRole>> userRoleMap = new HashMap<>();
//        User user = new User();
//
//        user.setFirstName("tmp");
//        user.setLastName("tmp");
//        user.setEmail("tmpUser@mail.com");
//        user.setPassword("P4ssword");
//        user.setPhoneNumber("2341234232");
//        user.setActive(true);
//
//        Set<UserRole> userRoles = new HashSet<>();
//        Role role2 = new Role();
//        role2.setName("USER");
//
//        userRoles.add(new UserRole(user, role2));
//
//        userRoleMap.put(user, userRoles);
//
//        return userRoleMap;
//    }
//

    public static User updateValidUser(User user){
        user.setFirstName("update");
        user.setLastName("update");
        user.setEmail("updated@mail.com");
        user.setPassword("P4ssword");
        user.setPhoneNumber("5643217687");
        user.setActive(true);

        return user;
    }

    public static User updateValidUserWithoutPassword(User user){
        user.setFirstName("update");
        user.setLastName("update");
        user.setEmail("updated@mail.com");
        user.setPhoneNumber("5643217687");
        user.setActive(true);

        return user;
    }

    public static User createValidUser(String username) {
        User user = createValidUser();
        user.setEmail(username);
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

    public static Account createValidAccount(double balance) {
        Account account = new Account();
        account.setBalance(new BigDecimal(balance));

        return account;
    }

}
