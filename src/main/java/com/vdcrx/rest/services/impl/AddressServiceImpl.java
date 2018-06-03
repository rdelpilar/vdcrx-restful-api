package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.api.v1.mapper.entities.AddressMapper;
import com.vdcrx.rest.api.v1.model.dto.AddressDto;
import com.vdcrx.rest.domain.entities.Address;
import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.domain.enums.AddressType;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.repositories.AddressRepository;
import com.vdcrx.rest.repositories.PersonRepository;
import com.vdcrx.rest.services.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Address service implementation
 *
 * @author Ranel del Pilar
 */

@Service
public class AddressServiceImpl implements AddressService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final PersonRepository personRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository,
                              AddressMapper addressMapper,
                              PersonRepository personRepository) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public AddressDto createAddress(final UUID id, final AddressDto resource) throws DataAccessException {
        Assert(id, resource);

        LOG.debug("Creating new address for person with id '" + id + "'");

        return personRepository
                .findById(id)
                .map(person ->
                    saveAndReturnDto(person, resource)
                ).orElseThrow(() -> new ResourceNotFoundException("No person found with id '" + id + "'"));
    }

    @Override
    @Transactional
    public AddressDto updateAddress(final UUID id, final UUID address_id, final AddressDto resource) throws DataAccessException {
        Assert(id, address_id, resource);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Updating address with id '" + address_id + "'");

        return addressRepository.findById(address_id)
                .map(address -> {
                    String address1 = resource.getAddress1();
                    String address2 = resource.getAddress2();
                    String city = resource.getCity();
                    String state = resource.getState();
                    String postalCode = resource.getPostalCode();
                    AddressType addressType = resource.getAddressType();

                    if(address1 != null && !address1.equals(address.getAddress1()))
                        address.setAddress1(address1);
                    if(address2 != null && !address2.equals(address.getAddress2()))
                        address.setAddress2(address2);
                    if(city != null && !city.equals(address.getCity()))
                        address.setCity(city);
                    if(state != null && !state.equals(address.getState()))
                        address.setState(state);
                    if(postalCode != null && !postalCode.equals(address.getPostalCode()))
                        address.setPostalCode(postalCode);
                    if(addressType != null && !addressType.name().equals(address.getAddressType().name()))
                        address.setAddressType(addressType);

                    return addressMapper.mapToAddressDto(addressRepository.saveAndFlush(address));
                }).orElseThrow(() -> new ResourceNotFoundException("No address found with id '" + address_id + "'"));
    }

    @Override
    @Transactional
    public void deleteAddress(final UUID id, final UUID address_id) throws DataAccessException {
        Assert(id, address_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Deleting address with id '" + address_id + "'");

        addressRepository.deleteById(address_id);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDto findAddressById(final UUID id, final UUID address_id) {
        Assert(id, address_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Retrieving address with id '" + address_id + "'");

        return addressRepository.findById(address_id)
                .map(addressMapper::mapToAddressDto)
                .orElseThrow(() -> new ResourceNotFoundException("No address found with id '" + address_id + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDto> findAddressesByPersonId(final UUID id) {
        Assert.notNull(id, "Person id must not be null!");

        LOG.debug("Retrieving addresses with person id '" + id + "'");

        Set<Address> addresses = addressRepository.findAddressesByPersonId(id);

        AssertCollection(addresses, id.toString());

        return addressMapperHelper(addresses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDto> findAddressByUsername(final String username) {
        Assert.notNull(username, "Username must not be null!");

        LOG.debug("Retrieving addresses for username '" + username + "'");

        Set<Address> addresses = addressRepository.findAddressesByPersonUsername(username);

        AssertCollection(addresses, username);

        return addressMapperHelper(addresses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDto> findAddressesByEmail(final String email) {
        Assert.notNull(email, "Email must not be null!");

        LOG.debug("Retrieving addresses for email '" + email + "'");

        Set<Address> addresses = addressRepository.findAddressesByPersonEmail(email);

        AssertCollection(addresses, email);

        return addressMapperHelper(addresses);
    }

    private AddressDto saveAndReturnDto(final Person person, final AddressDto dto) {
        Address address = addressMapper.mapToAddress(dto);
        person.addAddress(address);
        return addressMapper.mapToAddressDto(addressRepository.saveAndFlush(address));
    }

    private List<AddressDto> addressMapperHelper(final Set<Address> addresses) {
        return addresses
                .stream()
                .map(addressMapper::mapToAddressDto)
                .sorted(Comparator.comparing(AddressDto::getAddressType))
                .collect(Collectors.toList());
    }

    private void Assert(final UUID id, final UUID address_id) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(address_id, "Address id must not be null!");
    }

    private void Assert(final UUID id, final AddressDto resource) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(resource, "Address DTO resource must not be null!");
    }

    private void Assert(final UUID id, final UUID address_id, final AddressDto resource) {
        Assert(id, resource);
        Assert.notNull(address_id, "Address id must not be null!");
    }

    private void AssertCollection(final Collection<?> collection, final String param) {
        if(collection.isEmpty())
            throw new ResourceNotFoundException("No address found using '" + param + "'");
    }
}
