package com.restbank.repository;

import com.restbank.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {


    @Transactional
    User findByEmail(String email);

    @Transactional
    User findByPhoneNumber(String number);

    @Transactional
    void deleteByEmail(String email);
}
