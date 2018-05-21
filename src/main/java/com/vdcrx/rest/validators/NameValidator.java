package com.vdcrx.rest.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Legal name constraint validator using @ValidName
 *
 * @author Ranel del Pilar
 */

public class NameValidator implements ConstraintValidator<ValidName, String> {

    private final int MIN = 2;
    private final int MAX = 32;

    @Override
    public void initialize(ValidName constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String name, ConstraintValidatorContext constraintValidatorContext) {
        return validateName(name);
    }

    private boolean validateName(final String name) {

        if(name == null || name.isEmpty())
            return false;

        if(name.length() >= MIN && name.length() <= MAX) {

            final String NAME_PATTERN = "^[a-zA-Z- ]*$"; // English alphabet, space ' ', and '-'

            Pattern pattern = Pattern.compile(NAME_PATTERN);
            Matcher matcher = pattern.matcher(name);
            return matcher.matches();
        }

        return false;
    }
}
