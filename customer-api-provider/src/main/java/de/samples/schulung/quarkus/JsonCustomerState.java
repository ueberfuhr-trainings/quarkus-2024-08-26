package de.samples.schulung.quarkus;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({
  ElementType.FIELD,
  ElementType.METHOD,
  ElementType.PARAMETER,
  ElementType.ANNOTATION_TYPE,
  ElementType.CONSTRUCTOR
})
@Documented
@Constraint(validatedBy = {})
@Pattern(regexp = "active|locked|disabled")
@ReportAsSingleViolation
public @interface JsonCustomerState {

  String message() default "allowed states are only 'active', 'locked', and 'disabled'";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
