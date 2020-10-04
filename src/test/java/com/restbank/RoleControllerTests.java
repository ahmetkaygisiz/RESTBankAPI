package com.restbank;

import com.restbank.domain.Role;
import com.restbank.error.ApiError;
import com.restbank.repository.RoleRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RoleControllerTests {

    private static final String API_1_0_ROLES = "/api/1.0/roles";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    RoleRepository roleRepository;

    @Before
    public void cleanUp(){
        roleRepository.deleteByName("TEST");
    }

    @Test
    public void postRole_whenUserRoleIsValid_receiveOK() {
        ResponseEntity<Object> response = postRole(TestUtil.createRole("TEST"), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postRole_whenRoleAlreadyExists_receiveBadRequest() {
        ResponseEntity<ApiError> response = postRole(TestUtil.createRole("ADMIN"), ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();

        assertThat(validationErrors.get("name")).isEqualTo("Role name already exists");
    }

    // role name kucuk girildiginde uppercase olarak kaydet.
    @Test
    public void postRole_whenRoleNameSavedLowercase_changeToUpperCase() {
        postRole(TestUtil.createRole("test"), Object.class);
        Role r = roleRepository.findByName("TEST");
        assertThat(r).isNotNull();
    }


    public <T> ResponseEntity<T> postRole(Object request, Class<T> response){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .postForEntity(API_1_0_ROLES, request, response);
    }
    
    public <T> ResponseEntity<T> getRoles(Object request, Class<T> response){
        return testRestTemplate.withBasicAuth("test@mail.com","P4ssword")
                .getForEntity(API_1_0_ROLES, response);
    }
}
