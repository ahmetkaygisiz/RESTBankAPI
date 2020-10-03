package com.restbank.domain.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueRoleNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueRoleName {
    String message() default "{restbankapi.constraints.role.UniqueRoleName.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}



