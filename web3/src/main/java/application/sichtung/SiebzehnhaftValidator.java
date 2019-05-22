package application.sichtung;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SiebzehnhaftValidator implements ConstraintValidator<Siebzehnhaft, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("(?i)(17|siebzehn)");
    }
}
