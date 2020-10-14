package com.restbank;

import static org.assertj.core.api.Assertions.assertThat;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.error.ApiError;
import com.restbank.repository.RoleRepository;
import com.restbank.repository.UserRepository;
import com.restbank.service.UserService;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTests {

    private static final String API_1_0_USERS = "/api/1.0/users";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Before
    public void cleanUp(){
        userRepository.deleteAllByEmailNotContaining("test@mail.com");
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

        // 1 - admin 2 - tmp user
        assertThat(userRepository.count()).isEqualTo(2);
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
        user.setPassword("123dA");
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
       userService.save(TestUtil.createValidUser());

       User user = TestUtil.createValidUser();
       ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
       Map<String, String> validationErrors = response.getBody().getValidationErrors();

       assertThat(validationErrors.get("email")).isEqualTo("This email is in use");
    }

    @Test
    public void postUser_whenPhoneNumberDuplicated_receiveUniquePhoneNumberError(){
        userService.save(TestUtil.createValidUser());

        User user = TestUtil.createValidUser();
        ResponseEntity<ApiError> response = postSignup(user, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("phoneNumber")).isEqualTo("This phone number is in use");
    }

    @Test
    public void getUsers_whenThereAreUsersInDB_receivePageWithUser() {
        ResponseEntity<Object> response = getUsers(new ParameterizedTypeReference<Object>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUsers_whenThereAreUsersInDB_receiveDataWithoutPassword() {
        ResponseEntity<GenericResponse<List<Map<String, Object>>>> response = getUsers(new ParameterizedTypeReference<GenericResponse<List<Map<String, Object>>>>() {});
        assertThat(response.getBody().getData().get(0).containsKey("password")).isFalse();
    }

    @Test
    public void getUsers_whenPageIsRequestedFor3ItemsPerPageWhereTheDatabaseHas20Users_receive3Users() {
        IntStream.rangeClosed(1, 20).mapToObj(i -> "tmp-test-user" + i)
                .map(TestUtil::createValidUser)
                .forEach(userRepository::save);

        String path = API_1_0_USERS + "?page=0&size=3";

        ResponseEntity<GenericResponse<List<Object>>> response = getUsers(path, new ParameterizedTypeReference<GenericResponse<List<Object>>>() {});
        assertThat(response.getBody().getData().size()).isEqualTo(3);
    }

    @Test
    public void getUsers_whenPageSizeNotProvided_receivePageSizeAs20() {
        IntStream.rangeClosed(1, 20).mapToObj(i -> "tmp-test-user" + i)
                .map(TestUtil::createValidUser)
                .forEach(userRepository::save);

        ResponseEntity<GenericResponse<List<Object>>> response = getUsers(new ParameterizedTypeReference<GenericResponse<List<Object>>>() {});
        assertThat(response.getBody().getData().size()).isEqualTo(20);
    }

    @Test
    public void getUsers_whenPageSizeIsGreaterThan100_receivePageSizeAs100() {
        String path = API_1_0_USERS + "?size=231";

        ResponseEntity<GenericResponse<List<Object>>> response = getUsers(path, new ParameterizedTypeReference<GenericResponse<List<Object>>>() {});
        assertThat(response.getBody().getInfo().getSize()).isEqualTo(100);
    }

    @Test
    public void getUsers_whenPageSizeIsNegative_receivePageSizeAs20() {
        String path = API_1_0_USERS + "?size=-3";

        ResponseEntity<GenericResponse<List<Object>>> response = getUsers(path, new ParameterizedTypeReference<GenericResponse<List<Object>>>() {});
        assertThat(response.getBody().getInfo().getSize()).isEqualTo(20);
    }

    @Test
    public void getUsers_whenPageIsNegative_receiveFirstPage() {
        String path = API_1_0_USERS + "?size=-3";

        ResponseEntity<GenericResponse<List<Object>>> response = getUsers(path, new ParameterizedTypeReference<GenericResponse<List<Object>>>() {});
        assertThat(response.getBody().getInfo().getPage()).isEqualTo(1);
    }

    @Test
    public void getUserById_whenUserExists_receiveOK(){
        ResponseEntity<Object> response = getUserById(1L, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUserById_whenUserExists_receiveUserWithoutPassword() {
        String email = "test@mail.com";
        ResponseEntity<String> response = getUserById(1L, String.class);
        assertThat(response.getBody().contains("password")).isFalse();
    }

    @Test
    public void getUserById_whenUserDoesntExists_receiveNotFound() {
        ResponseEntity<Object> response = getUserById(5L, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getUserById_whenUserDoesntExists_receiveApiError() {
        ResponseEntity<ApiError> response = getUserById(5L, ApiError.class);
        assertThat(response.getBody().getMessage()).contains("User not found with id=" + 5L);
    }

    @Test
    public void putUser_whenRequestBodyIsValid_receiveOK() {
        User user = userService.save(TestUtil.createValidUser());
        User updatedUser = TestUtil.updateValidUser(user);

        HttpEntity<User> requestEntity = new HttpEntity<>(updatedUser);
        ResponseEntity<Object> response = putUser(user.getId(), requestEntity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void putUser_whenRequestBodyNotContainsPassword_notUpdatePassword() {
        User user = userService.save(TestUtil.createValidUser());
        User updatedUser = TestUtil.updateValidUserWithoutPassword(user);
        String password = user.getPassword();

        HttpEntity<User> requestEntity = new HttpEntity<>(updatedUser);
        putUser(user.getId(), requestEntity, Object.class);
        assertThat(updatedUser.getPassword()).isEqualTo(password);
    }

    @Test
    public void putUser_whenUpdatingEmailWithAlreadyExists_receiveBadRequest() {
        User user = userService.save(TestUtil.createValidUser());
        user.setEmail("test@mail.com");

        HttpEntity<User> requestEntity = new HttpEntity<>(user);
        ResponseEntity<Object> response = putUser(user.getId(), requestEntity, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void putUser_whenAddedValidRoleToUser_receiveOK() {
        User user = userService.save(TestUtil.createValidUser());

        String path = API_1_0_USERS + "/" + user.getId() + "/roles?roles=ADMIN&roles=USER";
        ResponseEntity<Object> response = testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(path, HttpMethod.PUT, null, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


    public <T> ResponseEntity<T> putUser(Long id, HttpEntity<?> requestEntity, Class<T> responseType){
        String path = API_1_0_USERS + "/" + id;
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(path, HttpMethod.PUT, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> postSignup(Object request, Class<T> response){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .postForEntity(API_1_0_USERS, request, response);
    }

    public <T> ResponseEntity<T> getUsers(ParameterizedTypeReference<T> responseType){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(API_1_0_USERS, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> getUsers(String path, ParameterizedTypeReference<T> responseType){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .exchange(path, HttpMethod.GET, null, responseType);
    }


    public <T> ResponseEntity<T> getUserById(Long id, Class<T> responseType) {
        String path = API_1_0_USERS + "/" + id;
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .getForEntity(path, responseType);
    }
}
