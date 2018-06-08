package com.vdcrx.rest.repositories;

import com.vdcrx.rest.domain.entities.Role;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Role repository interface
 *
 * @author Ranel del Pilar
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query("SELECT r FROM Role r INNER JOIN r.persons p WHERE p.id = :id")
    Set<Role> findRolesByPersonId(@Param("id") final UUID uuid) throws DataAccessException;

    @Query("SELECT r FROM Role r INNER JOIN r.persons p WHERE p.username = :username")
    Set<Role> findRolesByPersonUsername(@Param("username") final String username) throws DataAccessException;
}
