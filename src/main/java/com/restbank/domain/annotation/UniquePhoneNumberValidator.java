package com.restbank.domain.annotation;

import com.restbank.domain.User;
import com.restbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        User inDB = userRepository.findByPhoneNumber(phoneNumber);

        if( inDB == null)
            return true;
        return false;
    }
}
