package pl.wszeborowski.mateusz.exhibit.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExhibitYearValidator.class)
@Documented
public @interface ExhibitYear {

    String message() default "an exhibit must have a valid date from the past";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
