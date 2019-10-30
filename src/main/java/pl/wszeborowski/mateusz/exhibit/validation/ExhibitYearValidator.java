package pl.wszeborowski.mateusz.exhibit.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;

public class ExhibitYearValidator implements ConstraintValidator<ExhibitYear, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return value < currentYear;
    }
}
