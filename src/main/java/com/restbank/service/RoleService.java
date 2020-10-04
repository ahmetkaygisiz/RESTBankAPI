package com.restbank.service;

import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public void saveRole(Role role){
        role.setName( role.getName().toUpperCase() );
        roleRepository.save(role);
    }

    public void deleteRole(Role role){
        roleRepository.delete(role);
    }

    public List<Role> getRoles(){
        return roleRepository.findAll();
    }

    public Role getRoleByName(String value) {
        return roleRepository.findByName(value);
    }
}
