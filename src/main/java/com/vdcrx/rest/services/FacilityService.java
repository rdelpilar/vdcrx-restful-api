package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.model.dto.FacilityDto;
import org.springframework.dao.DataAccessException;

import java.util.List;
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
    FacilityDto findFacilityById(final UUID id, final UUID facility_id);
    List<FacilityDto> findFacilitiesByPersonId(final UUID id);
    List<FacilityDto> findFacilitiesByUsername(final String username);
    List<FacilityDto> findFacilitiesByEmail(final String email);
}
