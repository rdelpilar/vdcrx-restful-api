package com.vdcrx.rest.repositories;

import com.vdcrx.rest.domain.entities.Phone;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

/**
 * Phone repository interface
 *
 * @author Ranel del Pilar
 */

@Repository
public interface PhoneRepository extends JpaRepository<Phone, UUID> {

    Set<Phone> findPhonesByPersonId(final UUID id) throws DataAccessException;
    Set<Phone> findPhoneByPersonUsername(final String username) throws DataAccessException;
    Set<Phone> findPhoneByPersonEmail(final String email) throws DataAccessException;
}
