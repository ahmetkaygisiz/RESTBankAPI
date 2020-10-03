package com.restbank.domain.annotation;

import com.restbank.domain.Role;
import com.restbank.repository.RoleRepository;
import com.restbank.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueRoleNameValidator implements ConstraintValidator<UniqueRoleName, String> {

    @Autowired
    RoleService roleService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Role role = roleService.getRoleByName(value);

        if( role == null)
            return true;
        return false;
    }
}
