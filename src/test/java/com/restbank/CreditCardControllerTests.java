package com.restbank;

import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.error.ApiError;
import com.restbank.repository.AccountRepository;
import com.restbank.repository.CreditCardRepository;
import com.restbank.service.AccountService;
import com.restbank.service.CreditCardService;
import com.restbank.utils.Statics;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreditCardControllerTests {

    private static final String API_1_0_CREDIT_CARDS = Statics.API_1_0_CREDIT_CARDS + "/000000001";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Before
    public void clean(){
        creditCardRepository.deleteAll();
    }

}
