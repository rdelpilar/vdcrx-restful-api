package com.vdcrx.rest.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Valid signature annotation
 *
 * @author Ranel del Pilar
 */

@Documented
@Constraint(validatedBy = { SignatureValidator.class })
@Target({ TYPE, METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidSignature {
    String message() default "Invalid Signature image format!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
