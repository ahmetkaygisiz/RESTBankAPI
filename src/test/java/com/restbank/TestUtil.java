package com.restbank;

import com.restbank.domain.*;
import com.restbank.utils.Statics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestUtil {

    public static User createValidUser(){
        User user = new User();

        user.setFirstName("tmp");
        user.setLastName("tmp");
        user.setEmail("tmpUser@mail.com");
        user.setPassword("P4ssword");
        user.setPhoneNumber("1234567890");
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
//        userRoleMap.put(user, userRoles);
//
//        return userRoleMap;
//    }

    public static User updateValidUser(){
        User user = new User();
        user.setFirstName("update");
        user.setLastName("update");
        user.setPassword("P4ssword");
        user.setActive(true);

        return user;
    }

    public static User updateValidUserWithoutPassword(){
        User user = new User();
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

    public static Transaction createTransaction(String from, String to) {
        Transaction transaction = new Transaction();
        transaction.setTransferFrom(from);
        transaction.setTransferTo(to);
        transaction.setAmount(new BigDecimal("123.22"));
        transaction.setDescription("Some description");

        return transaction;
    }

    public static CreditCard createCreditCard(){
        CreditCard creditCard = new CreditCard();
        creditCard.setBankCode(Statics.BANK_CODE_FIRST);
        creditCard.setBranchCode(Statics.BRANCH_CODE_BILECIK);

        return creditCard;
    }
}
