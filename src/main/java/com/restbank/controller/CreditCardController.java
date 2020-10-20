package com.restbank.controller;

import com.restbank.api.GenericResponse;
import com.restbank.domain.CreditCard;
import com.restbank.service.CreditCardService;
import com.restbank.utils.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(Statics.API_1_0_CREDIT_CARDS)
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

}
