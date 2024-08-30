package de.samples.schulung.quarkus.shared;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

  private Adult adult;

  @Override
  public void initialize(Adult constraintAnnotation) {
    this.adult = constraintAnnotation;
  }

  @Override
  public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
    if (null == value) {
      return true;
    }
    var difference = adult.unit().between(value, LocalDate.now());
    return difference >= adult.value();
  }
}
