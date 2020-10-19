package com.restbank.service;

import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.repository.CreditCardRepository;
import com.restbank.utils.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreditCardService {

    @Autowired
    CreditCardRepository creditCardRepository;

    public void create(){
//        CreditCard creditCard = new CreditCard();
//
//        creditCard.setBankCode(Statics.BANK_CODE_FIRST);
//        creditCard.setBranchCode(Statics.BRANCH_CODE_BILECIK);
//        creditCard.setAccount(new Account());
//
//        creditCard.setMaxLimit(new BigDecimal(""));
//        creditCard.setAvaiableBalance(new BigDecimal(""));
//        creditCard.setExpireDate();
//
//         creditCard.
    }
}
