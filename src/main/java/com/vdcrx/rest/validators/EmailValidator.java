package com.vdcrx.rest.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Email constraint validator using @ValidEmail
 *
 * @author Ranel del Pilar
 */

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private final int MIN = 6;
    private final int MAX = 60;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String email, ConstraintValidatorContext context){
        return validateEmail(email);
    }

    private boolean validateEmail(final String email) {

        if(email == null || email.isEmpty())
            return false;

        if(email.length() >= MIN && email.length() <= MAX) {
            final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }

        return false;
    }
}
