package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.CreditCard;
import com.restbank.service.CreditCardService;
import com.restbank.utils.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Statics.API_1_0_CREDIT_CARDS)
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @GetMapping
    public GenericResponse<List<CreditCard>> getCreditCardList(Pageable page){
        return creditCardService.getCreditCardList(page);
    }

    @PutMapping("{id}")
    public GenericResponse getCreditCardList(@Valid @RequestBody CreditCard creditCard){
        return creditCardService.updateCreditCard(creditCard);
    }

    @DeleteMapping("{id}")
    public GenericResponse getCreditCardList(@PathVariable("id") Long id){
        return creditCardService.deleteCreditCard(id);
    }
}
