package com.restbank;

import com.restbank.domain.User;
import com.restbank.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    public void findByEmail_whenUserExists_returnUser() {
        testEntityManager.persist(TestUtil.createValidUser());
        User inDB = userRepository.findByEmail("ahmetkaygisiz@gmail.com");

        Assertions.assertThat(inDB).isNotNull();
    }

    @Test
    public void findByEmail_whenUserNotExists_returnNull(){
        User inDB = userRepository.findByEmail("non-exists-user@email.com");
        Assertions.assertThat(inDB).isNull();
    }

}
