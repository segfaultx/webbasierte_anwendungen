package application.sichtung;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * validates a field containing the number 17 in numeric or written form
 */
public class SiebzehnhaftValidator implements ConstraintValidator<Siebzehnhaft, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return value.matches("^.*(?i)(17|siebzehn).*$");
    }
}
