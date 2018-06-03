package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.api.v1.mapper.security.AuthorizedUserMapper;
import com.vdcrx.rest.repositories.PersonRepository;
import com.vdcrx.rest.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Person service implementation
 *
 * @author Ranel del Pilar
 */

@Service
public class PersonServiceImpl implements PersonService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final AuthorizedUserMapper mapper;
    private final PersonRepository repository;

    @Autowired
    public PersonServiceImpl(AuthorizedUserMapper mapper,
                             PersonRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Assert.notNull(username, "Username must not be null!");

        LOG.debug("Retrieving username '" + username + "'");

        return repository.findByUsername(username)
                .map(mapper::mapToAuthorizedUser)
                .orElseThrow(() -> new InternalAuthenticationServiceException("Bad Credentials!"));
    }
}
