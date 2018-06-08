package com.vdcrx.rest.repositories;

import com.vdcrx.rest.domain.entities.Address;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

/**
 * Address repository interface
 *
 * @author Ranel del Pilar
 */

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    /* Search */
    Set<Address> findAddressesByPersonId(final UUID id) throws DataAccessException;
    Set<Address> findAddressesByPersonUsername(final String username) throws DataAccessException;
    Set<Address> findAddressesByPersonEmail(final String email) throws DataAccessException;
}
