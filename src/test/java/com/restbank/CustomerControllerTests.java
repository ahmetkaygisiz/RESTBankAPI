package com.restbank;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Account;
import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.domain.UserRole;
import com.restbank.domain.dto.UserVM;
import com.restbank.repository.UserRepository;
import com.restbank.service.CustomerService;
import com.restbank.service.UserService;
import com.restbank.utils.Statics;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomerControllerTests {

    private static final String API_1_0_CUSTOMERS = Statics.API_1_0_CUSTOMERS;

    @Autowired
    CustomerService customerService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Before
    public void cleanUp(){
        userRepository.deleteAllByEmailNotContaining("test@mail.com");
    }

    // Tests
    @Test
    public void getCustomer_whenUserRecordInDB_receiveOK(){
        createValidTestUser();
        ResponseEntity<Object> response = getUserWithPrincipal(new ParameterizedTypeReference<Object>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getCustomerWithPrincipal_whenUserRecordInDB_receiveOK(){
        User user = createValidTestUser();
        ResponseEntity<GenericResponse<UserVM>> response = getUserWithPrincipal(new ParameterizedTypeReference<GenericResponse<UserVM>>() {});

        assertThat(response.getBody().getData().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void putCustomerWithPrincipal_whenRequestBodyIsValid_receiveOK() {
        createValidTestUser();
        User requestBody = new User();
        requestBody.setPassword("Qwerty123");
        requestBody.setLastName("No-name");

        HttpEntity<User> requestEntity = new HttpEntity<>(requestBody);
        ResponseEntity<Object> response = putUser(requestEntity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // GET Account List
    // GET customer/AccountId/CreditCard
    // Transactions
    // Get Transactions

    // TestRestTemplate Functions - REST CALLS
    public <T>ResponseEntity<T> getUserWithPrincipal(ParameterizedTypeReference<T> responseType){
        return testRestTemplate.withBasicAuth("tmpUser@mail.com","P4ssword")
                .exchange(API_1_0_CUSTOMERS, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> putUser(HttpEntity<?> requestEntity, Class<T> responseType){
        return testRestTemplate.withBasicAuth("tmpUser@mail.com","P4ssword")
                .exchange(API_1_0_CUSTOMERS, HttpMethod.PUT, requestEntity, responseType);
    }

    // UTIL
    private User createValidTestUser(){
        User user1 = TestUtil.createValidUser();

        Set<UserRole> userRoles = new HashSet<>();
        Role role2 = new Role();
        role2.setName("USER");

        userRoles.add(new UserRole(user1, role2));

        User userInDB = userService.create(user1, userRoles);

//        Account account = new Account();
//        account.setBalance(new BigDecimal("123.22"));
//
//        userService.addUserAccount(userInDB.getId(), account);

        return userInDB;
    }
}
