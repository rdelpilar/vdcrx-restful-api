package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.api.v1.model.dto.SignatureDto;
import com.vdcrx.rest.domain.entities.Signature;
import com.vdcrx.rest.domain.enums.SignatureType;
import com.vdcrx.rest.api.utils.SignatureTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URISyntaxException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SignatureMapperTest.SignatureMapperTestConfig.class)
public class SignatureMapperTest {

    @Configuration
    @ComponentScan(basePackageClasses = SignatureMapperTest.class)
    public static class SignatureMapperTestConfig {

        @Bean
        SignatureTestHelper getSignatureTestHelper() throws URISyntaxException {
            return new SignatureTestHelper();
        }
    }

    @Autowired
    private SignatureMapper signatureMapper;

    @Autowired
    private SignatureTestHelper signatureTestHelper;

    private static byte bytes [] = null;
    private final String contentType = "jpeg";
    private final SignatureType type = SignatureType.VETERINARIAN;
    private final SignatureType type1 = SignatureType.PHYSICIAN;

    @Before
    public void setUp() {
        bytes = signatureTestHelper.getBytes();
    }

    @Test
    public void mapToSignatureDto() {
        // Given
        Signature source = new Signature();
        source.setSignatureType(type);
        source.setContentType(contentType);
        source.setSize(bytes.length);
        source.setImage(bytes);

        // When
        SignatureDto target = signatureMapper.mapToSignatureDto(source);

        // Then
        assertNotNull(target);
        assertEquals(SignatureType.VETERINARIAN, target.getSignatureType());
        assertEquals(bytes.length, target.getSize());
        assertEquals("jpeg", target.getContentType());
        assertArrayEquals(bytes, target.getImage());
    }

    @Test
    public void mapToSignature() {

        SignatureDto source = new SignatureDto();
        source.setSignatureType(type1);
        source.setContentType(contentType);
        source.setSize(bytes.length);
        source.setImage(bytes);

        Signature target = signatureMapper.mapToSignature(source);

        assertNotNull(target);
        assertEquals(SignatureType.PHYSICIAN, target.getSignatureType());
        assertEquals(bytes.length, target.getSize());
        assertEquals("jpeg", target.getContentType());
        assertArrayEquals(bytes, target.getImage());
    }
}