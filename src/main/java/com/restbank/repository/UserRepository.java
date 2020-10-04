package com.restbank.repository;

import com.restbank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    User findByEmail(String email);

    @Transactional
    User findByPhoneNumber(String number);

    @Transactional
    void deleteByEmail(String email);
}
