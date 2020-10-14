package com.restbank;

import com.restbank.repository.*;
import com.restbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


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

    }

}
