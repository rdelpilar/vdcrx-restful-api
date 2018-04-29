package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.model.dto.AddressDto;
import org.springframework.dao.DataAccessException;

import java.util.Set;
import java.util.UUID;

/**
 * Address service interface
 *
 * @author Ranel del Pilar
 */

public interface AddressService {

    /* Create */
    AddressDto createAddress(final UUID id, final AddressDto resource) throws DataAccessException;

    /* Update */
    AddressDto updateAddress(final UUID id, final UUID address_id, final AddressDto resource) throws DataAccessException;

    /* Delete */
    void deleteAddress(final UUID id, final UUID address_id) throws DataAccessException;

    /* Retrieve */
    AddressDto findAddressById(final UUID id);
    Set<AddressDto> findAddressesByPersonId(final UUID id);
    Set<AddressDto> findAddressByUsername(final String username);
    Set<AddressDto> findAddressesByEmail(final String email);
    Set<AddressDto> findAddressesByPhone(final String phone);
    Set<AddressDto> findAddressesByUsernameCol(final Set<String> usernames);
}
