package com.restbank;

import static org.assertj.core.api.Assertions.assertThat;

import com.restbank.api.GenericResponse;
import com.restbank.domain.User;
import com.restbank.repository.UserRepository;
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

import javax.persistence.GeneratedValue;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTests {

    private static final String API_1_0_USERS = "/api/1.0/users";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @Before
    public void cleanUp(){
        userRepository.deleteAll();
    }

    @Test
    public void postUser_whenUserIsValid_receiveOK(){
        User user = createValidUser();
        ResponseEntity<Object> responseEntity = postSignup(user, Object.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postUser_whenUserIsValid_userSavedToDatabase(){
        User user = createValidUser();
        postSignup(user, Object.class);

        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void postUser_whenUserIsValid_receiveSuccessMessage(){
        User user = createValidUser();
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getBody().getMessage()).isNotNull();
    }

    @Test
    public void postUser_whenUserIsValid_passwordIsHashedInDatabase(){
        User user = createValidUser();

        postSignup(user, Object.class);
        List<User> users = userRepository.findAll();
        User inDb = users.get(0);

        assertThat(inDb.getPassword()).isNotEqualTo(user.getPassword());
    }

    @Test
    public void postUser_whenUserHasfirstNameLessThanRequired_receiveBadRequest(){
        User user = createValidUser();
        user.setFirstName("ab");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasLastNameLessThanRequired_receiveBadRequest(){
        User user = createValidUser();
        user.setLastName("ab");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordLessThanRequired_receiveBadRequest(){
        User user = createValidUser();
        user.setPassword("P42sssq");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserPasswordHasAllTheLowercase_receiveBadRequest(){
        User user = createValidUser();
        user.setPassword("aaaaaaaaaa");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserPasswordHasAllTheUppercase_receiveBadRequest(){
        User user = createValidUser();
        user.setPassword("AAAAAAAAA");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserPasswordHasAllTheDigits_receiveBadRequest(){
        User user = createValidUser();
        user.setPassword("123456789");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserEmailNotAnEmail_receiveBadRequest(){
        User user = createValidUser();
        user.setEmail("sadfasdfasdf");

        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserPhoneNumberHasLessThanRequiredDigits_receiveBadRequest(){
        User user = createValidUser();
        user.setPhoneNumber("1231231");

        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    public void postUser_whenUserPhoneNumberHasMoreThanRequiredDigits_receiveBadRequest(){
        User user = createValidUser();
        user.setPhoneNumber("12312312321222");

        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserPhoneNumberNotRequiredTypedData_receiveBadRequest(){
        User user = createValidUser();
        user.setPhoneNumber("ABC123SFe");

        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    public <T> ResponseEntity<T> postSignup(Object request, Class<T> response){
        return testRestTemplate.postForEntity(API_1_0_USERS, request, response);
    }

    private User createValidUser(){
        User user = new User();

        user.setFirstName("Ahmet");
        user.setLastName("TestUser");
        user.setEmail("ahmetkaygisiz@gmail.com");
        user.setPassword("P4ssw4rDd");
        user.setPhoneNumber("5066758941");
        user.setActive(true);

        //user.setRoles();
        //user.setAccountList();

        return user;
    }
}
