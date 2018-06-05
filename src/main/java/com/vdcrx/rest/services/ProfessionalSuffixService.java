package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.model.dto.ProfessionalSuffixDto;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.UUID;

/**
 * Professional suffix service
 *
 * @author Ranel del Pilar
 */

public interface ProfessionalSuffixService {

    /* Create */
    ProfessionalSuffixDto createProfessionalSuffix(final UUID id, final ProfessionalSuffixDto resource) throws DataAccessException;

    /* Update */
    ProfessionalSuffixDto updateProfessionalSuffix(final UUID id, final UUID suffix_id, final ProfessionalSuffixDto resource) throws DataAccessException;

    /* Delete */
    void deleteProfessionalSuffix(final UUID id, final UUID suffix_id) throws DataAccessException;

    /* Retrieve */
    ProfessionalSuffixDto findProfessionalSuffixById(final UUID id, final UUID suffix_id);
    List<ProfessionalSuffixDto> findProfessionalSuffixesByPersonId(final UUID id);
    List<ProfessionalSuffixDto> findProfessionalSuffixesByUsername(final String username);
    List<ProfessionalSuffixDto> findProfessionalSuffixesByEmail(final String email);
}
