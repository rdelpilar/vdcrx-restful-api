package com.vdcrx.rest.repositories;

import com.vdcrx.rest.domain.entities.Veterinarian;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Veterinarian repository interface
 *
 * @author Ranel del Pilar
 */

@Repository
public interface VeterinarianRepository extends JpaRepository<Veterinarian, UUID> {

    /***************************** RETRIEVE *****************************/

    // TODO: Need to restrict CRUD queries

    /* Find veterinarian given a phone number. Also check if authorized to search by phone */
    @Query("SELECT p.person FROM Phone p WHERE p.phone = :phone")
    List<Veterinarian> findVeterinariansByPhone(@Param("phone") final String phone) throws DataAccessException;

    /* Find by provider id */
    @Query("SELECT pi.person FROM ProviderId pi WHERE pi.dea = :dea")
    Optional<Veterinarian> findVeterinarianByDEA(@Param("dea") final String dea) throws DataAccessException;

    @Query("SELECT pi.person FROM ProviderId pi WHERE pi.me = :me")
    Optional<Veterinarian> findVeterinarianByME(@Param("me") final String me) throws DataAccessException;

    @Query("SELECT pi.person FROM ProviderId pi WHERE pi.npi = :npi")
    Optional<Veterinarian> findVeterinarianByNPI(@Param("npi") final String npi) throws DataAccessException;

    /* Find by veterinarian fields*/

    // TODO: Temporary. Remove.
    @EntityGraph(value = "Veterinarian.graph", type = EntityGraph.EntityGraphType.LOAD)
    List<Veterinarian> findAll() throws DataAccessException;

    @EntityGraph(value = "Veterinarian.graph", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Veterinarian> findVeterinarianById(final UUID id) throws DataAccessException;

    @EntityGraph(value = "Veterinarian.graph", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Veterinarian> findVeterinarianByUsername(final String username) throws DataAccessException;

    @EntityGraph(value = "Veterinarian.graph", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Veterinarian> findVeterinarianByEmail(final String email) throws DataAccessException;

    @EntityGraph(value = "Veterinarian.graph", type = EntityGraph.EntityGraphType.LOAD)
    List<Veterinarian> findVeterinariansByLastNameIsStartingWith(final String lastNme, Pageable pageable) throws DataAccessException;

    @EntityGraph(value = "Veterinarian.graph", type = EntityGraph.EntityGraphType.LOAD)
    List<Veterinarian> findVeterinariansByFirstNameIsStartingWith(final String firstName, Pageable pageable) throws DataAccessException;
}
