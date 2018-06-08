package com.vdcrx.rest.repositories;

import com.vdcrx.rest.domain.entities.Facility;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

/**
 * Facility repository interface
 *
 * @author Ranel del Pilar
 */

@Repository
public interface FacilityRepository extends JpaRepository<Facility, UUID> {

    // Find facilities owned by person with id = id
    @Query("SELECT facility FROM Facility facility INNER JOIN facility.persons person WHERE person.id = :id")
    Set<Facility> findFacilitiesByPersonId(@Param("id") final UUID id) throws DataAccessException;

    @Query("SELECT facility FROM Facility facility INNER JOIN facility.persons person WHERE person.username = :username")
    Set<Facility> findFacilitiesByPersonUsername(@Param("username") final String username) throws DataAccessException;

    @Query("SELECT facility FROM Facility facility INNER JOIN facility.persons person WHERE person.email = :email")
    Set<Facility> findFacilitiesByPersonEmail(@Param("email") final String email) throws DataAccessException;
}
