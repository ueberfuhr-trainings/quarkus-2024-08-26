package de.samples.schulung.quarkus;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.time.temporal.ChronoUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({
  ElementType.FIELD,
  ElementType.METHOD,
  ElementType.PARAMETER,
  ElementType.ANNOTATION_TYPE,
  ElementType.CONSTRUCTOR
})
@Documented
@Constraint(validatedBy = AdultValidator.class)
public @interface Adult {

  int value() default 18;

  ChronoUnit unit() default ChronoUnit.YEARS;

  String message() default "adults must have a birthdate before {value} {unit}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
