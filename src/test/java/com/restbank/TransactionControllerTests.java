package com.restbank;

import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.domain.Transaction;
import com.restbank.error.ApiError;
import com.restbank.repository.AccountRepository;
import com.restbank.repository.CreditCardRepository;
import com.restbank.repository.TransactionRepository;
import com.restbank.service.CreditCardService;
import com.restbank.utils.Statics;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransactionControllerTests {

    private static final String API_1_0_TRANSACTIONS    = Statics.API_1_0_TRANSACTIONS;
    private static final String BETWEEN_ACCOUNTS        = API_1_0_TRANSACTIONS + "/account2account";
    private static final String ACCOUNT_TO_CARD         = API_1_0_TRANSACTIONS + "/account2creditcard";
    private static final String CARD_TO_ACCOUNT         = API_1_0_TRANSACTIONS + "/creditcard2account";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Before
    public void cleanUp(){
        transactionRepository.deleteAll();
    }

    @Test
    public void postTransaction_whenTransactionValid_receiveOK() {
        Account fromAccount = accountRepository.save(TestUtil.createValidAccount());
        Account toAccount = accountRepository.save(TestUtil.createValidAccount());
        Transaction transaction = transactionRepository.save(TestUtil.
                                    createTransaction(fromAccount.getAccountNumber(),
                                                        toAccount.getAccountNumber()));

        ResponseEntity<Object> response = postTransaction(BETWEEN_ACCOUNTS, transaction, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // It might be silly but I wanted to see with my eyes - GO DEBUG POWER
    @Test
    public void postTransaction_whenTransactionValid_checkFromAccountBalance() {
        Account fromAccount = accountRepository.save(TestUtil.createValidAccount());
        Account toAccount = accountRepository.save(TestUtil.createValidAccount());
        Transaction transaction = TestUtil.createTransaction(fromAccount.getAccountNumber(),
                                                            toAccount.getAccountNumber());

        postTransaction(BETWEEN_ACCOUNTS, transaction, Object.class);

        Account afterTransaction = accountRepository.findByAccountNumber(fromAccount.getAccountNumber()).get();

        assertThat(afterTransaction.getBalance())
                .isEqualByComparingTo(fromAccount.getBalance().subtract(transaction.getAmount()));
    }

    // Check from account transfer amount.
    @Test
    public void postTransaction_whenFromAccountAmountSmallerThatTransactionAmount_receiveBadRequest() {
        Account fromAccount = accountRepository.save(TestUtil.createValidAccount());
        Account toAccount = accountRepository.save(TestUtil.createValidAccount());

        Transaction transaction = TestUtil.createTransaction(fromAccount.getAccountNumber(), toAccount.getAccountNumber());
        transaction.setAmount(new BigDecimal("123123123.22"));

        ResponseEntity<Object> response = postTransaction(BETWEEN_ACCOUNTS, transaction, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postTransaction_whenFromAccountAmountSmallerThatTransactionAmount_receiveApiError() {
        Account fromAccount = accountRepository.save(TestUtil.createValidAccount());
        Account toAccount = accountRepository.save(TestUtil.createValidAccount());

        Transaction transaction = TestUtil.createTransaction(fromAccount.getAccountNumber(), toAccount.getAccountNumber());
        transaction.setAmount(new BigDecimal("123123123.22"));

        ResponseEntity<ApiError> response = postTransaction(BETWEEN_ACCOUNTS, transaction, ApiError.class);
        assertThat(response.getBody().getMessage()).isEqualTo("Insufficient balance/limit");
    }

    // From account number and to account number cannot be same
    @Test
    public void postTransaction_whenFromAndToAccountsSame_receiveBadRequest() {
        Account fromAccount = accountRepository.save(TestUtil.createValidAccount());

        Transaction transaction = TestUtil.createTransaction(fromAccount.getAccountNumber(), fromAccount.getAccountNumber());
        ResponseEntity<Object> response = postTransaction(BETWEEN_ACCOUNTS, transaction, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // Control Account Numbers
    @Test
    public void postTransaction_whenFromAccountNumbersNotFound_receiveNotFound() {
        Account toAccount = accountRepository.save(TestUtil.createValidAccount());

        Transaction transaction = TestUtil.createTransaction("1231231231", toAccount.getAccountNumber());
        ResponseEntity<Object> response = postTransaction(BETWEEN_ACCOUNTS, transaction, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void postTransaction_whenToAccountNumbersNotFound_receiveNotFound() {
        Account fromAccount = accountRepository.save(TestUtil.createValidAccount());

        Transaction transaction = TestUtil.createTransaction(fromAccount.getAccountNumber(), "1231231231");
        ResponseEntity<Object> response = postTransaction(BETWEEN_ACCOUNTS, transaction, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void postTransaction_whenAccount2CreditCardValid_receiveOK() {
        Account fromAccount = accountRepository.save(TestUtil.createValidAccount());
        CreditCard toCard = creditCardRepository.save(TestUtil.createCreditCard());

        Transaction transaction = TestUtil.createTransaction(fromAccount.getAccountNumber(), toCard.getCreditCardNumber());
        transaction.setAmount(new BigDecimal("250.22"));

        ResponseEntity<Object> response = postTransaction(ACCOUNT_TO_CARD, transaction, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postTransaction_whenCreditCard2AccountValid_receiveOK() {
        CreditCard fromCard = creditCardRepository.save(TestUtil.createCreditCard());
        Account toAccount = accountRepository.save(TestUtil.createValidAccount());

        Transaction transaction = TestUtil.createTransaction(fromCard.getCreditCardNumber(), toAccount.getAccountNumber());
        transaction.setAmount(new BigDecimal("250.22"));

        ResponseEntity<Object> response = postTransaction(CARD_TO_ACCOUNT, transaction, Object.class);

        CreditCard cardInDB = creditCardService.getCreditCardById(fromCard.getId());
        Account accountInDB = accountRepository.findByAccountNumber(toAccount.getAccountNumber()).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postTransaction_whenTheCreditCardLimitIsExceeded_receiveBadRequest() {
        CreditCard fromCard = creditCardRepository.save(TestUtil.createCreditCard());
        Account toAccount = accountRepository.save(TestUtil.createValidAccount());

        Transaction transaction = TestUtil.createTransaction(fromCard.getCreditCardNumber(), toAccount.getAccountNumber());
        transaction.setAmount(new BigDecimal("500.11"));

        ResponseEntity<Object> response = postTransaction(CARD_TO_ACCOUNT, transaction, Object.class);

        CreditCard cardInDB = creditCardService.getCreditCardById(fromCard.getId());
        Account accountInDB = accountRepository.findByAccountNumber(toAccount.getAccountNumber()).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // TestRestTemplate Functions
    public <T> ResponseEntity<T> postTransaction(String path, Object request, Class<T> response){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .postForEntity(path, request, response);
    }
}
