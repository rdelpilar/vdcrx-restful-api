package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.model.dto.PhoneDto;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.UUID;

/**
 * Phone service
 *
 * @author Ranel del Pilar
 */

public interface PhoneService {

    /* Create */
    PhoneDto createPhone(final UUID id, final PhoneDto resource) throws DataAccessException;

    /* Update */
    PhoneDto updatePhone(final UUID id, final UUID phone_id, final PhoneDto resource) throws DataAccessException;

    /* Delete */
    void deletePhone(final UUID id, final UUID phone_id) throws DataAccessException;

    /* Retrieve */
    PhoneDto findPhoneById(final UUID id, final UUID phone_Id);
    List<PhoneDto> findPhonesByPersonId(final UUID id);
    List<PhoneDto> findPhonesByUsername(final String username);
    List<PhoneDto> findPhonesByEmail(final String email);
}
