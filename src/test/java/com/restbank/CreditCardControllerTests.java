package com.restbank;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.error.ApiError;
import com.restbank.repository.AccountRepository;
import com.restbank.repository.CreditCardRepository;
import com.restbank.service.AccountService;
import com.restbank.service.CreditCardService;
import com.restbank.utils.Statics;
import org.apache.coyote.Response;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreditCardControllerTests {

    private static final String API_1_0_CREDIT_CARDS = Statics.API_1_0_CREDIT_CARDS;

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

    @Test
    public void getCreditCards_whenThereIsAnyRecordInDB_receiveOK() {
        creditCardService.createCreditCard(TestUtil.createCreditCard());
        ResponseEntity<GenericResponse<List<Object>>> response =
                getCreditCards(new ParameterizedTypeReference<GenericResponse<List<Object>>>() { });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getCreditCards_whenThereIsAnyRecordInDB_receiveData() {
        creditCardService.createCreditCard(TestUtil.createCreditCard());
        ResponseEntity<GenericResponse<List<Object>>> response =
                getCreditCards(new ParameterizedTypeReference<GenericResponse<List<Object>>>() { });

        assertThat(response.getBody().getData()).isNotNull();
    }

    @Test
    public void updateCreditCard_whenCreditCardValid_receiveOK() {
        CreditCard creditCardInDB = creditCardService.createCreditCard(TestUtil.createCreditCard());

        creditCardInDB.setUsedAmount(new BigDecimal("123.00"));
        creditCardInDB.setMaxLimit(new BigDecimal("1000.00"));

        HttpEntity<CreditCard> httpEntity = new HttpEntity<>(creditCardInDB);
        ResponseEntity<Object> response = putCreditCard(creditCardInDB.getId(), httpEntity, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteCreditCard_whenCallDeleteMethod_receiveOK() {
        CreditCard creditCardInDB = creditCardService.createCreditCard(TestUtil.createCreditCard());
        ResponseEntity<Object> response = deleteCreditCard(creditCardInDB.getId(), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteCreditCard_whenCreditCardNotFound_receiveApiError() {
        ResponseEntity<ApiError> response = deleteCreditCard(3L, ApiError.class);

        assertThat(response.getBody().getMessage()).contains("Credit Card not found with id =");
    }

    @Test
    public void deleteCreditCard_whenUsedAmountBiggerThanZero_receiveBadRequest() {
        CreditCard creditCard = TestUtil.createCreditCard();
        creditCard.setUsedAmount(new BigDecimal("123.00"));

        CreditCard creditCardInDB = creditCardService.createCreditCard(creditCard);
        ResponseEntity<Object> response = deleteCreditCard(creditCardInDB.getId(), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    }

    @Test
    public void deleteCreditCard_whenUsedAmountBiggerThanZero_receiveApiError() {
        CreditCard creditCard = TestUtil.createCreditCard();
        creditCard.setUsedAmount(new BigDecimal("123.00"));

        CreditCard creditCardInDB = creditCardService.createCreditCard(creditCard);
        ResponseEntity<ApiError> response = deleteCreditCard(creditCardInDB.getId(), ApiError.class);

        assertThat(response.getBody().getMessage()).isEqualTo("Credit Card cannot be deleted because card has debt");
    }

    public <T> ResponseEntity<T> getCreditCards(ParameterizedTypeReference<T> responseType){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(API_1_0_CREDIT_CARDS, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> putCreditCard(Long id, HttpEntity<CreditCard> requestEntity, Class<T> responseType){
        String path = API_1_0_CREDIT_CARDS + "/" + id;
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(path, HttpMethod.PUT, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> deleteCreditCard(Long id, Class<T> responseType ) {
        String path = API_1_0_CREDIT_CARDS + "/" + id;
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(path, HttpMethod.DELETE, null, responseType);
    }

}
