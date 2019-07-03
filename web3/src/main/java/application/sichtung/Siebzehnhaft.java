package application.sichtung;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * validation interface
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SiebzehnhaftValidator.class)
@Documented
public @interface Siebzehnhaft {
    String message() default "Sichtung muss Siebzehnhaft sein";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
}
