package com.restbank;

import static org.assertj.core.api.Assertions.assertThat;

import com.restbank.api.GenericResponse;
import com.restbank.domain.User;
import com.restbank.error.ApiError;
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
import javax.xml.ws.Response;
import java.util.List;
import java.util.Map;

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
        userRepository.deleteByEmail("tmpUser@mail.com");
    }

    @Test
    public void postUser_whenUserIsValid_receiveOK(){
        User user = TestUtil.createValidUser();
        ResponseEntity<Object> responseEntity = postSignup(user, Object.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postUser_whenUserIsValid_userSavedToDatabase(){
        User tmpUser = TestUtil.createValidUser();
        postSignup(tmpUser, Object.class);

        // 1 - admin 2 - user 3- tmpuser
        assertThat(userRepository.count()).isEqualTo(3);
    }

    @Test
    public void postUser_whenUserIsValid_receiveSuccessMessage(){
        User user = TestUtil.createValidUser();
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getBody().getMessage()).isNotNull();
    }

    @Test
    public void postUser_whenUserIsValid_passwordIsHashedInDatabase(){
        User user = TestUtil.createValidUser();

        postSignup(user, Object.class);
        List<User> users = userRepository.findAll();
        User inDb = users.get(0);

        assertThat(inDb.getPassword()).isNotEqualTo(user.getPassword());
    }

    @Test
    public void postUser_whenUserHasFirstNameLessThanRequired_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setFirstName("ab");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasLastNameLessThanRequired_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setLastName("ab");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserHasPasswordLessThanRequired_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPassword("P42sssq");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserPasswordHasAllTheLowercase_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPassword("aaaaaaaaaa");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserPasswordHasAllTheUppercase_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPassword("AAAAAAAAA");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserPasswordHasAllTheDigits_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPassword("123456789");
        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserEmailNotAnEmail_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setEmail("sadfasdfasdf");

        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserPhoneNumberHasLessThanRequiredDigits_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPhoneNumber("1231231");

        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    public void postUser_whenUserPhoneNumberHasMoreThanRequiredDigits_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPhoneNumber("12312312321222");

        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserPhoneNumberNotRequiredTypedData_receiveBadRequest(){
        User user = TestUtil.createValidUser();
        user.setPhoneNumber("ABC123SFe");

        ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postUser_whenUserIsInvalid_receiveApiError() {
        User user = new User();
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_USERS);
    }

    @Test
    public void postUser_whenUserIsInvalid_receiveApiErrorWithValidations() {
        User user = new User();
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        assertThat(response.getBody().getValidationErrors().size()).isEqualTo(5);
    }

    @Test
    public void postUser_whenUserHasNullEmail_receiveMessageOfNullErrorForEmail() {
        User user = TestUtil.createValidUser();
        user.setEmail(null);
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("email")).isEqualTo("Email cannot be null");
    }

    @Test
    public void postUser_whenUSerHasNullPassword_receiveGenericMessageOfNullError() {
        User user = TestUtil.createValidUser();
        user.setPassword(null);
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("password")).isEqualTo("Cannot be null");
    }

    @Test
    public void postUser_whenHasInvalidPasswordPattern_receivePasswordPatternError() {
        User user = TestUtil.createValidUser();
        user.setPassword("123Dsss");
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("password")).isEqualTo("It must have minimum 8 and maximum 255 characters");
    }

    @Test
    public void postUser_whenHasInvalidPhoneNumberPattern_receivePhonePatternError() {
        User user = TestUtil.createValidUser();
        user.setPhoneNumber("123ds1sss");
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("phoneNumber")).isEqualTo("Phone number must have 10 number digits");
    }

    @Test
    public void postUser_whenHasInvalidEmailPattern_receiveEmailPatternError() {
        User user = TestUtil.createValidUser();
        user.setEmail("ahmetkaygisiz.1231");
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("email")).isEqualTo("Email format is incorrect");
    }

    @Test
    public void postUser_whenEmailDuplicated_receiveUniqueEmailError(){
       userRepository.save(TestUtil.createValidUser());

       User user = TestUtil.createValidUser();
       ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
       Map<String, String> validationErrors = response.getBody().getValidationErrors();

       assertThat(validationErrors.get("email")).isEqualTo("This email is in use");
    }

    @Test
    public void postUser_whenPhoneNumberDuplicated_receiveUniquePhoneNumberError(){
        userRepository.save(TestUtil.createValidUser());

        User user = TestUtil.createValidUser();
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("phoneNumber")).isEqualTo("This phone number is in use");
    }

    public <T> ResponseEntity<T> postSignup(Object request, Class<T> response){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .postForEntity(API_1_0_USERS, request, response);
    }

}
