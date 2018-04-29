package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.model.dto.FacilityDto;
import com.vdcrx.rest.domain.entities.Facility;
import org.springframework.dao.DataAccessException;

import java.util.Set;
import java.util.UUID;

/**
 * Veterinarian facility service
 *
 * @author Ranel del Pilar
 */

public interface FacilityService {

    /* Create */
    FacilityDto createFacility(final UUID id, final FacilityDto resource) throws DataAccessException;

    /* Update */
    FacilityDto updateFacility(final UUID id, final UUID facility_id, FacilityDto resource) throws DataAccessException;

    /* Delete */
    void deleteFacility(final UUID id, final UUID facility_id) throws DataAccessException;

    /* Retrieve */
    FacilityDto findFacilityById(final UUID id);
    Set<FacilityDto> findFacilitiesByPersonId(final UUID id);
    Set<FacilityDto> findFacilitiesByUsername(final String username);
    Set<FacilityDto> findFacilitiesByEmail(final String email);
    Set<FacilityDto> findFacilitiesByPhone(final String phone);
    Set<Facility> findFacilitiesByUsernameIn(final Set<String> usernames);
}
