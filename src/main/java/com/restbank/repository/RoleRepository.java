package com.restbank.repository;

import com.restbank.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Transactional
    Role findByName(String name);

    @Transactional
    void deleteByName(String name);
}
