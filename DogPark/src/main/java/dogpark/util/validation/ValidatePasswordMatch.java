package dogpark.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface ValidatePasswordMatch {
    String password();

    String confirmPassword();
    String message() default "Passwords miss match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
