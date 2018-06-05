package com.vdcrx.rest.services;

import java.util.UUID;

/**
 * Password service interface
 *
 * @author Ranel del Pilar
 */

public interface PasswordService {

    /* Create */
    String encodePassword(final String password);

    /* Update */
    void updatePassword(final UUID id, final String replacement, final String current);
}
