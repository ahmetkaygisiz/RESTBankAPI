package com.restbank.domain.annotation;

import com.restbank.domain.User;
import com.restbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        User inDB = userRepository.findByEmail(email);

        if( inDB == null)
            return true;
        return false;
    }

}
