package pl.wszeborowski.mateusz.exhibit.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;

public class ExhibitYearValidator implements ConstraintValidator<ExhibitYear, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Long)) {
            return false;
        }
        final int currentYear = Math.toIntExact(Calendar.getInstance().get(Calendar.YEAR));
        return (Long) value < currentYear;
    }
}
