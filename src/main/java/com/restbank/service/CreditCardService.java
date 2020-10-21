package com.restbank.service;

import com.restbank.api.GenericResponse;
import com.restbank.api.Info;
import com.restbank.domain.CreditCard;
import com.restbank.error.exceptions.NotFoundException;
import com.restbank.error.exceptions.NotAcceptableException;
import com.restbank.repository.CreditCardRepository;

import com.restbank.utils.Statics;
import com.restbank.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class CreditCardService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CreditCardRepository creditCardRepository;

    public CreditCard getCreditCardById(Long id){
        CreditCard creditCard = creditCardRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Credit Card not found with id = " + id);
        });

        return creditCard;
    }

    // I think this method should be developed. But If it works, you know...
    public CreditCard getCreditCardByCardNumber(String cardNumber){
        Long id = Long.valueOf(cardNumber.substring(8)); // get last 8 number -> it wll be creditCard.id

        CreditCard creditCard = creditCardRepository.findById(id).orElseThrow(() -> {
            log.error("Credit Card not found with cardNumber = " + cardNumber);
            throw new NotFoundException("Credit Card not found with cardNumber = " + cardNumber);
        });

        return creditCard;
    }

    public CreditCard createCreditCard(CreditCard creditCard){
        creditCard.setCvc(Utils.generateCvcNumber());
        creditCard.setExpireDate(Utils.generateExpirationDate());

        return  creditCardRepository.save(creditCard);
    }

    public GenericResponse<List<CreditCard>> getCreditCardList(Pageable page) {
        Page<CreditCard> creditCardList = creditCardRepository.findAll(page);
        Info info = new Info(creditCardList, Statics.API_1_0_CREDIT_CARDS);

        return new GenericResponse<List<CreditCard>>(info, creditCardList.getContent());
    }

    // update CreditCard
    public GenericResponse updateCreditCard(CreditCard creditCard){
        CreditCard creditCardInDB = getCreditCardById(creditCard.getId());

        creditCardInDB .setBranchCode(creditCard.getBranchCode());
        creditCardInDB.setBankCode(creditCard.getBankCode());
        creditCardInDB.setUsedAmount(creditCard.getUsedAmount());
        creditCardInDB.setMaxLimit(creditCard.getMaxLimit());

        creditCardRepository.save(creditCardInDB);

        return new GenericResponse("Credit Card updated.");
    }

    // delete CreditCard
    public GenericResponse deleteCreditCard(Long id){
        CreditCard creditCard = getCreditCardById(id);

        if (creditCard.getUsedAmount().intValue() > 0 )
            throw new NotAcceptableException("Credit Card cannot be deleted because card has debt");

        creditCardRepository.deleteById(id);
        return new GenericResponse("Credit card deleted");
    }
}
