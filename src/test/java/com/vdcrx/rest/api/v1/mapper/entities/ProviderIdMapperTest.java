package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.ProviderIdDto;
import com.vdcrx.rest.domain.entities.ProviderId;
import com.vdcrx.rest.domain.enums.ProviderIdType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProviderIdMapperTest.ProviderIdMapperTestConfig.class)
public class ProviderIdMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = ProviderIdMapperTest.class)
    public static class ProviderIdMapperTestConfig {}

    @Autowired
    private ProviderIdMapper providerIdMapper;

    private final String DEA = "DEA0000000";
    private final String ME = "ME123456987";
    private final String NPI = "NPI1237654";
    private final ProviderIdType type = ProviderIdType.VETERINARIAN;

    private final String DEA1 = "DEA1111111";
    private final String ME1 = "ME222222222";
    private final String NPI1 = "NPI1111111";
    private final ProviderIdType type1 = ProviderIdType.PHYSICIAN;

    @Test
    public void mapToProviderIdDto() {
        //Given
        ProviderId source = new ProviderId();
        source.setMe(ME);
        source.setDea(DEA);
        source.setNpi(NPI);
        source.setProviderIdType(type);

        // When
        ProviderIdDto target = providerIdMapper.mapToProviderIdDto(source);

        // Then
        assertNotNull(target);
        assertEquals("ME123456987", target.getMe());
        assertEquals("DEA0000000", target.getDea());
        assertEquals("NPI1237654", target.getNpi());
        assertEquals(ProviderIdType.VETERINARIAN, target.getProviderIdType());
    }

    @Test
    public void mapToProviderId() {

        ProviderIdDto source = new ProviderIdDto();
        source.setMe(ME1);
        source.setDea(DEA1);
        source.setNpi(NPI1);
        source.setProviderIdType(type1);

        ProviderId target = providerIdMapper.mapToProviderId(source);

        assertNotNull(target);
        assertEquals("DEA1111111", target.getDea());
        assertEquals("ME222222222", target.getMe());
        assertEquals("NPI1111111", target.getNpi());
        assertEquals(ProviderIdType.PHYSICIAN, target.getProviderIdType());
    }
}