package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.FacilityDto;
import com.vdcrx.rest.domain.entities.Facility;
import com.vdcrx.rest.domain.enums.FacilityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FacilityMapperTest.FacilityMapperTestConfig.class)
public class FacilityMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = FacilityMapperTest.class)
    public static class FacilityMapperTestConfig {}

    @Autowired
    private FacilityMapper facilityMapper;

    private final FacilityType facilityType = FacilityType.HOSPITAL;
    private final String name = "Calbee's Dog Hospital";
    private final FacilityType facilityType1 = FacilityType.CLINIC;
    private final String name1 = "Calbee's Dog Clinic";

    @Test
    public void mapToFacilityDto() {
        // Given
        Facility source = new Facility();
        source.setFacilityType(facilityType);
        source.setName(name);

        // When
        FacilityDto target = facilityMapper.mapToFacilityDto(source);

        // Then
        assertNotNull(target);
        assertEquals(FacilityType.HOSPITAL, target.getFacilityType());
        assertEquals("Calbee's Dog Hospital", target.getName());
    }

    @Test
    public void mapToFacility() {

        FacilityDto source = new FacilityDto();
        source.setFacilityType(facilityType1);
        source.setName(name1);

        Facility target = facilityMapper.mapToFacility(source);

        assertNotNull(target);
        assertEquals("Calbee's Dog Clinic", target.getName());
        assertEquals(FacilityType.CLINIC, target.getFacilityType());
    }
}