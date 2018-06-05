package com.vdcrx.rest.services;

import com.vdcrx.rest.domain.entities.Role;

import java.util.Set;
import java.util.UUID;

/**
 * Role service interface
 *
 * @author Ranel del Pilar
 */

public interface RoleService {

    // Limit Role Service functionality

    Role findRoleById(final UUID id);
    Set<Role> findRolesByPersonId(final UUID id);
    Set<Role> findRolesByUsername(final String username);
}
