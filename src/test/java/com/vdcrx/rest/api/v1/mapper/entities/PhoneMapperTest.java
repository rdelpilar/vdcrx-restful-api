package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.PhoneDto;
import com.vdcrx.rest.domain.entities.Phone;
import com.vdcrx.rest.domain.enums.PhoneType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PhoneMapperTest.PhoneMapperTestConfig.class)
public class PhoneMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = PhoneMapperTest.class)
    public static class PhoneMapperTestConfig {}

    @Autowired
    private PhoneMapper phoneMapper;

    private final PhoneType mobile = PhoneType.MOBILE;
    private final PhoneType fax = PhoneType.FAX;
    private final String mobileNo = "4152226543";
    private final String faxNo = "2133331212";
    private final String phoneExt = "4434";

    @Test
    public void mapToPhoneDto() {
        // Given
        Phone source = new Phone();
        source.setPhone(mobileNo);
        source.setPhoneExt(phoneExt);
        source.setPhoneType(mobile);

        // When
        PhoneDto target = phoneMapper.mapToPhoneDto(source);

        // Then
        assertNotNull(target);
        assertEquals("4152226543", target.getPhone());
        assertEquals("4434", target.getPhoneExt());
        assertEquals(PhoneType.MOBILE, target.getPhoneType());
    }

    @Test
    public void mapToPhone() {

        PhoneDto source = new PhoneDto();
        source.setPhone(faxNo);
        source.setPhoneExt(null);
        source.setPhoneType(fax);

        Phone target = phoneMapper.mapToPhone(source);

        assertNotNull(target);
        assertEquals("2133331212", target.getPhone());
        assertNull(target.getPhoneExt());
        assertEquals(fax, target.getPhoneType());
    }
}