package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.api.v1.mapper.entities.ProfessionalSuffixMapper;
import com.vdcrx.rest.api.v1.model.dto.ProfessionalSuffixDto;
import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.domain.entities.ProfessionalSuffix;
import com.vdcrx.rest.domain.enums.ProfessionalSuffixType;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.repositories.PersonRepository;
import com.vdcrx.rest.repositories.ProfessionalSuffixRepository;
import com.vdcrx.rest.services.ProfessionalSuffixService;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Professional suffix service implementation
 *
 * @author Ranel del Pilar
 */

@Service
public class ProfessionalSuffixServiceImpl implements ProfessionalSuffixService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ProfessionalSuffixRepository professionalSuffixRepository;
    private final ProfessionalSuffixMapper professionalSuffixMapper;
    private final PersonRepository personRepository;

    @Autowired
    public ProfessionalSuffixServiceImpl(ProfessionalSuffixRepository professionalSuffixRepository,
                                         ProfessionalSuffixMapper professionalSuffixMapper,
                                         PersonRepository personRepository) {
        this.professionalSuffixRepository = professionalSuffixRepository;
        this.professionalSuffixMapper = professionalSuffixMapper;
        this.personRepository = personRepository;
    }


    @Override
    @Transactional
    public ProfessionalSuffixDto createProfessionalSuffix(final UUID id, final ProfessionalSuffixDto resource) throws DataAccessException {
        Assert(id, resource);

        LOG.debug("Creating new professional suffix for person with id '" + id + "'");

        return personRepository
                .findById(id)
                .map(person ->
                    saveAndReturnDto(person, resource)
                ).orElseThrow(() -> new ResourceNotFoundException("No person found with id '" + id + "'"));
    }

    @Override
    @Transactional
    public ProfessionalSuffixDto updateProfessionalSuffix(final UUID id, final UUID suffix_id, ProfessionalSuffixDto resource) throws DataAccessException {
        Assert(id, suffix_id, resource);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Updating professional suffix with id '" + suffix_id + "'");

        return professionalSuffixRepository.findById(suffix_id)
                .map(suffix -> {
                    ProfessionalSuffixType suffixType = resource.getProfessionalSuffixType();

                    if (suffixType != null && !suffixType.name().equals(suffix.getProfessionalSuffixType().name()))
                        suffix.setProfessionalSuffixType(suffixType);

                    return professionalSuffixMapper.mapToProfessionalSuffixDto(professionalSuffixRepository.saveAndFlush(suffix));
                }).orElseThrow(() -> new ResourceNotFoundException("No professional suffix found with id '" + suffix_id + "'"));
    }

    @Override
    @Transactional
    public void deleteProfessionalSuffix(final UUID id, final UUID suffix_id) throws DataAccessException {
        Assert(id, suffix_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Deleting professional suffix with id '" + suffix_id + "'");

        professionalSuffixRepository.deleteById(suffix_id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessionalSuffixDto findProfessionalSuffixById(final UUID id, final UUID suffix_id) {
        Assert(id, suffix_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Retrieving professional suffix with id '" + suffix_id + "'");

        return professionalSuffixRepository.findById(id)
                .map(professionalSuffixMapper::mapToProfessionalSuffixDto)
                .orElseThrow(() -> new ResourceNotFoundException("No professional suffix found with id '" + suffix_id + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalSuffixDto> findProfessionalSuffixesByPersonId(final UUID id) {
        Assert.notNull(id, "Person id must not be null!");

        LOG.debug("Retrieving professional suffix for person with id '" + id + "'");

        Set<ProfessionalSuffix> suffixes = professionalSuffixRepository.findProfessionalSuffixesByPersonId(id);

        AssertCollection(suffixes, id.toString());

        return professionalSuffixMapperHelper(suffixes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalSuffixDto> findProfessionalSuffixesByUsername(final String username) {
        Assert.notNull(username, "Username must not be null!");

        LOG.debug("Retrieving professional suffix for person with username '" + username + "'");

        Set<ProfessionalSuffix> suffixes = professionalSuffixRepository.findProfessionalSuffixesByPersonUsername(username);

        AssertCollection(suffixes, username);

        return professionalSuffixMapperHelper(suffixes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalSuffixDto> findProfessionalSuffixesByEmail(final String email) {
        Assert.notNull(email, "Email must not be null!");

        LOG.debug("Retrieving professional suffix for person with email '" + email + "'");

        Set<ProfessionalSuffix> suffixes = professionalSuffixRepository.findProfessionalSuffixesByPersonEmail(email);

        AssertCollection(suffixes, email);

        return professionalSuffixMapperHelper(suffixes);
    }

    private ProfessionalSuffixDto saveAndReturnDto(final Person person, final ProfessionalSuffixDto dto) {
        ProfessionalSuffix suffix = professionalSuffixMapper.mapToProfessionalSuffix(dto);
        suffix.setPerson(person);
        return professionalSuffixMapper.mapToProfessionalSuffixDto(professionalSuffixRepository.saveAndFlush(suffix));
    }

    private List<ProfessionalSuffixDto> professionalSuffixMapperHelper(final Set<ProfessionalSuffix> suffixes) {
        return suffixes
                .stream()
                .map(professionalSuffixMapper::mapToProfessionalSuffixDto)
                .sorted(Comparator.comparing(ProfessionalSuffixDto::getProfessionalSuffixType))
                .collect(Collectors.toList());
    }

    private void Assert(final UUID id, final UUID suffix_id) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(suffix_id, "Professional suffix id must be null!");
    }

    private void Assert(final UUID id, final ProfessionalSuffixDto resource) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(resource, "Professional suffix DTO resource must not be null!");
    }

    private void Assert(final UUID id, final UUID suffix_id, final ProfessionalSuffixDto resource) {
        Assert(id, resource);
        Assert.notNull(suffix_id, "Professional suffix id must be null!");
    }

    private void AssertCollection(final Collection<?> collection, final String param) {
        if(collection.isEmpty())
            throw new ResourceNotFoundException("No professional suffix found using '" + param + "'");
    }
}
