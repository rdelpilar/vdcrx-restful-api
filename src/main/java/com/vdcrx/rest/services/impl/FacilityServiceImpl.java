package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.api.v1.mapper.entities.FacilityMapper;
import com.vdcrx.rest.api.v1.model.dto.FacilityDto;
import com.vdcrx.rest.domain.entities.Facility;
import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.domain.enums.FacilityType;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.repositories.FacilityRepository;
import com.vdcrx.rest.repositories.PersonRepository;
import com.vdcrx.rest.services.FacilityService;
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
 * Facility service implementation
 *
 * @author Ranel del Pilar
 */

@Service
public class FacilityServiceImpl implements FacilityService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;
    private final PersonRepository personRepository;

    @Autowired
    public FacilityServiceImpl(FacilityRepository facilityRepository,
                               FacilityMapper facilityMapper,
                               PersonRepository personRepository) {
        this.facilityRepository = facilityRepository;
        this.facilityMapper = facilityMapper;
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public FacilityDto createFacility(final UUID id, final FacilityDto resource) throws DataAccessException {
        Assert(id, resource);

        LOG.debug("Creating new facility for person with id '" + id + "'");

        return personRepository
                .findById(id)
                .map(person ->
                        saveAndReturnDto(person, resource)
                ).orElseThrow(() -> new ResourceNotFoundException("No person found with id '" + id + "'"));
    }

    @Override
    @Transactional
    public FacilityDto updateFacility(final UUID id, final UUID facility_id, final FacilityDto resource) throws DataAccessException {
        Assert(id, facility_id, resource);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Updating facility with id '" + facility_id + "'");

        return facilityRepository.findById(facility_id)
                .map(facility -> {
                    FacilityType facilityType = resource.getFacilityType();
                    String name = resource.getName();

                    if(facilityType != null && !facilityType.name().equals(facility.getFacilityType().name()))
                        facility.setFacilityType(facilityType);
                    if(name != null && !name.equals(facility.getName()))
                        facility.setName(name);
                    return facilityMapper.mapToFacilityDto(facilityRepository.saveAndFlush(facility));
                }).orElseThrow(() -> new ResourceNotFoundException("No facility found with id '" + id + "'"));
    }

    @Override
    @Transactional
    public void deleteFacility(final UUID id, final UUID facility_id) throws DataAccessException {
        Assert(id, facility_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Deleting facility with id '" + facility_id + "'");

        facilityRepository.deleteById(facility_id);
    }

    @Override
    @Transactional(readOnly = true)
    public FacilityDto findFacilityById(final UUID id, final UUID facility_id) {
        Assert(id, facility_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Retrieving facilities with id '" + facility_id + "'");

        return facilityRepository.findById(facility_id)
                .map(facilityMapper::mapToFacilityDto)
                .orElseThrow(() -> new ResourceNotFoundException("No facility found with id '" + facility_id + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityDto> findFacilitiesByPersonId(final UUID id) {
        Assert.notNull(id, "Person id must not be null!");

        LOG.debug("Retrieving facilities for person with id '" + id + "'");

        Set<Facility> facilities = facilityRepository.findFacilitiesByPersonId(id);

        AssertCollection(facilities, id.toString());

        return facilityMapperHelper(facilities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityDto> findFacilitiesByUsername(final String username) {
        Assert.notNull(username, "Username must not be null!");

        LOG.debug("Retrieving facilities for person with username '" + username + "'");

        Set<Facility> facilities = facilityRepository.findFacilitiesByPersonUsername(username);

        AssertCollection(facilities, username);

        return facilityMapperHelper(facilities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityDto> findFacilitiesByEmail(final String email) {
        Assert.notNull(email, "Email must not be null!");

        LOG.debug("Retrieving facilities for person with email '" + email + "'");

        Set<Facility> facilities = facilityRepository.findFacilitiesByPersonEmail(email);

        AssertCollection(facilities, email);

        return facilityMapperHelper(facilities);
    }

    private FacilityDto saveAndReturnDto(final Person person, final FacilityDto dto) {
        Facility facility = facilityMapper.mapToFacility(dto);
        facility.addPerson(person);
        return facilityMapper.mapToFacilityDto(facilityRepository.saveAndFlush(facility));
    }

    private List<FacilityDto> facilityMapperHelper(final Set<Facility> facilities) {
        return facilities
                .stream()
                .map(facilityMapper::mapToFacilityDto)
                .sorted(Comparator.comparing(FacilityDto::getName))
                .collect(Collectors.toList());
    }

    private void Assert(final UUID id, final UUID facility_id) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(facility_id, "Facility id must not be null!");
    }

    private void Assert(final UUID id, final FacilityDto resource) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(resource, "Facility DTO resource must not be null!");
    }

    private void Assert(final UUID id, final UUID facility_id, final FacilityDto resource) {
        Assert(id, resource);
        Assert.notNull(facility_id, "Facility id must not be null!");
    }

    private void AssertCollection(final Collection<?> collection, final String param) {
        if(collection.isEmpty())
            throw new ResourceNotFoundException("No facility found using '" + param + "'");
    }
}
