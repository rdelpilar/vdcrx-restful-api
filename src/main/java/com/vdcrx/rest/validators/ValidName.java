package com.vdcrx.rest.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Valid name annotation
 *
 * @author Ranel del Pilar
 */

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidName {
    String message() default "Invalid name!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
