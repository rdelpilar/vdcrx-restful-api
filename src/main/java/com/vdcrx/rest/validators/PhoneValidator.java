package com.vdcrx.rest.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Phone number validator using @ValidPhone
 *
 * @author Ranel del Pilar
 */

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return validatePhoneNumber(phoneNumber);
    }

    private boolean validatePhoneNumber(final String phoneNumber) {

        if(phoneNumber == null || phoneNumber.isEmpty())
            return false;

        final String PHONE_NUMBER_PATTERN = "(?:(?:(\\s*\\(?([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\)?\\s*(?:[.-]\\s*)?)([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})";

        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}
