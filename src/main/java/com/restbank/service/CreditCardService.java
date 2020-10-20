package com.restbank.service;

import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.repository.CreditCardRepository;

import com.restbank.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CreditCardService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CreditCardRepository creditCardRepository;

    // update CreditCard

    // delete CreditCard

    // checkBalance()

    // payment
}
