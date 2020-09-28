package com.restbank.domain.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinValueZeroValidator  implements ConstraintValidator<MinValueZero, Double> {
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        // check balance valid
        // must have 2 digit after point.
        if( value >= 0.0)
            return true;
        return false;
    }
}
