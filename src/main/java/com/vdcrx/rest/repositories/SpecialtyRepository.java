package com.vdcrx.rest.repositories;

import com.vdcrx.rest.domain.entities.Specialty;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

/**
 * SpecialtyService repository interface
 *
 * @author Ranel del Pilar
 */

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, UUID> {

    // Find specialties owned by person with id = id
    @Query("SELECT specialty FROM VeterinarianSpecialty specialty INNER JOIN specialty.persons person WHERE person.id =:id")
    Set<Specialty> findVeterinarianSpecialtiesByPersonId(@Param("id") final UUID id) throws DataAccessException;

    @Query("SELECT specialty FROM VeterinarianSpecialty specialty INNER JOIN specialty.persons person WHERE person.username =:username")
    Set<Specialty> findVeterinarianSpecialtiesByPersonUsername(@Param("username") final String username) throws DataAccessException;

    @Query("SELECT specialty FROM VeterinarianSpecialty specialty INNER JOIN specialty.persons person WHERE person.email =:email")
    Set<Specialty> findVeterinarianSpecialtiesByPersonEmail(@Param("email") final String email) throws DataAccessException;


    // TODO: Follow through using VeterinarianSpecialty
    //@Query("SELECT specialty FROM MedicalProviderSpecialty specialty INNER JOIN specialty.persons person WHERE person.id =:id")
    //Set<Specialty> findMedicalProviderSpecialtiesByPersonId(@Param("id") final UUID id) throws DataAccessException;
}
