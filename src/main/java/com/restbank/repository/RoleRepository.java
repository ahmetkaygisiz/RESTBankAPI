package com.restbank.repository;

import com.restbank.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Transactional
    Role findByName(String name);

    @Transactional
    void deleteByName(String name);
}
