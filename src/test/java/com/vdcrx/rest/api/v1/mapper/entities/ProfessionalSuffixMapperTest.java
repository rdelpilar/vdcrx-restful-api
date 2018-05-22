package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.ProfessionalSuffixDto;
import com.vdcrx.rest.domain.entities.ProfessionalSuffix;
import com.vdcrx.rest.domain.enums.ProfessionalSuffixType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProfessionalSuffixMapperTest.ProfessionalSuffixMapperTestConfig.class)
public class ProfessionalSuffixMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = ProfessionalSuffixMapperTest.class)
    public static class ProfessionalSuffixMapperTestConfig {}

    @Autowired
    private ProfessionalSuffixMapper professionalSuffixMapper;

    private final ProfessionalSuffixType suffixType = ProfessionalSuffixType.DVM;
    private final ProfessionalSuffixType dtoSuffixType = ProfessionalSuffixType.MD;

    @Test
    public void mapToProfessionalSuffixDto() {
        // Given
        ProfessionalSuffix source = new ProfessionalSuffix();
        source.setProfessionalSuffixType(suffixType);

        // When
        ProfessionalSuffixDto target = professionalSuffixMapper.mapToProfessionalSuffixDto(source);

        // Then
        assertNotNull(target);
        assertEquals(ProfessionalSuffixType.DVM, target.getProfessionalSuffixType());
    }

    @Test
    public void mapToProfessionalSuffix() {

        ProfessionalSuffixDto source = new ProfessionalSuffixDto();
        source.setProfessionalSuffixType(dtoSuffixType);

        ProfessionalSuffix target = professionalSuffixMapper.mapToProfessionalSuffix(source);

        assertNotNull(target);
        assertEquals(ProfessionalSuffixType.MD, target.getProfessionalSuffixType());
    }
}