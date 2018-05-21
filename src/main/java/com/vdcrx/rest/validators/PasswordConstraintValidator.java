package com.vdcrx.rest.validators;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Password validator using @ValidPassword annotation
 *
 * @author Ranel del Pilar
 *
 * Override initialize() if loading a dictionary file is desired.
 * See here: https://memorynotfound.com/custom-password-constraint-validator-annotation/
 *
 * Web service (API) is also available for checking compromised passwords.
 * See here: https://haveibeenpwned.com/
 *
 */

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private PasswordValidator validator;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        validator = new PasswordValidator(Arrays.asList(

                // at least 8 characters
                new LengthRule(8, 30),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character
                new CharacterRule(EnglishCharacterData.Special, 1),

                // no whitespaces
                new WhitespaceRule()
        ));
    }

    @Override
    public boolean isValid(final String password, ConstraintValidatorContext context) {
        return validatePassword(password, context);
    }

    private boolean validatePassword(final String password, final ConstraintValidatorContext context) {

        if(password == null || password.isEmpty())
            return false;

        RuleResult result = validator.validate(new PasswordData(password));

        if(result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = messages.stream().collect(Collectors.joining(","));
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;
    }
}
