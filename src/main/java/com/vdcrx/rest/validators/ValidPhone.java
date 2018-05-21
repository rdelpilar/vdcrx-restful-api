package com.vdcrx.rest.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Valid phone number annotation
 *
 * @author Ranel del Pilar
 */

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface ValidPhone {
    String message() default "Invalid phone number!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
