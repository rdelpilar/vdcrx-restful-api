package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.api.v1.mapper.entities.PhoneMapper;
import com.vdcrx.rest.api.v1.model.dto.PhoneDto;
import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.domain.entities.Phone;
import com.vdcrx.rest.domain.enums.PhoneType;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.repositories.PersonRepository;
import com.vdcrx.rest.repositories.PhoneRepository;
import com.vdcrx.rest.services.PhoneService;
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

import static com.vdcrx.rest.utils.PhoneNumberUtil.stripWhitespaces;

/**
 * Phone service implementation
 *
 * @author Ranel del Pilar
 */

@Service
public class PhoneServiceImpl implements PhoneService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private PhoneRepository phoneRepository;
    private PhoneMapper phoneMapper;
    private PersonRepository personRepository;

    @Autowired
    public PhoneServiceImpl(PhoneRepository phoneRepository,
                            PhoneMapper phoneMapper,
                            PersonRepository personRepository) {
        this.phoneRepository = phoneRepository;
        this.phoneMapper = phoneMapper;
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public PhoneDto createPhone(final UUID id, final PhoneDto resource) throws DataAccessException {
        Assert(id, resource);

        LOG.debug("Creating new phone for person with id '" + id + "'");

        return personRepository
                .findById(id)
                .map(person ->
                    saveAndReturnDto(person, resource)
                ).orElseThrow(() -> new ResourceNotFoundException("No person found with id '" + id + "'"));
    }


    @Override
    @Transactional
    public PhoneDto updatePhone(final UUID id, final UUID phone_id, final PhoneDto resource) throws DataAccessException {
        Assert(id, phone_id, resource);

        if(!personRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("No person found with id '" + id + "'");
        }

        LOG.debug("Updating phone with id '" + phone_id + "'");

        return phoneRepository.findById(phone_id)
                .map(phone -> {
                    PhoneType phoneType = resource.getPhoneType();
                    String phoneNumber = resource.getPhone();
                    String phoneExt = resource.getPhoneExt();

                    if(phoneType != null && !phoneType.name().equals(phone.getPhoneType().name()))
                        phone.setPhoneType(phoneType);
                    if(phoneNumber != null && !phoneNumber.equals(phone.getPhone()))
                        phone.setPhone(phoneNumber);
                    if(phoneExt != null && !phoneExt.equals(phone.getPhoneExt()))
                        phone.setPhoneExt(phoneExt);

                    return phoneMapper.mapToPhoneDto(phoneRepository.saveAndFlush(phone));
                }).orElseThrow(() -> new ResourceNotFoundException("No phone found with id '" + phone_id + "'"));
    }

    @Override
    @Transactional
    public void deletePhone(final UUID id, final UUID phone_id) throws DataAccessException {
        Assert(id, phone_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Deleting phone with id '" + phone_id + "'");

        phoneRepository.deleteById(phone_id);
    }

    @Override
    @Transactional(readOnly = true)
    public PhoneDto findPhoneById(final UUID id) {
        Assert(id);

        LOG.debug("Retrieving phone with id '" + id + "'");

        return phoneRepository.findById(id)
                .map(phoneMapper::mapToPhoneDto)
                .orElseThrow(() -> new ResourceNotFoundException("No phone found with id '" + id + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<PhoneDto> findPhonesByPersonId(final UUID id) {
        Assert.notNull(id, "Person id must not be null!");

        LOG.debug("Retrieving phones with person id '" + id + "'");

        return phoneMapperHelper(Optional.of(phoneRepository.findPhonesByPersonUuid(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<PhoneDto> findPhonesByUsername(final String username) {
        Assert.notNull(username, "Username must not be null!");

        LOG.debug("Retrieving phones for username '" + username + "'");

        return phoneMapperHelper(Optional.of(phoneRepository.findPhoneByPersonUsername(username)));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<PhoneDto> findPhonesByEmail(final String email) {
        Assert.notNull(email, "Email must not be null!");

        LOG.debug("Retrieving phone for email '" + email + "'");

        return phoneMapperHelper(Optional.of(phoneRepository.findPhoneByPersonEmail(email)));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<PhoneDto> findPhonesByUsernameCol(final Set<String> usernames) {
        Assert.notNull(usernames, "Usernames must not be null!");

        usernames.forEach(username -> LOG.debug("Retrieving phones for username '" + username + "'"));

        return phoneMapperHelper(Optional.of(phoneRepository.findPhonesByPersonUsernameIn(usernames)));
    }

    private PhoneDto saveAndReturnDto(final Person person, final PhoneDto resource) {
        Phone phone = phoneMapper.mapToPhone(resource);
        phone.setPhone(stripWhitespaces(phone.getPhone()));
        person.addPhone(phone);
        return phoneMapper.mapToPhoneDto(phoneRepository.saveAndFlush(phone));
    }

    private Set<PhoneDto> phoneMapperHelper(Optional<Set<Phone>> phones) {
        return phones.map(phoneMapper::mapToPhoneDtoSet)
                .orElseThrow(() -> new ResourceNotFoundException("No phones found!"));
    }

    private void Assert(final UUID id) {
        Assert.notNull(id, "Phone id must not be null!");
    }

    private void Assert(final UUID id, final UUID phone_id) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(phone_id, "Phone id must not be null!");
    }

    private void Assert(final UUID id, final PhoneDto resource) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(resource, "Phone Dto resource must not be null!");
    }

    private void Assert(final UUID id, final UUID phone_id, final PhoneDto resource) {
        Assert(id, resource);
        Assert.notNull(phone_id, "Phone id must not be null!");
    }
}
