package com.vdcrx.rest.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Username validator for @ValidUsername
 *
 * @author Ranel del Pilar
 */

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private final int MIN = 6;
    private final int MAX = 32;

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String username, ConstraintValidatorContext constraintValidatorContext) {
        return validateUsername(username);
    }

    private boolean validateUsername(final String username) {

        if(username == null || username.isEmpty())
            return false;

        if(username.length() >= MIN && username.length() <= MAX) {

            final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]*$";

            Pattern pattern = Pattern.compile(USERNAME_PATTERN);
            Matcher matcher = pattern.matcher(username);
            return matcher.matches();
        }

        return false;
    }
}
