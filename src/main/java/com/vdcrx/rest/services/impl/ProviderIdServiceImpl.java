package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.api.v1.mapper.entities.ProviderIdMapper;
import com.vdcrx.rest.api.v1.model.dto.ProviderIdDto;
import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.domain.entities.ProviderId;
import com.vdcrx.rest.domain.entities.Veterinarian;
import com.vdcrx.rest.domain.enums.ProviderIdType;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.repositories.PersonRepository;
import com.vdcrx.rest.repositories.ProviderIdRepository;
import com.vdcrx.rest.services.ProviderIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Professional identifier service implementation
 *
 * @author Ranel del Pilar
 */

@Service
public class ProviderIdServiceImpl implements ProviderIdService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ProviderIdRepository providerIdRepository;
    private final ProviderIdMapper providerIdMapper;
    private final PersonRepository personRepository;

    @Autowired
    public ProviderIdServiceImpl(ProviderIdRepository providerIdRepository,
                                 ProviderIdMapper providerIdMapper,
                                 PersonRepository personRepository) {
        this.providerIdRepository = providerIdRepository;
        this.providerIdMapper = providerIdMapper;
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public ProviderIdDto createProviderIdentifier(final UUID id, final ProviderIdDto resource) throws DataAccessException {
        Assert(id, resource);

        LOG.debug("Creating new provider identifier for person with id '" + id + "'");

        return personRepository
                .findById(id)
                .map(person ->
                        saveAndReturnDto(person, resource)
                ).orElseThrow(() -> new ResourceNotFoundException("No person found with id '" + id + "'"));
    }

    @Override
    @Transactional
    public ProviderIdDto updateProviderIdentifier(final UUID id, final UUID provider_id, final ProviderIdDto resource) throws DataAccessException {
        Assert(id, provider_id, resource);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Updating provider identifier with id '" + provider_id + "'");

        return providerIdRepository.findById(provider_id)
                .map(providerId -> {
                    String dea = resource.getDea();
                    String me = resource.getMe();
                    String npi = resource.getNpi();

                    if (dea != null && !dea.equals(providerId.getDea()))
                        providerId.setDea(dea);
                    if (me != null && !me.equals(providerId.getMe()))
                        providerId.setMe(me);
                    if (npi != null && !npi.equals(providerId.getNpi()))
                        providerId.setNpi(npi);

                    return providerIdMapper.mapToProviderIdDto(providerIdRepository.saveAndFlush(providerId));
                }).orElseThrow(() -> new ResourceNotFoundException("No provider id found with id '" + provider_id + "'"));
    }

    @Override
    @Transactional
    public void deleteProviderIdentifier(final UUID id, final UUID provider_id) throws DataAccessException {
        Assert(id, provider_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Deleting provider identifier with id '" + provider_id + "'");

        providerIdRepository.deleteById(provider_id);
    }

    private ProviderIdDto saveAndReturnDto(final Person person, final ProviderIdDto resource) {
        ProviderId providerId = providerIdMapper.mapToProviderId(resource);
        providerId.setPerson(person);

        if(person instanceof Veterinarian)
            providerId.setProviderIdType(ProviderIdType.VETERINARIAN);

        return providerIdMapper.mapToProviderIdDto(providerIdRepository.saveAndFlush(providerId));
    }

    @Override
    @Transactional
    public ProviderIdDto findProviderIdById(final UUID id, final UUID provider_id) {
        Assert(id, provider_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Retrieving professional identifier by id '" + provider_id + "'");

        return providerIdRepository.findById(provider_id)
                .map(providerIdMapper::mapToProviderIdDto)
                .orElseThrow(() -> new ResourceNotFoundException("No professional identifier found with id '" + provider_id + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderIdDto findProviderIdByPersonId(final UUID id) {
        Assert.notNull(id, "Person Id must not be null!");

        LOG.debug("Retrieving professional identifier by person with id '" + id + "'");

        return providerIdRepository.findProviderIdByPersonId(id)
                .map(providerIdMapper::mapToProviderIdDto)
                .orElseThrow(() -> new ResourceNotFoundException("No professional identifier found with id '" + id + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderIdDto findProviderIdByUsername(final String username) {
        Assert.notNull(username, "Username must not be null!");

        LOG.debug("Retrieving professional identifier for username '" + username + "'");

        return providerIdRepository.findProviderIdByPersonUsername(username)
                .map(providerIdMapper::mapToProviderIdDto)
                .orElseThrow(() -> new ResourceNotFoundException("No provider identifier found using '" + username + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderIdDto findProviderIdByEmail(final String email) {
        Assert.notNull(email, "Email must not be null!");

        LOG.debug("Retrieving professional identifier for email '" + email + "'");

        return providerIdRepository.findProviderIdByPersonEmail(email)
                .map(providerIdMapper::mapToProviderIdDto)
                .orElseThrow(() -> new ResourceNotFoundException("No provider identifier found using '" + email + "'"));
    }

    private void Assert(final UUID id, final UUID prov_id) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(prov_id, "Professional identifier id must not be null!");
    }

    private void Assert(final UUID id, final ProviderIdDto resource) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(resource, "Professional identifier Dto resource must not be null!");
    }

    private void Assert(final UUID id, final UUID prov_id, final ProviderIdDto resource) {
        Assert(id, resource);
        Assert.notNull(prov_id, "Professional identifier id must not be null!");
    }
}
