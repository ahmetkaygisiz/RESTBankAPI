package com.restbank;

import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.error.ApiError;
import com.restbank.repository.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
        roleRepository.deleteAll();
    }

    @Test
    public void postRole_whenUserRoleIsValid_receiveOK() {
        roleRepository.save(TestUtil.createValidRole("USER_ADMIN"));

        Role role = TestUtil.createValidRole("USER_ADMIN");
        Assertions.assertThat(role);
    }

    public <T> ResponseEntity<T> postSignup(Object request, Class<T> response){
        return testRestTemplate.postForEntity(API_1_0_ROLES, request, response);
    }
}
