package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.api.v1.mapper.entities.SpecialtyMapper;
import com.vdcrx.rest.api.v1.model.dto.SpecialtyDto;
import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.domain.entities.Specialty;
import com.vdcrx.rest.domain.entities.Veterinarian;
import com.vdcrx.rest.domain.entities.VeterinarianSpecialty;
import com.vdcrx.rest.domain.enums.VetSpecialtyType;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.repositories.PersonRepository;
import com.vdcrx.rest.repositories.SpecialtyRepository;
import com.vdcrx.rest.services.SpecialtyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Specialty service implementation
 *
 * @author Ranel del Pilar
 */

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private SpecialtyRepository specialtyRepository;
    private SpecialtyMapper specialtyMapper;
    private PersonRepository personRepository;

    @Autowired
    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository,
                                SpecialtyMapper specialtyMapper,
                                PersonRepository personRepository) {
        this.specialtyRepository = specialtyRepository;
        this.specialtyMapper = specialtyMapper;
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public SpecialtyDto createSpecialty(final UUID id, final SpecialtyDto resource) throws DataAccessException {
        Assert(id, resource);

        LOG.debug("Creating new specialty for person with id '" + id + "'");

        return personRepository
                .findById(id)
                .map(person ->
                        saveAndReturnDto(person, resource)
                ).orElseThrow(() -> new ResourceNotFoundException("No person found with id '" + id + "'"));
    }

    @Override
    @Transactional
    public SpecialtyDto updateSpecialty(final UUID id, final UUID specialty_id, final SpecialtyDto resource) throws DataAccessException {
        Assert(id, specialty_id, resource);

        final Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No person found with id '" + id + "'"));

        LOG.debug("Updating specialty with id '" + specialty_id + "'");

        return specialtyRepository.findById(specialty_id)
                .map(specialty -> {
                    String name = resource.getSpecialty();

                    if(person instanceof Veterinarian) {
                        if(name != null && !name.equals(((VeterinarianSpecialty) specialty).getVetSpecialtyType().name()))
                            ((VeterinarianSpecialty) specialty).setVetSpecialtyType(VetSpecialtyType.valueOf(name));
                    }

                    return specialtyMapper.mapToSpecialtyDto(specialtyRepository.saveAndFlush(specialty));
                }).orElseThrow(() -> new ResourceNotFoundException("No specialty found with id '" + specialty_id + "'"));
    }

    @Override
    @Transactional
    public void deleteSpecialty(final UUID id, final UUID specialty_id) throws DataAccessException {
        Assert(id, specialty_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Deleting specialty with id '" + specialty_id + "'");

        specialtyRepository.deleteById(specialty_id);
    }

    @Override
    @Transactional(readOnly = true)
    public SpecialtyDto findSpecialtyById(final UUID id, final UUID specialty_id) {
        Assert(id, specialty_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Retrieving specialty with id '" + specialty_id + "'");

        return specialtyRepository.findById(specialty_id)
                .map(specialtyMapper::mapToSpecialtyDto)
                .orElseThrow(() -> new ResourceNotFoundException("No specialty found with id '" + specialty_id + "'"));
    }

    @Override
    @Transactional
    public List<SpecialtyDto> findSpecialtiesByPersonId(final UUID id) {
        Assert.notNull(id, "Person id must not be null !");

        LOG.debug("Retrieving specialties with person id '" + id + "'");

        Set<Specialty> specialties = specialtyRepository.findVeterinarianSpecialtiesByPersonId(id);

        AssertCollection(specialties, id.toString());

        return specialtyMapperHelper(specialties);
    }

    @Override
    @Transactional
    public List<SpecialtyDto> findSpecialtiesByUsername(final String username) {
        Assert.notNull(username, "Username must not be null !");

        LOG.debug("Retrieving specialties for username '" + username + "'");

        Set<Specialty> specialties = specialtyRepository.findVeterinarianSpecialtiesByPersonUsername(username);

        AssertCollection(specialties, username);

        return specialtyMapperHelper(specialties);
    }

    @Override
    @Transactional
    public List<SpecialtyDto> findSpecialtiesByEmail(final String email) {
        Assert.notNull(email, "Email must not be null !");

        LOG.debug("Retrieving specialties for email '" + email + "'");

        Set<Specialty> specialties = specialtyRepository.findVeterinarianSpecialtiesByPersonEmail(email);

        AssertCollection(specialties, email);

        return specialtyMapperHelper(specialties);
    }

    private SpecialtyDto saveAndReturnDto(final Person person, final SpecialtyDto dto) {
        Specialty specialty = specialtyMapper.mapToSpecialty(person, dto);

        if(person instanceof Veterinarian) {
            ((Veterinarian) person).addSpecialty(specialty);
        }

        return specialtyMapper.mapToSpecialtyDto(specialtyRepository.saveAndFlush(specialty));
    }

    private List<SpecialtyDto> specialtyMapperHelper(Set<Specialty> specialties) {
        return specialties
                .stream()
                .map(specialtyMapper::mapToSpecialtyDto)
                .sorted(Comparator.comparing(SpecialtyDto::getSpecialty))
                .collect(Collectors.toList());
    }

    private void Assert(final UUID id, final UUID specialty_id) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(specialty_id, "Specialty id must not be null!");
    }

    private void Assert(final UUID id, final SpecialtyDto resource) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(resource, "Specialty DTO resource must not be null!");
    }

    private void Assert(final UUID id, final UUID specialty_id, final SpecialtyDto resource) {
        Assert(id, resource);
        Assert.notNull(specialty_id, "Specialty id must not be null!");
    }

    private void AssertCollection(final Collection<?> collection, final String param) {
        if(collection.isEmpty())
            throw new ResourceNotFoundException("No specialty found using '" + param + "'");
    }
}
