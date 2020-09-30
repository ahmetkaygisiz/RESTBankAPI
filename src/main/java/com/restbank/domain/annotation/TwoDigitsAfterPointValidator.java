package com.restbank.domain.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class TwoDigitsAfterPointValidator implements ConstraintValidator<TwoDigitsAfterPoint, BigDecimal> {
   public void initialize(TwoDigitsAfterPoint constraint) {
   }

   public boolean isValid(BigDecimal obj, ConstraintValidatorContext context) {
      String decimalValue = obj.subtract(new BigDecimal(obj.intValue())).toString();

      if(decimalValue.length() <= 4 ) // 0.xx -> 4 char
         return true;
      return false;
   }
}
