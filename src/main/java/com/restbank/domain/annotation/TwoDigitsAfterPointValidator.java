package com.restbank.domain.annotation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

@Slf4j
public class TwoDigitsAfterPointValidator implements ConstraintValidator<TwoDigitsAfterPoint, BigDecimal> {
   public void initialize(TwoDigitsAfterPoint constraint) {
   }

   public boolean isValid(BigDecimal obj, ConstraintValidatorContext context) {
      String decimalValue = obj.subtract(new BigDecimal(obj.intValue())).toString(); // Deger 123.23 ise 0.23 seklinde alıyoruz.
      decimalValue = decimalValue.split("\\.")[1]; // Noktan sonrasını alıyoruz.

      if(decimalValue.length() <= 2) // Noktadan sonra 2'den kücük ve esitse valid.
         return true;
      return false;
   }
}
