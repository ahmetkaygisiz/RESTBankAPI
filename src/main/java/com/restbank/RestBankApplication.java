package com.restbank;

import com.restbank.domain.CreditCard;
import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.repository.*;
import com.restbank.service.UserService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.management.relation.RoleStatus;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class RestBankApplication implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(RestBankApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        saveRoles();
//        saveUser();
//        saveUserRole("ahmetkaygisiz@gmail.com",Arrays.asList("ADMIN","USER"));
    }
    public User saveUser(){
        User user = new User();

        user.setFirstName("Ahmet");
        user.setLastName("TestUser");
        user.setEmail("ahmetkaygisiz@gmail.com");
        user.setPassword("P4ssword");
        user.setPhoneNumber("5066758941");
        user.setActive(true);

        return userService.save(user);

    }

    public List<Role> saveRoles(){
        Role roleAdmin = roleRepository.save(createValidRole("ADMIN"));
        Role roleUser = roleRepository.save(createValidRole("USER"));

        return Arrays.asList(roleAdmin, roleUser);
    }

    public void saveUserRole(String email, List<String> roleNames){
        Set<Role> roles = new HashSet<>();
        for (String role : roleNames)
            roles.add(roleRepository.findByName(role));

        User userInDB = userService.getUserByEmail(email);
        userInDB.setRoles(roles);
        userService.updateUser(userInDB);
    }

    public Role createValidRole(String name){
        Role role = new Role();
        role.setName(name);

        return role;
    }
}
