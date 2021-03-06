package com.restbank;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.error.ApiError;
import com.restbank.repository.AccountRepository;
import com.restbank.repository.CreditCardRepository;
import com.restbank.service.AccountService;
import com.restbank.utils.Statics;
import org.junit.Before;
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
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AccountControllerTests {

    private static final String API_1_0_ACCOUNTS = Statics.API_1_0_ACCOUNTS;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Before
    public void cleanUp(){
        accountRepository.deleteAll();
        creditCardRepository.deleteAll();
    }

    @Test
    public void getAccounts_whenThereAreAnyRecordsInDB_receiveOK(){
        ResponseEntity<Object> response = getAccounts(new ParameterizedTypeReference<Object>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getAccounts_whenThereAreAccountsInDB_receiveOK(){
        accountService.create(TestUtil.createValidAccount());
        ResponseEntity<Object> response = getAccounts(new ParameterizedTypeReference<Object>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getAccounts_whenThereAreAccountsInDB_receiveGenericResponseWithAccountData(){
        accountService.create(TestUtil.createValidAccount());
        ResponseEntity<GenericResponse<List<Account>>> response = getAccounts(new ParameterizedTypeReference<GenericResponse<List<Account>>>() {});
        assertThat(response.getBody().getData().get(0)).isInstanceOf(Account.class);
    }

    @Test
    public void getAccounts_whenThereAreAccountsInDB_receiveGenericResponseWithInfo(){
        accountService.create(TestUtil.createValidAccount());
        ResponseEntity<GenericResponse<List<Account>>> response = getAccounts(new ParameterizedTypeReference<GenericResponse<List<Account>>>() {});
        assertThat(response.getBody().getInfo()).isNotNull();
    }

    @Test
    public void getAccounts_whenPageIsRequestedFor3ItemsPerPageWhereTheDatabaseHas20Accounts_receive3Accounts() {
        IntStream.rangeClosed(1,20).mapToObj(i -> (double) i)
                .map(TestUtil::createValidAccount)
                .forEach(accountRepository::save);

        String path = API_1_0_ACCOUNTS + "?page=0&size=3";

        ResponseEntity<GenericResponse<List<Object>>> response = getAccounts(path, new ParameterizedTypeReference<GenericResponse<List<Object>>>() {});
        assertThat(response.getBody().getData().size()).isEqualTo(3);
    }

    @Test
    public void getAccount_whenAccountExists_receiveOK() {
        Account account = accountService.create(TestUtil.createValidAccount());
        ResponseEntity<Object> response = getAccountByAccountNumber(account.getAccountNumber(), Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getAccount_whenAccountNonExists_receiveNotFound() {
        ResponseEntity<Object> response = getAccountByAccountNumber("00000005", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getAccount_whenAccountNonExists_receiveApiError() {
        ResponseEntity<ApiError> response = getAccountByAccountNumber("00000005", ApiError.class);
        assertThat(response.getBody().getMessage()).contains("Account not found");
    }

    @Test
    public void putAccount_whenRequestBodyIsValid_receiveOK() {
        Account account = accountService.create(TestUtil.createValidAccount());
        account.setBalance(new BigDecimal("123.22"));

        HttpEntity<Account> requestEntity = new HttpEntity<>(account);
        ResponseEntity<Object> response = putAccount(account.getAccountNumber(), requestEntity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteAccount_whenCallDeleteMethod_receiveAccountDeletedMessage() {
        Account account = accountService.create(TestUtil.createValidAccount());

        ResponseEntity<GenericResponse> response = deleteUserByAccountNumber(account.getAccountNumber(), GenericResponse.class);
        assertThat(response.getBody().getMessage()).contains("Account deleted.");
    }


    // Put CreditCard /{accountNumber}/credit-card
    @Test
    public void postCreditCard_whenCreditCardValid_receiveOK() {
        Account account = accountRepository.save(TestUtil.createValidAccount());
        CreditCard creditCard = TestUtil.createCreditCard();

        HttpEntity<CreditCard> requestEntity = new HttpEntity<>(creditCard);
        ResponseEntity<Object> response = putCreditCard(account.getAccountNumber(), requestEntity, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void putCreditCard_beforeSavingCreditCard_generateCVC() {
        Account account = accountRepository.save(TestUtil.createValidAccount());
        CreditCard creditCard = TestUtil.createCreditCard();

        HttpEntity<CreditCard> requestEntity = new HttpEntity<>(creditCard);

        putCreditCard(account.getAccountNumber(), requestEntity, Object.class);
        CreditCard creditCardInDB = creditCardRepository.findAll().get(0);

        assertThat(creditCardInDB.getCvc()).isNotNull();
    }

    @Test
    public void putCreditCard_beforeSavingCreditCard_generateExpireDate() {
        Account account = accountRepository.save(TestUtil.createValidAccount());
        CreditCard creditCard = TestUtil.createCreditCard();

        HttpEntity<CreditCard> requestEntity = new HttpEntity<>(creditCard);
        putCreditCard(account.getAccountNumber(), requestEntity, Object.class);

        CreditCard creditCardInDB = creditCardRepository.findAll().get(0);
        assertThat(creditCardInDB.getExpireDate()).isNotNull();
    }

    @Test
    public void putCreditCard_whenBankCodeIsShorterThanNecessary_receiveBadRequest() {
        Account account = accountRepository.save(TestUtil.createValidAccount());
        CreditCard creditCard = TestUtil.createCreditCard();
        creditCard.setBankCode("123");

        HttpEntity<CreditCard> requestEntity = new HttpEntity<>(creditCard);
        ResponseEntity<Object> response = putCreditCard(account.getAccountNumber(), requestEntity, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void putCreditCard_whenBankCodeIsLongerThanNecessary_receiveBadRequest() {
        Account account = accountRepository.save(TestUtil.createValidAccount());
        CreditCard creditCard = TestUtil.createCreditCard();
        creditCard.setBankCode("123123");

        HttpEntity<CreditCard> requestEntity = new HttpEntity<>(creditCard);
        ResponseEntity<Object> response = putCreditCard(account.getAccountNumber(), requestEntity, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void putCreditCard_whenBankCodeNotRequiredTypedData_receiveBadRequest() {
        Account account = accountRepository.save(TestUtil.createValidAccount());
        CreditCard creditCard = TestUtil.createCreditCard();
        creditCard.setBankCode("AWE1");

        HttpEntity<CreditCard> requestEntity = new HttpEntity<>(creditCard);
        ResponseEntity<Object> response = putCreditCard(account.getAccountNumber(), requestEntity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void putCreditCard_whenBankCodeNotRequiredTypedData_receiveApiError() {
        Account account = accountRepository.save(TestUtil.createValidAccount());
        CreditCard creditCard = TestUtil.createCreditCard();
        creditCard.setBankCode("AWE1");

        HttpEntity<CreditCard> requestEntity = new HttpEntity<>(creditCard);
        ResponseEntity<ApiError> response = putCreditCard(account.getAccountNumber(), requestEntity, ApiError.class);
        assertThat(response.getBody().getValidationErrors().get("bankCode")).isEqualTo("Code must be 4 digit number");
    }

    // bankCode and branchCode has same validation props.
    @Test
    public void putCreditCard_whenBranchCodeIsShorterThanNecessary_receiveBadRequest() {
        Account account = accountRepository.save(TestUtil.createValidAccount());
        CreditCard creditCard = TestUtil.createCreditCard();
        creditCard.setBranchCode("123");

        HttpEntity<CreditCard> requestEntity = new HttpEntity<>(creditCard);
        ResponseEntity<Object> response = putCreditCard(account.getAccountNumber(), requestEntity, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // get account credit card
    @Test
    public void getCreditCard_whenThereAreAnyRecordsInDB_receiveOK(){
        Account account = accountService.create(TestUtil.createValidAccount());

        CreditCard creditCard = TestUtil.createCreditCard();
        creditCard.setAccount(account);

        accountService.createCreditCard(account.getAccountNumber(), creditCard);
        ResponseEntity<GenericResponse<CreditCard>> response = getCreditCard(account.getAccountNumber(),
                                                new ParameterizedTypeReference<GenericResponse<CreditCard>>(){});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // TestRestTemplate Functions
    public <T> ResponseEntity<T> postAccount(Object request, Class<T> response){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .postForEntity(API_1_0_ACCOUNTS, request, response);
    }

    public <T> ResponseEntity<T> putCreditCard(String accountNumber, HttpEntity<CreditCard> requestEntity, Class<T> response){
        String path = API_1_0_ACCOUNTS + "/" + accountNumber +"/credit-card";
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(path, HttpMethod.PUT, requestEntity, response);
    }

    public <T> ResponseEntity<T> getCreditCard(String accountNumber, ParameterizedTypeReference<T> responseType){
        String path = API_1_0_ACCOUNTS + "/" + accountNumber +"/credit-card";
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(path, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> getAccounts(ParameterizedTypeReference<T> responseType){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(API_1_0_ACCOUNTS, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> getAccounts(String path, ParameterizedTypeReference<T> responseType){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(path, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> getAccountByAccountNumber(String accountNumber, Class<T> responseType) {
        String path = API_1_0_ACCOUNTS + "/" + accountNumber;
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .getForEntity(path, responseType);
    }

    private <T> ResponseEntity<T> putAccount(String accountNumber, HttpEntity<Account> requestEntity, Class<T> responseType) {
        String path = API_1_0_ACCOUNTS + "/" + accountNumber;
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(path, HttpMethod.PUT, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> deleteUserByAccountNumber(String accountNumber, Class<T> responseType ) {
        String path = API_1_0_ACCOUNTS + "/" + accountNumber;
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(path, HttpMethod.DELETE, null, responseType);
    }

    /*
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
        ResponseEntity<Object> response = postAccount(account, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postAccount_whenAccountNumberHaveMoreThanRequired_receiveBadRequest() {
        Account account = TestUtil.createValidAccount();
        account.setAccountNumber("1236788993412");
        ResponseEntity<Object> response = postAccount(account, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postAccount_whenAccountNumberHaveCharacters_receiveBadRequest() {
        Account account = TestUtil.createValidAccount();
        account.setAccountNumber("123ASs--");

        ResponseEntity<Object> response = postAccount(account, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postAccount_whenBalanceGoesDownZero_receiveBadRequest() {
        Account account = TestUtil.createValidAccount();
        account.setBalance(new BigDecimal("-33.2"));
        ResponseEntity<Object> response = postAccount(account, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postAccount_whenBalanceGoesDownZero_receiveApiError() {
        Account account = TestUtil.createValidAccount();
        account.setBalance(new BigDecimal("-33.21"));
        ResponseEntity<ApiError> response = postAccount(account, ApiError.class);

        assertThat(response.getBody().getValidationErrors().get("balance")).isEqualTo("Insufficient balance");
    }

    @Test
    public void postAccount_whenBalanceEqualToZero_receiveOK() {
        Account account = TestUtil.createValidAccount();
        account.setBalance(new BigDecimal("0.00"));
        ResponseEntity<Object> response = postAccount(account, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postAccount_whenBalanceEqualsOnlyDigits_receiveOK() {
        Account account = TestUtil.createValidAccount();
        account.setBalance(new BigDecimal("0.02"));
        ResponseEntity<GenericResponse> response = postAccount(account, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postAccount_whenBalanceHaveOneDigitAfterPoint_receiveOK() {
        Account account = TestUtil.createValidAccount();
        account.setBalance(new BigDecimal("0.2"));
        ResponseEntity<GenericResponse> response = postAccount(account, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postAccount_whenBalanceHaveMoreDigitThanRequiredAfterPoint_receiveBadRequest() {
        Account account = TestUtil.createValidAccount();
        account.setBalance(new BigDecimal("12.222"));
        ResponseEntity<Object> response = postAccount(account, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postAccount_whenBalanceHaveMoreDigitThanRequired_receiveBalanceFormatError() {
        Account account = TestUtil.createValidAccount();
        account.setBalance(new BigDecimal("32.2323"));
        ResponseEntity<ApiError> response = postAccount(account, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("balance")).isEqualTo("Balance must only two digits after point");
    }

    @Test
    public void postAccount_whenBalanceGoesDownZero_receiveMinValueZeroError() {
        Account account = TestUtil.createValidAccount();
        account.setBalance(new BigDecimal("-33.2"));
        ResponseEntity<ApiError> response = postAccount(account, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("balance")).isEqualTo("Insufficient balance");
    }
*/
}
