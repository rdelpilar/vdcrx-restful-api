package com.vdcrx.rest.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Valid email annotation
 *
 * @author Ranel del Pilar
 */

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ PARAMETER, METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidEmail {
    String message() default "Invalid email address!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
