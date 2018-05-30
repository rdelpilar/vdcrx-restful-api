package com.vdcrx.rest.constants;

/**
 * API constants
 *
 * Stores application wide constants
 *
 * @author Ranel del Pilar
 */

public abstract class ApiConstants {

    public static final String BASE_PATH = "/api/v1";
    public static final String VET_BASE_PATH = BASE_PATH + "/veterinarian";
    public static final String VET_SIGNUP_PATH = VET_BASE_PATH + "/signup";

    public static final String LOGIN_PATH = "/login";

    public static final String PET_OWNER_BASE_PATH = "/api/v1/petowner";
    public static final String PROVIDER_ID_BASE_PATH = "/api/v1/provider_id";
}
