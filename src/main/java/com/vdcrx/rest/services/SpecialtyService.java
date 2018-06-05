package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.model.dto.SpecialtyDto;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.UUID;

/**
 * Specialty service
 *
 * @author Ranel del Pilar
 */

public interface SpecialtyService {

    /* Create */
    SpecialtyDto createSpecialty(final UUID id, final SpecialtyDto resource) throws DataAccessException;

    /* Update */
    SpecialtyDto updateSpecialty(final UUID id, final UUID specialty_id, SpecialtyDto resource) throws DataAccessException;

    /* Delete */
    void deleteSpecialty(final UUID id, final UUID specialty_id) throws DataAccessException;

    /* Retrieve */
    SpecialtyDto findSpecialtyById(final UUID id, final UUID specialty_id);
    List<SpecialtyDto> findSpecialtiesByPersonId(final UUID id);
    List<SpecialtyDto> findSpecialtiesByUsername(final String username);
    List<SpecialtyDto> findSpecialtiesByEmail(final String email);
}
