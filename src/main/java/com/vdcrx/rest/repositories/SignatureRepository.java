package com.vdcrx.rest.repositories;

import com.vdcrx.rest.domain.entities.Signature;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Signature repository interface
 *
 * @author Ranel del Pilar
 */

@Repository
public interface SignatureRepository extends JpaRepository<Signature, UUID> {
    Optional<Signature> findSignatureByPersonId(final UUID id) throws DataAccessException;
    Optional<Signature> findSignatureByPersonUsername(final String username) throws DataAccessException;
    Optional<Signature> findSignatureByPersonEmail(final String email) throws DataAccessException;
}
