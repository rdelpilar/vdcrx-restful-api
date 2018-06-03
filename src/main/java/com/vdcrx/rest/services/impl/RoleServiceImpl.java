package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.domain.entities.Role;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.repositories.RoleRepository;
import com.vdcrx.rest.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * Role service implementation
 *
 * @author Ranel del Pilar
 */

@Service
public class RoleServiceImpl implements RoleService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Role findRoleById(final UUID id) {
        Assert.notNull(id, "Role id must not be null!");

        LOG.debug("Retrieving role with id '" + id + "'");

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No role found using id '" + id + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> findRolesByPersonId(final UUID id) {
        Assert.notNull(id, "Uuid must not be null!");

        LOG.debug("Retrieving roles for person with id '" + id + "'");

        Set<Role> roles = findRolesByPersonId(id);

        AssertCollection(roles, id.toString());

        return roles;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> findRolesByUsername(final String username) {
        Assert.notNull(username, "Username must not be null!");

        LOG.debug("Retrieving roles for username '" + username + "'");

        Set<Role> roles = findRolesByUsername(username);

        AssertCollection(roles, username);

        return roles;
    }

    private void AssertCollection(final Collection<?> collection, final String param) {
        if(collection.isEmpty())
            throw new ResourceNotFoundException("No roles found using '" + param + "'");
    }
}
