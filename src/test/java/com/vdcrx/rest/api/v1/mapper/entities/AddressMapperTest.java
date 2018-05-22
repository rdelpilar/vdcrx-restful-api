package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.AddressDto;
import com.vdcrx.rest.domain.entities.Address;
import com.vdcrx.rest.domain.enums.AddressType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AddressMapperTest.AddressMapperTestConfig.class)
public class AddressMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = AddressMapperTest.class)
    public static class AddressMapperTestConfig {}

    @Autowired
    private AddressMapper addressMapper;

    private final UUID id = UUID.randomUUID();
    private final String address1 = "1212 Los Angeles Blvd.";
    private final String address2 = "713";
    private final String city = "Los Angeles";
    private final String state = "CA";
    private final String postalCode = "90210";
    private final AddressType addressType = AddressType.MAILING;


    @Test
    public void mapToAddressDto() {

        // Given
        Address source = new Address();
        source.setAddress1(address1);
        source.setAddress2(address2);
        source.setCity(city);
        source.setState(state);
        source.setPostalCode(postalCode);
        source.setAddressType(addressType);
        source.setId(id);

        // When
        AddressDto target = addressMapper.mapToAddressDto(source);

        // Then
        assertNotNull(target);
        assertEquals("1212 Los Angeles Blvd.", target.getAddress1());
        assertEquals("713", target.getAddress2());
        assertEquals("Los Angeles", target.getCity());
        assertEquals("CA", target.getState());
        assertEquals("90210", target.getPostalCode());
        assertEquals(AddressType.MAILING, target.getAddressType());
        assertEquals(id, target.getId());
    }

    @Test
    public void mapToAddress() {

        // Given
        AddressDto source = new AddressDto();
        source.setAddress1(address1);
        source.setAddress2(address2);
        source.setCity(city);
        source.setState(state);
        source.setPostalCode(postalCode);
        source.setAddressType(addressType);
        source.setId(id);

        // When
        Address target = addressMapper.mapToAddress(source);

        // Then
        assertEquals("1212 Los Angeles Blvd.", target.getAddress1());
        assertEquals("713", target.getAddress2());
        assertEquals("Los Angeles", target.getCity());
        assertEquals("CA", target.getState());
        assertEquals("90210", target.getPostalCode());
        assertEquals(AddressType.MAILING, target.getAddressType());
        assertEquals(id, target.getId());
    }

    @Test
    public void mapToAddressDtoSet() {

        // Given
        Address source = new Address();
        source.setAddress1(address1);
        source.setAddress2(address2);
        source.setCity(city);
        source.setState(state);
        source.setPostalCode(postalCode);
        source.setAddressType(addressType);
        source.setId(id);

        Set<Address> addresses = new HashSet<>();
        addresses.add(source);

        // When
        List<AddressDto> target = addressMapper.mapToAddressDtoList(addresses);

        // Then
        assertNotNull(target);

        addresses.forEach(address3 -> {
            assertEquals("1212 Los Angeles Blvd.", address3.getAddress1());
            assertEquals("713", address3.getAddress2());
            assertEquals("Los Angeles", address3.getCity());
            assertEquals("CA", address3.getState());
            assertEquals("90210", address3.getPostalCode());
            assertEquals(AddressType.MAILING, address3.getAddressType());
            assertEquals(id, address3.getId());
        });
    }
}