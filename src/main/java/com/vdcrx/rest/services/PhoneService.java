package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.model.dto.PhoneDto;
import org.springframework.dao.DataAccessException;

import java.util.Set;
import java.util.UUID;

public interface PhoneService {

    /* Create */
    PhoneDto createPhone(final UUID id, final PhoneDto resource) throws DataAccessException;

    /* Update */
    PhoneDto updatePhone(final UUID id, final UUID phone_id, final PhoneDto resource) throws DataAccessException;

    /* Delete */
    void deletePhone(final UUID id, final UUID phone_id) throws DataAccessException;

    /* Retrieve */
    PhoneDto findPhoneById(final UUID id);
    Set<PhoneDto> findPhonesByPersonId(final UUID id);
    Set<PhoneDto> findPhonesByUsername(final String username);
    Set<PhoneDto> findPhonesByEmail(final String email);
    Set<PhoneDto> findPhonesByUsernameCol(final Set<String> usernameSet);
}
