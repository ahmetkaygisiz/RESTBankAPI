package com.restbank;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Account;
import com.restbank.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sun.net.www.content.text.Generic;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AccountControllerTests {

    private static final String API_1_0_ACCOUNTS = "/api/1.0/accounts";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    AccountRepository accountRepository;

    @Before
    public void cleanUp(){
        accountRepository.deleteAll();
    }

    @Test
    public void postAccount_whenAccountIsValid_receiveOk() {
        Account account = TestUtil.createValidAccount();
        ResponseEntity<Object> responseEntity = postAccount(account, Object.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postAccount_whenAccountIsValid_accountSavedToDatabase() {
        Account account = TestUtil.createValidAccount();
        postAccount(account, Object.class);

        assertThat(accountRepository.count()).isEqualTo(1);
    }

    @Test
    public void postAccount_whenAccountIsValid_receiveSuccessMessage() {
        Account account = TestUtil.createValidAccount();
        ResponseEntity<GenericResponse> response = postAccount(account, GenericResponse.class);

        assertThat(response.getBody().getMessage()).isEqualTo("Account created");
    }

    @Test
    public void postAccount_whenAccountNumberHaveLessThanRequired_receiveBadRequest() {
        Account account = TestUtil.createValidAccount();
        account.setAccountNumber("123");
        ResponseEntity<GenericResponse> response = postAccount(account, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postAccount_whenAccountNumberHaveMoreThanRequired_receiveBadRequest() {
        Account account = TestUtil.createValidAccount();
        account.setAccountNumber("123456787654");
        ResponseEntity<GenericResponse> response = postAccount(account, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postAccount_whenAccountNumberHaveCharacters_receiveBadRequest() {
        Account account = TestUtil.createValidAccount();
        account.setAccountNumber("1ABc-324");
        ResponseEntity<GenericResponse> response = postAccount(account, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postAccount_whenBalanceGoesDownZero_receiveBadRequest() {
        Account account = TestUtil.createValidAccount();
        account.setBalance(-33.2);
        ResponseEntity<GenericResponse> response = postAccount(account, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postAccount_whenBalanceEqualToZero_receiveOK() {
        Account account = TestUtil.createValidAccount();
        account.setBalance(0.00);
        ResponseEntity<GenericResponse> response = postAccount(account, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postAccount_whenBalanceHaveMoreDigitThanRequired_receiveBadRequest() {
        Account account = TestUtil.createValidAccount();
        account.setBalance(12.332);
        ResponseEntity<GenericResponse> response = postAccount(account, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    public <T> ResponseEntity<T> postAccount(Object request, Class<T> response){
        return testRestTemplate.postForEntity(API_1_0_ACCOUNTS, request, response);
    }
}
