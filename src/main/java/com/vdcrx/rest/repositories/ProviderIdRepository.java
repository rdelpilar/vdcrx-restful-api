package com.vdcrx.rest.repositories;

import com.vdcrx.rest.domain.entities.ProviderId;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Provider identifier repository interface
 *
 * @author Ranel del Pilar
 */

@Repository
public interface ProviderIdRepository extends JpaRepository<ProviderId, UUID> {

    Optional<ProviderId> findProviderIdByPersonId(final UUID id) throws DataAccessException;
    Optional<ProviderId> findProviderIdByPersonUsername(final String username) throws DataAccessException;
    Optional<ProviderId> findProviderIdByPersonEmail(final String email) throws DataAccessException;
}
