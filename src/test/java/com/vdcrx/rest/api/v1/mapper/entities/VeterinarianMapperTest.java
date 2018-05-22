package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.*;
import com.vdcrx.rest.domain.entities.*;
import com.vdcrx.rest.domain.enums.ProfessionalSuffixType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = VeterinarianMapperTest.VeterinarianMapperTestConfig.class)
public class VeterinarianMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = VeterinarianMapperTest.class)
    public static class VeterinarianMapperTestConfig {
        @Autowired
        public static PasswordEncoder passwordEncoder;
    }

    @Autowired
    private VeterinarianMapper veterinarianMapper;

    private final String username = "peter";
    private final String firstName = "Peter";
    private final String middleName = "";
    private final String lastName = "McTavish";
    private final ProfessionalSuffix suffix = new ProfessionalSuffix(ProfessionalSuffixType.DVM);
    private final String email = "peter@gmail.com";
    private final Phone phone = new Phone();
    private final Address address = new Address();
    private final Facility facility = new Facility();
    private final Specialty specialty = new VeterinarianSpecialty();

    private final UUID id = UUID.randomUUID();
    private final String username1 = "susan";
    private final String firstName1 = "Susan";
    private final String middleName1 = "Happy";
    private final String lastName1 = "Enriquez";
    private final Set<ProfessionalSuffixDto> suffixes = new HashSet<>();
    private final String email1 = "susan@gmail.com";
    private final Set<PhoneDto> phones = new HashSet<>();
    private final Set<AddressDto> addresses = new HashSet<>();
    private final Set<FacilityDto> facilities = new HashSet<>();
    private final Set<SpecialtyDto> specialties = new HashSet<>();

    @Test
    public void mapToVeterinarian() {

        // Given
        Veterinarian source = new Veterinarian();
        source.setUsername(username);
        source.setFirstName(firstName);
        source.setMiddleName(middleName);
        source.setLastName(lastName);
        source.setEmail(email);
        source.addProfessionalSuffix(suffix);
        source.addPhone(phone);
        source.addAddress(address);
        source.addFacility(facility);
        source.addSpecialty(specialty);

        // When
        VetDto target = veterinarianMapper.mapToVetDto(source);

        // Then
        assertNotNull(target);
        assertEquals("peter", target.getUsername());
        assertEquals("Peter", target.getFirstName());
        assertEquals("", target.getMiddleName());
        assertEquals("McTavish", target.getLastName());
        assertNotNull(target.getSuffixes());
        assertEquals("peter@gmail.com", target.getEmail());
        assertNotNull(target.getPhones());
        assertNotNull(target.getAddresses());
        assertNotNull(target.getFacilities());
        assertNotNull(target.getSpecialties());
    }

    @Test
    public void mapToVetDto() {

        VetDto source = new VetDto();
        source.setUsername(username1);
        source.setFirstName(firstName1);
        source.setMiddleName(middleName1);
        source.setLastName(lastName1);
        source.setSuffixes(suffixes);
        source.setEmail(email1);
        source.setPhones(phones);
        source.setAddresses(addresses);
        source.setFacilities(facilities);
        source.setSpecialties(specialties);
        source.setId(id);

        Veterinarian target = veterinarianMapper.mapToVeterinarian(source);

        assertNotNull(target);
        assertEquals("susan", target.getUsername());
        assertEquals("Susan", target.getFirstName());
        assertEquals("Happy", target.getMiddleName());
        assertEquals("Enriquez", target.getLastName());
        assertNotNull(target.getSuffixes());
        assertNotNull("susan@gmail.com", target.getEmail());
        assertNotNull(target.getPhones());
        assertNotNull(target.getAddresses());
        assertNotNull(target.getFacilities());
        assertNotNull(target.getSpecialties());
        assertNotNull(target.getId());
    }

    @Test
    public void mapToPersonBasicDto() {

        Veterinarian source = new Veterinarian();
        source.setUsername(username);
        source.setFirstName(firstName);
        source.setMiddleName(middleName);
        source.setLastName(lastName);
        source.setEmail(email);
        source.addProfessionalSuffix(suffix);
        source.addPhone(phone);
        source.addAddress(address);
        source.addFacility(facility);
        source.addSpecialty(specialty);

        PersonBasicDto target = veterinarianMapper.mapToPersonBasicDto(source);

        assertNotNull(target);
        assertEquals("Peter", target.getFirstName());
        assertEquals("", target.getMiddleName());
        assertEquals("McTavish", target.getLastName());
    }
}