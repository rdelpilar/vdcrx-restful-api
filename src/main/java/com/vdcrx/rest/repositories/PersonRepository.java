package com.vdcrx.rest.repositories;

import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.domain.entities.Phone;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Person repository interface
 *
 * @author Ranel del Pilar
 */

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    //@Cacheable(value = "usernameCache")
    //@EntityGraph(value = "Veterinarian.graph", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Person> findById(final UUID id) throws DataAccessException;

    @EntityGraph(value = "Person.roles.graph", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Person> findByUsername(final String username) throws DataAccessException;

    Set<Person> findByEmail(final String email) throws DataAccessException;
    Set<Person> findByPhonesIn(final Set<Phone> phones) throws DataAccessException;
}
