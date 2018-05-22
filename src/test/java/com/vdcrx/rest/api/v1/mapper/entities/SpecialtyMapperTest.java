package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.SpecialtyDto;
import com.vdcrx.rest.domain.entities.Specialty;
import com.vdcrx.rest.domain.entities.Veterinarian;
import com.vdcrx.rest.domain.entities.VeterinarianSpecialty;
import com.vdcrx.rest.domain.enums.VetSpecialtyType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpecialtyMapperTest.SpecialtyMapperTestConfig.class)
public class SpecialtyMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = SpecialtyMapperTest.class)
    public static class SpecialtyMapperTestConfig {}

    @Autowired
    private SpecialtyMapper specialtyMapper;

    private final Veterinarian veterinarian = new Veterinarian();
    private final VetSpecialtyType vetSpecialtyType = VetSpecialtyType.ANIMAL_BEHAVIOR;
    private final VetSpecialtyType vetSpecialtyType1 = VetSpecialtyType.DENTISTRY;
    private final VetSpecialtyType vetSpecialtyType2 = VetSpecialtyType.DERMATOLOGY;

    @Test
    public void mapToSpecialtyDto() {
        // Given
        Specialty source = new VeterinarianSpecialty();
        ((VeterinarianSpecialty) source).setVetSpecialtyType(vetSpecialtyType);

        // When
        SpecialtyDto target = specialtyMapper.mapToSpecialtyDto(source);

        // Then
        assertNotNull(target);
        assertEquals(VetSpecialtyType.ANIMAL_BEHAVIOR.name(), target.getSpecialty());
    }

    @Test
    public void mapToSpecialty() {

        SpecialtyDto source = new SpecialtyDto();
        source.setSpecialty(vetSpecialtyType1.name());

        Specialty target = specialtyMapper.mapToSpecialty(veterinarian, source);

        assertNotNull(target);
        assertEquals(VetSpecialtyType.DENTISTRY, ((VeterinarianSpecialty) target).getVetSpecialtyType());
    }

    @Test
    public void mapToSpecialty1() {

        SpecialtyDto source = new SpecialtyDto();
        source.setSpecialty(vetSpecialtyType2.name());

        Specialty target = specialtyMapper.mapToSpecialty(source);

        assertNotNull(target);
        assertEquals(VetSpecialtyType.DERMATOLOGY, ((VeterinarianSpecialty) target).getVetSpecialtyType());

    }
}