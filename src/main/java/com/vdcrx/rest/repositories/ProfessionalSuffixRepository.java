package com.vdcrx.rest.repositories;

import com.vdcrx.rest.domain.entities.ProfessionalSuffix;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

/**
 * Professional suffix repository interface
 *
 * @author Ranel del Pilar
 */

@Repository
public interface ProfessionalSuffixRepository extends JpaRepository<ProfessionalSuffix, UUID> {
    Set<ProfessionalSuffix> findProfessionalSuffixesByPersonId(final UUID id) throws DataAccessException;
    Set<ProfessionalSuffix> findProfessionalSuffixesByPersonEmail(final String email) throws DataAccessException;
    Set<ProfessionalSuffix> findProfessionalSuffixesByPersonUsername(final String username) throws DataAccessException;
}
