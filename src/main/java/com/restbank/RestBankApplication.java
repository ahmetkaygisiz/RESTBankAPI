package com.restbank;

import com.restbank.domain.CreditCard;
import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.management.relation.RoleStatus;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class RestBankApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(RestBankApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
