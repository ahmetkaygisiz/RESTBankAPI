package com.restbank.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restbank.TestUtil;
import com.restbank.domain.*;
import com.restbank.repository.AccountRepository;
import com.restbank.repository.CreditCardRepository;
import com.restbank.repository.TransactionRepository;
import com.restbank.repository.UserRepository;
import com.restbank.service.AccountService;
import com.restbank.service.CreditCardService;
import com.restbank.service.TransactionService;
import com.restbank.service.UserService;
import com.restbank.utils.Statics;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocsTests {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("docs/output");

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    // Endpoints
    private String API_1_0_USERS = Statics.API_1_0_USERS;
    private String API_1_0_ACCOUNTS = Statics.API_1_0_ACCOUNTS;
    private String API_1_0_ROLES = Statics.API_1_0_ROLES;
    private String API_1_0_CREDIT_CARDS = Statics.API_1_0_CREDIT_CARDS;
    private String API_1_0_TRANSACTIONS = Statics.API_1_0_TRANSACTIONS;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Before
    public void setUp(){
        userRepository.deleteAllByEmailNotContaining("test@mail.com");
        accountRepository.deleteAll();
        creditCardRepository.deleteAll();
        transactionRepository.deleteAll();;

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void getUserById() throws Exception {
        this.mockMvc.perform(get(API_1_0_USERS + "/1")
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("user/get-user-by-id"));
    }

    @Test
    public void getUsers() throws Exception {
        this.mockMvc.perform(get(API_1_0_USERS)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("user/get-users"));
    }

    @Test
    public void postUser() throws Exception {
        User user = TestUtil.createValidUser();
        String body = (new ObjectMapper()).valueToTree(user).toString();

        this.mockMvc.perform(post(API_1_0_USERS)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("user/post-user"));
    }

    @Test
    public void putUser() throws Exception {
        User user = userService.create(TestUtil.createValidUser());
        user.setLastName("frewtsdfg");
        user.setPassword("P4ssword");

        String path = API_1_0_USERS + "/" + user.getId();
        String body = (new ObjectMapper()).valueToTree(user).toString();

        this.mockMvc.perform(
                    put(path)
                    .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("user/put-user"));
    }

    @Test
    public void deleteUser() throws Exception {
        User user = userService.create(TestUtil.createValidUser());

        String path = API_1_0_USERS + "/" + user.getId();
        this.mockMvc.perform(delete(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("user/delete-user"));
    }

    @Test
    public void updateUserRoles() throws Exception {
        User user = userService.create(TestUtil.createValidUser());

        String path = API_1_0_USERS + "/" + user.getId() + "/roles?roles=USER";
        this.mockMvc.perform(put(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("user/update-user-roles"));
    }

    @Test
    public void addUserAccounts() throws Exception {
        User user = userService.create(TestUtil.createValidUser());
        Account account = TestUtil.createValidAccount();

        String path = API_1_0_USERS + "/" + user.getId() + "/accounts";
        String body = (new ObjectMapper()).valueToTree(account).toString();

        this.mockMvc.perform(put(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("user/put-user-accounts"));
    }

    @Test
    public void getUserAccounts() throws Exception {
        User user = userService.create(TestUtil.createValidUser());

        String path = API_1_0_USERS + "/" + user.getId() + "/accounts";
        this.mockMvc.perform(get(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("user/get-user-accounts"));
    }

    // ACCOUNT
    @Test
    public void getAccounts() throws Exception {
        this.mockMvc.perform(get(API_1_0_ACCOUNTS)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("account/get-accounts"));
    }

    @Test
    public void getAccountWithAccountNumber() throws Exception {
        Account account = accountService.create(TestUtil.createValidAccount());

        String path = API_1_0_ACCOUNTS + "/" + account.getAccountNumber();
        this.mockMvc.perform(get(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("account/get-account-by-account-number"));
    }

    @Test
    public void putAccount() throws Exception {
        Account account = accountService.create(TestUtil.createValidAccount());
        account.setBalance(new BigDecimal("3423.34"));

        String path = API_1_0_ACCOUNTS + "/" + account.getAccountNumber();
        String body = (new ObjectMapper()).valueToTree(account).toString();

        this.mockMvc.perform(put(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("account/put-account"));
    }

    @Test
    public void deleteAccount() throws Exception {
        Account account = accountService.create(TestUtil.createValidAccount());

        String path = API_1_0_ACCOUNTS + "/" + account.getAccountNumber();
        this.mockMvc.perform(delete(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("user/delete-user"));
    }

    @Test
    public void putAccountCreditCard() throws Exception {
        Account account = accountService.create(TestUtil.createValidAccount());
        CreditCard creditCard = TestUtil.createCreditCard();

        String path = API_1_0_ACCOUNTS + "/" + account.getAccountNumber() + "/credit-card";
        //String body = (new ObjectMapper()).valueToTree(creditCard).toString(); -> maxLimit 5E+2 seklinde serialize oldu
        String body = new ObjectMapper().writeValueAsString(creditCard);

        this.mockMvc.perform(put(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("account/put-account-credit-card"));
    }

    @Test
    public void getAccountCreditCard() throws Exception {
        Account account = accountService.create(TestUtil.createValidAccount());

        String path = API_1_0_ACCOUNTS + "/" + account.getAccountNumber() + "/credit-card";
        this.mockMvc.perform(get(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("account/get-account-credit-card"));
    }

    @Test
    public void postRole() throws Exception {
        Role role = TestUtil.createRole("TMP");
        String body = (new ObjectMapper()).valueToTree(role).toString();

        this.mockMvc.perform(post(API_1_0_ROLES)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("role/post-role"));
    }

    @Test
    public void getCreditCards() throws Exception {
        creditCardService.createCreditCard(TestUtil.createCreditCard());

        this.mockMvc.perform(get(API_1_0_CREDIT_CARDS)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("credit-card/get-credit-cards"));
    }

    @Test
    public void putCreditCard() throws Exception {
        CreditCard creditCard = creditCardService.createCreditCard(TestUtil.createCreditCard());
        creditCard.setExpireDate(null); // LocalDate serializing problem
        creditCard.setMaxLimit(new BigDecimal("1000.00"));

        String path = API_1_0_CREDIT_CARDS + "/" + creditCard.getId();
        String body = new ObjectMapper().writeValueAsString(creditCard);

        this.mockMvc.perform(
                put(path)
                        .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("credit-card/put-credit-card"));
    }

    @Test
    public void deleteCreditCard() throws Exception {
        CreditCard creditCard = creditCardService.createCreditCard(TestUtil.createCreditCard());

        String path = API_1_0_CREDIT_CARDS + "/" + creditCard.getId();

        this.mockMvc.perform(delete(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("credit-card/delete-credit-card"));
    }

    @Test
    public void postTransaction_account2account() throws Exception{
        Account fromAccount = accountRepository.save(TestUtil.createValidAccount());
        Account toAccount = accountRepository.save(TestUtil.createValidAccount());
        Transaction transaction = TestUtil.
                createTransaction(fromAccount.getAccountNumber(),
                        toAccount.getAccountNumber());

        String path = API_1_0_TRANSACTIONS + "/account2account" ;
        String body = new ObjectMapper().writeValueAsString(transaction);

        this.mockMvc.perform(post(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("transaction/account2account"));
    }

    @Test
    public void postTransaction_account2creditcard() throws Exception{
        Account fromAccount = accountRepository.save(TestUtil.createValidAccount());
        CreditCard toCreditCard = creditCardRepository.save(TestUtil.createCreditCard());
        Transaction transaction = TestUtil.
                createTransaction(fromAccount.getAccountNumber(),
                        toCreditCard.getCreditCardNumber());

        String path = API_1_0_TRANSACTIONS + "/account2creditcard" ;
        String body = new ObjectMapper().writeValueAsString(transaction);

        this.mockMvc.perform(post(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("transaction/account2creditcard"));
    }

    @Test
    public void postTransaction_creditcard2account() throws Exception{
        CreditCard fromCreditCard = creditCardRepository.save(TestUtil.createCreditCard());
        Account toAccount = accountRepository.save(TestUtil.createValidAccount());
        Transaction transaction = TestUtil.
                createTransaction(fromCreditCard.getCreditCardNumber(),
                        toAccount.getAccountNumber());

        String path = API_1_0_TRANSACTIONS + "/creditcard2account" ;
        String body = new ObjectMapper().writeValueAsString(transaction);

        this.mockMvc.perform(post(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("transaction/creditcard2account"));
    }

    @Test
    public void getTransactions() throws Exception {
        CreditCard fromCreditCard = creditCardRepository.save(TestUtil.createCreditCard());
        Account toAccount = accountRepository.save(TestUtil.createValidAccount());
        Transaction transaction = transactionRepository.save(TestUtil.
                createTransaction(fromCreditCard.getCreditCardNumber(),
                        toAccount.getAccountNumber()));

        this.mockMvc.perform(get(API_1_0_TRANSACTIONS)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("transaction/get-transactions"));
    }


    @Test
    public void getTransactionById() throws Exception {
        CreditCard fromCreditCard = creditCardRepository.save(TestUtil.createCreditCard());
        Account toAccount = accountRepository.save(TestUtil.createValidAccount());
        Transaction transaction = transactionRepository.save(TestUtil.
                createTransaction(fromCreditCard.getCreditCardNumber(),
                        toAccount.getAccountNumber()));

        String path = API_1_0_TRANSACTIONS + "/"+ transaction.getId();
        this.mockMvc.perform(get(path)
                .with(user("test@mail.com").password("P4ssword").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("transaction/get-transaction-by-id"));
    }
}
