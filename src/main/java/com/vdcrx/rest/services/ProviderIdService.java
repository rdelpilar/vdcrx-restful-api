package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.model.dto.ProviderIdDto;
import org.springframework.dao.DataAccessException;

import java.util.UUID;

/**
 * Provider identifier service interface
 *
 * @author Ranel del Pilar
 */

public interface ProviderIdService {

    /* Create */
    ProviderIdDto createProviderIdentifier(final UUID id, final ProviderIdDto resource) throws DataAccessException;

    /* Update */
    ProviderIdDto updateProviderIdentifier(final UUID id, final UUID provider_id, ProviderIdDto resource) throws DataAccessException;

    /* Delete */
    void deleteProviderIdentifier(final UUID id, final UUID provider_id) throws DataAccessException;

    /* Retrieve */
    ProviderIdDto findProviderIdById(final UUID id, final UUID provider_Id);
    ProviderIdDto findProviderIdByPersonId(final UUID id);
    ProviderIdDto findProviderIdByUsername(final String username);
    ProviderIdDto findProviderIdByEmail(final String email);
}
