package com.vdcrx.rest.utils;

import lombok.NoArgsConstructor;

/**
 * Phone number utility class
 *
 * @author Ranel del Pilar
 */

@NoArgsConstructor
public class PhoneNumberUtil {

    /**
     * Strips the input string of whitespaces and special characters
     *
     * @param phoneNumber   TransactionSystemException
     * @return              The stripped String object
     */
    public static String stripWhitespaces(String phoneNumber) {
        return phoneNumber.replaceAll("[^\\d]", "");
    }
}
