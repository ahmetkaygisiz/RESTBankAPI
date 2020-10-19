package com.restbank.repository;

import com.restbank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    User getById(Long id);

    @Transactional
    User findByEmail(String email);

    @Transactional
    User findByPhoneNumber(String number);

    @Transactional
    void deleteByEmail(String email);

    @Transactional
    void deleteAllByEmailNotContaining(String email);

}
