package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.exceptions.PasswordsMismatchException;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.repositories.PersonRepository;
import com.vdcrx.rest.services.PasswordService;
import com.vdcrx.rest.validators.ValidPassword;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Password service implementation
 *
 * @author Ranel del Pilar
 */

@Service
public class PasswordServiceImpl implements PasswordService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    @Autowired
    public PasswordServiceImpl(PasswordEncoder passwordEncoder,
                               PersonRepository personRepository) {
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
    }

    @Override
    public String encodePassword(final String password) {
        Assert.notNull(password, "Password must not be null!");

        return passwordEncoder.encode(password);
    }

    @Override
    @Transactional
    public void updatePassword(final UUID id, @ValidPassword final String replacement, final String current) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(replacement, "Replacement password must not be null!");
        Assert.notNull(current, "Current password must not be null!");

        LOG.debug("Updating password of user id '" + id + "'");

        personRepository.findById(id)
                .map(person -> {
                    if (!passwordsMatch(current, person.getHashedPassword()))
                        throw new PasswordsMismatchException("Incorrect old password!");

                    person.setHashedPassword(passwordEncoder.encode(replacement));
                    return personRepository.saveAndFlush(person);
                }).orElseThrow(() -> new ResourceNotFoundException("No person found with id '" + id + "'"));

    }

    private boolean passwordsMatch(final String update, final String current) {
        return passwordEncoder.matches(update, current);
    }
}
