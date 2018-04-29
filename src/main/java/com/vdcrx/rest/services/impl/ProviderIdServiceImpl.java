package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.api.v1.mapper.entities.ProviderIdMapper;
import com.vdcrx.rest.api.v1.model.dto.ProviderIdDto;
import com.vdcrx.rest.domain.entities.ProviderId;
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

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Professional identifier service implementation
 *
 * @author Ranel del Pilar
 */

@Service
public class ProviderIdServiceImpl implements ProviderIdService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private ProviderIdRepository providerIdRepository;
    private ProviderIdMapper providerIdMapper;
    private PersonRepository personRepository;

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
                        saveAndReturnDto(resource)
                ).orElseThrow(() -> new ResourceNotFoundException("No provider identifier found with id '" + id + "'"));
    }

    @Override
    @Transactional
    public ProviderIdDto updateProviderIdentifier(final UUID id, final UUID provider_id, final ProviderIdDto resource) throws DataAccessException {
        Assert(id, provider_id, resource);

        if(!personRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("No person found with id '" + id + "'");
        }

        LOG.debug("Updating provider identifier with id '" + provider_id + "'");

        return providerIdRepository.findById(provider_id)
                .map(providerId -> {
                    ProviderIdType providerIdType = resource.getProviderIdType();
                    String dea = resource.getDea();
                    String me = resource.getMe();
                    String npi = resource.getNpi();

                    if (providerIdType != null && !providerIdType.name().equals(providerId.getProviderIdType().name()))
                        providerId.setProviderIdType(providerIdType);
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

    private ProviderIdDto saveAndReturnDto(final ProviderIdDto resource) {
        ProviderId providerId = providerIdMapper.mapToProviderId(resource);
        return providerIdMapper.mapToProviderIdDto(providerIdRepository.saveAndFlush(providerId));
    }

    @Override
    @Transactional
    public ProviderIdDto findProviderIdById(final UUID id) {
        Assert(id);

        LOG.debug("Retrieving professional identifier by id '" + id + "'");

        return providerIdRepository.findById(id)
                .map(providerIdMapper::mapToProviderIdDto)
                .orElseThrow(() -> new ResourceNotFoundException("No professional identifier found with id '" + id + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderIdDto findProviderIdByPersonId(final UUID id) {
        Assert.notNull(id, "Person Id must not be null!");

        LOG.debug("Retrieving Professional Identifier by person with id '" + id + "'");

        return providerIdRepository.findProviderIdByPersonUuid(id)
                .map(providerIdMapper::mapToProviderIdDto)
                .orElseThrow(() -> new ResourceNotFoundException("No Person found with id '" + id + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderIdDto findProviderIdByUsername(final String username) {
        Assert.notNull(username, "Username must not be null!");

        LOG.debug("Retrieving Professional Identifier for username '" + username + "'");

        return providerIdMapperHelper(providerIdRepository.findProviderIdByPersonUsername(username));
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderIdDto findProviderIdByEmail(final String email) {
        Assert.notNull(email, "Email must not be null!");

        LOG.debug("Retrieving Professional Identifier for email '" + email + "'");

        return providerIdMapperHelper(providerIdRepository.findProviderIdByPersonEmail(email));
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderIdDto findProviderIdByPhone(final String phone) {
        Assert.notNull(phone, "Phone must not be null!");

        LOG.debug("Retrieving Professional Identifier for phone '" + phone + "'");

        return providerIdMapperHelper(providerIdRepository.findProviderIdByPhone(phone));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ProviderIdDto> findProviderIdsByUsernameCol(final Set<String> usernames) {
        Assert.notNull(usernames, "Usernames must not be null!");

        usernames.forEach(username -> LOG.debug("Retrieving Professional Identifier for username '" + username + "'"));

        Optional<Set<ProviderId>> providerIds = Optional.of(providerIdRepository.findProviderIdByPersonUsernameIn(usernames));

        return providerIds
                .map(providerIdMapper::mapToProviderIdDtoSet)
                .orElseThrow(() -> new ResourceNotFoundException("No Professional Identifiers found from list of usernames!"));
    }

    private ProviderIdDto providerIdMapperHelper(Optional<ProviderId> providerId) {
        return providerId.map(providerIdMapper::mapToProviderIdDto)
                .orElseThrow(() -> new ResourceNotFoundException("No Professional Identifier found!"));
    }

    private void Assert(final UUID id) {
        Assert.notNull(id, "Professional Identifier id must not be null!");
    }

    private void Assert(final UUID id, final UUID phone_id) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(phone_id, "Professional Identifier id must not be null!");
    }

    private void Assert(final UUID id, final ProviderIdDto resource) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(resource, "Professional Identifier Dto resource must not be null!");
    }

    private void Assert(final UUID id, final UUID phone_id, final ProviderIdDto resource) {
        Assert(id, resource);
        Assert.notNull(phone_id, "Professional Identifier id must not be null!");
    }
}
