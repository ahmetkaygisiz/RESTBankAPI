package com.restbank;

import com.restbank.domain.*;
import com.restbank.repository.*;
import com.restbank.service.AccountService;
import com.restbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
public class RestBankApplication implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(RestBankApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setFirstName("admin");
        user1.setLastName("admin");
        user1.setPassword("P4ssword");
        user1.setEmail("test@mail.com");
        user1.setPhoneNumber("1231231212");
        user1.setActive(true);

        Set<UserRole> userRoles = new HashSet<>();
        Role role1 = new Role();
        role1.setName("ADMIN");
        Role role2 = new Role();
        role2.setName("USER");

        userRoles.add(new UserRole(user1, role1));
        userRoles.add(new UserRole(user1, role2));

        User userInDB = userService.create(user1, userRoles);

        Account account = new Account();
        account.setBalance(new BigDecimal("123.22"));

        userService.addUserAccount(userInDB.getId(), account);
    }

}
