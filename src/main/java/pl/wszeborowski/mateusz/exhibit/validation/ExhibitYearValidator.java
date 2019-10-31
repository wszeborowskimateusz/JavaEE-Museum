package pl.wszeborowski.mateusz.exhibit.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;

public class ExhibitYearValidator implements ConstraintValidator<ExhibitYear, Long> {
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        final int currentYear = Math.toIntExact(Calendar.getInstance().get(Calendar.YEAR));
        return value < currentYear;
    }
}
