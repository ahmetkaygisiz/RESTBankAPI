package com.restbank.repository;

import com.restbank.domain.User;
import com.restbank.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Transactional
    void deleteAllByUserIdNotContaining(Long id);

    @Transactional
    void deleteAllByUser(User user);
}
