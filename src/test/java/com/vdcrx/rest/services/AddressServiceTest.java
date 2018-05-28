package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.mapper.entities.AddressMapper;
import com.vdcrx.rest.api.v1.model.dto.AddressDto;
import com.vdcrx.rest.domain.entities.Address;
import com.vdcrx.rest.domain.enums.AddressType;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.repositories.AddressRepository;
import com.vdcrx.rest.repositories.PersonRepository;
import com.vdcrx.rest.services.impl.AddressServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    private AddressService addressService;

    @Mock
    private PersonRepository personRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        addressService = new AddressServiceImpl(addressRepository,
                Mappers.getMapper(AddressMapper.class),
                personRepository);
    }

    @Test
    public void createAddress() {
    }

    @Test
    public void updateAddress() {
    }

    @Test
    public void deleteAddress() {
    }

    @Test
    public void findAddressById() {

//        Address address = getAddress1();
//
//        given(addressRepository.findById(any(UUID.class))).willReturn(Optional.of(address));
//
//        AddressDto addressDto = addressService.findAddressById(UUID.randomUUID(), address.getId());
//
//        then(addressRepository).should(times(1)).findById(any(UUID.class));
//
//        assertNotNull(addressDto);
//        assertEquals("325 Sharon Park Dr", addressDto.getAddress1());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findAddressById_NOT_FOUND() {

        Address address = getAddress1();

        given(addressRepository.findById(any(UUID.class))).willReturn(Optional.of(address));

        AddressDto addressDto = addressService.findAddressById(UUID.randomUUID(), address.getId());

        then(addressRepository).should(times(1)).findById(any(UUID.class));
    }

    @Test
    public void findAddressesByPersonId() {
    }

    @Test
    public void findAddressByUsername() {
    }

    @Test
    public void findAddressesByEmail() {
    }

    private Address getAddress1() {
        Address address = new Address();
        address.setId(UUID.randomUUID());
        address.setAddress1("325 Sharon Park Dr");
        address.setAddress2("713");
        address.setCity("Menlo Park");
        address.setState("CA");
        address.setPostalCode("94025");
        address.setAddressType(AddressType.MAILING);

        return address;
    }
}