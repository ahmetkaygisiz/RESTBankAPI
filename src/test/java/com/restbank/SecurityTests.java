package com.restbank;

import com.restbank.domain.User;
import com.restbank.domain.UserRole;
import com.restbank.error.ApiError;
import com.restbank.repository.RoleRepository;
import com.restbank.service.UserService;
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

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SecurityTests {

    private String API_1_0_LOGIN = Statics.API_1_0_LOGIN;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Before
    public void cleanup(){

    }

    @Test
    public void postLogin_withoutUserCredentials_receiveUnauthorized() {
        ResponseEntity<Object> response = requestWithoutAuth(Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postLogin_withInvalidCredential_receiveUnauthorized() {
        ResponseEntity<Object> response = requestWithInvalidAuth(Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postLogin_withoutUserCredentials_receiveApiError() {
        ResponseEntity<ApiError> response = requestWithoutAuth(ApiError.class);
        assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_LOGIN);
    }

    // admin role'u ile giris yap.
    @Test
    public void postLogin_withValidCredentials_receiveOk() {
        ResponseEntity<Object> response = requestWithAdmin(Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // user role'u ile admin sayfasini cagir 403 al
    @Test
    public void postLogin_whenUserRequestAdminPages_receiveForbidden(){
        ResponseEntity<Object> response = testRestTemplate.withBasicAuth("user@mail.com","P4ssword")
                .getForEntity(Statics.API_1_0_USERS,null, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    // user role'u ile user sayfalarını cagir.
    @Test
    public void postLogin_whenUserRequestUserPages_receiveOK() {
        ResponseEntity<Object> response = requestWithUser(Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // admin role'u ile admin sayfalarini cagir.
    @Test
    public void getUsers_whenAdminRequestAdminPages_receiveOK() {
        ResponseEntity<Object> response = requestWithAdmin(Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // TestRestTemplate Methods

    public <T> ResponseEntity<T> requestWithoutAuth(Class<T> response){
        return testRestTemplate.postForEntity(API_1_0_LOGIN, null, response);
    }

    public <T> ResponseEntity<T> requestWithAdmin(Class<T> response){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                                .postForEntity(API_1_0_LOGIN, null, response);
    }

    public <T> ResponseEntity<T> requestWithUser(Class<T> response){
        return testRestTemplate.withBasicAuth("user@mail.com","P4ssword")
                .postForEntity(API_1_0_LOGIN, null, response);
    }

    public <T> ResponseEntity<T> requestWithInvalidAuth(Class<T> response){
        return testRestTemplate.withBasicAuth("test@mail.com","asdqwesa")
                            .postForEntity(API_1_0_LOGIN, null, response);
    }
}
