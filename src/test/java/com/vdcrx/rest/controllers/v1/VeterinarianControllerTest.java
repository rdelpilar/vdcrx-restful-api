package com.vdcrx.rest.controllers.v1;

import com.vdcrx.rest.api.utils.SignatureTestHelper;
import com.vdcrx.rest.api.v1.model.dto.*;
import com.vdcrx.rest.domain.enums.*;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.exceptions.controller_advice.RestResponseExceptionHandler;
import com.vdcrx.rest.services.VeterinarianService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URISyntaxException;
import java.util.*;

import static com.vdcrx.rest.constants.ApiConstants.VET_BASE_PATH;
import static com.vdcrx.rest.constants.ApiConstants.VET_SIGNUP_PATH;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VeterinarianControllerTest extends AbstractRestControllerTest {

    @Mock
    private VeterinarianService veterinarianService;

    @InjectMocks
    private VeterinarianController veterinarianController;

    private MockMvc mockMvc;

    private UUID id;
    private VetDto source = new VetDto();
    private VetDto target = new VetDto();
    private AddressDto address = new AddressDto();
    private PhoneDto phone = new PhoneDto();
    private FacilityDto facility = new FacilityDto();
    private ProfessionalSuffixDto suffix = new ProfessionalSuffixDto();
    private ProviderIdDto providerId = new ProviderIdDto();
    private SignatureDto signature = new SignatureDto();
    private SpecialtyDto specialty = new SpecialtyDto();
    private byte bytes [] = null;

    @Before
    public void setUp() throws URISyntaxException {

        id = UUID.randomUUID();

        source.setId(id);
        source.setUsername("harold");
        source.setPlainTextPassword("MyPassword@1");
        source.setFirstName("Harold");
        source.setLastName("Davis");
        source.setEmail("harold@gmail.com");

        phone.setId(UUID.randomUUID());
        phone.setPhoneType(PhoneType.MOBILE);
        phone.setPhone("4154151234");
        phone.setPhoneExt(null);

        Set<PhoneDto> phones = new HashSet<>();
        phones.add(phone);

        source.setPhones(phones);

        address.setId(UUID.randomUUID());
        address.setAddress1("325 Sharon Park Dr");
        address.setAddress2("713");
        address.setCity("Menlo Park");
        address.setState("CA");
        address.setPostalCode("94025");
        address.setAddressType(AddressType.MAILING);

        Set<AddressDto> addresses = new HashSet<>();
        addresses.add(address);

        source.setAddresses(addresses);

        facility.setId(UUID.randomUUID());
        facility.setFacilityType(FacilityType.HOSPITAL);
        facility.setName("Calbee's Pet Clinic");

        Set<FacilityDto> facilities = new HashSet<>();
        facilities.add(facility);

        source.setFacilities(facilities);

        suffix.setId(UUID.randomUUID());
        suffix.setProfessionalSuffixType(ProfessionalSuffixType.DVM);

        Set<ProfessionalSuffixDto> suffixes = new HashSet<>();
        suffixes.add(suffix);

        source.setSuffixes(suffixes);

        providerId.setId(UUID.randomUUID());
        providerId.setProviderIdType(ProviderIdType.VETERINARIAN);
        providerId.setDea("DEA0000000");
        providerId.setMe("ME123456987");
        providerId.setNpi("NPI1237654");

        source.setProviderId(providerId);

        SignatureTestHelper signatureTestHelper = new SignatureTestHelper();
        bytes  = signatureTestHelper.getBytes();

        signature.setId(UUID.randomUUID());
        signature.setSignatureType(SignatureType.VETERINARIAN);
        signature.setSize(bytes.length);
        signature.setContentType("jpeg");
        signature.setImage(bytes);

        source.setSignature(signature);

        specialty.setId(UUID.randomUUID());
        specialty.setSpecialty(VetSpecialtyType.ANIMAL_BEHAVIOR.name());

        Set<SpecialtyDto> specialties = new HashSet<>();
        specialties.add(specialty);

        source.setSpecialties(specialties);


        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(veterinarianController)
                .setControllerAdvice(new RestResponseExceptionHandler())
                .build();

        target.setId(id);
        target.setUsername(source.getUsername());
        target.setPlainTextPassword(source.getPlainTextPassword());
        target.setFirstName(source.getFirstName());
        target.setMiddleName(source.getMiddleName());
        target.setLastName(source.getLastName());
        target.setEmail(source.getEmail());
        target.setPhones(source.getPhones());
        target.setAddresses(source.getAddresses());
        target.setFacilities(source.getFacilities());
        target.setSuffixes(source.getSuffixes());
        target.setProviderId(source.getProviderId());
        target.setSignature(source.getSignature());
        target.setSpecialties(source.getSpecialties());
    }

    @Test
    public void createVeterinarian() throws Exception {

        // When
        when(veterinarianService.createVeterinarian(any(VetDto.class))).thenReturn(target);

        // Then
        mockMvc.perform(post(VET_SIGNUP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(source)))
                .andExpect(jsonPath("$.id", equalTo(id.toString())))
                .andExpect(jsonPath("$.username", equalTo("harold")))
                .andExpect(jsonPath("$.plainTextPassword", equalTo("MyPassword@1")))
                .andExpect(jsonPath("$.firstName", equalTo("Harold")))
                .andExpect(jsonPath("$.lastName", equalTo("Davis")))
                .andExpect(jsonPath("$.middleName", equalTo(null)))
                .andExpect(jsonPath("$.email", equalTo("harold@gmail.com")))

                .andExpect(jsonPath("$.addresses[0].address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$.addresses[0].address2", equalTo("713")))
                .andExpect(jsonPath("$.addresses[0].city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$.addresses[0].state", equalTo("CA")))
                .andExpect(jsonPath("$.addresses[0].postalCode", equalTo("94025")))
                .andExpect(jsonPath("$.addresses[0].addressType", equalTo("MAILING")))

                .andExpect(jsonPath("$.phones[0].phone", equalTo("4154151234")))
                .andExpect(jsonPath("$.phones[0].phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$.phones[0].phoneExt", equalTo(null)))

                .andExpect(jsonPath("$.facilities[0].facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$.facilities[0].name", equalTo("Calbee's Pet Clinic")))

                .andExpect(jsonPath("$.suffixes[0].professionalSuffixType", equalTo("DVM")))

                .andExpect(jsonPath("$.providerId.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.providerId.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.providerId.npi", equalTo("NPI1237654")))

                .andExpect(jsonPath("$.signature.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.signature.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signature.image", equalTo(new String(Base64.encodeBase64(bytes)))))

                .andExpect(jsonPath("$.specialties[0].specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))

                .andExpect(status().isCreated())
                .andDo(print());

        ArgumentCaptor<VetDto> vetDtoArg = ArgumentCaptor.forClass(VetDto.class);
        verify(veterinarianService, times(1)).createVeterinarian(vetDtoArg.capture());
        verifyNoMoreInteractions(veterinarianService);

        VetDto formObj = vetDtoArg.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(source.getId()));
        assertThat(formObj.getUsername(), is(source.getUsername()));
        assertThat(formObj.getPlainTextPassword(), is(source.getPlainTextPassword()));
        assertThat(formObj.getFirstName(), is(source.getFirstName()));
        assertNull(formObj.getMiddleName());
        assertThat(formObj.getLastName(), is(source.getLastName()));
        assertThat(formObj.getEmail(), is(source.getEmail()));
        assertNotNull(formObj.getAddresses());
        assertNotNull(formObj.getPhones());
        assertNotNull(formObj.getFacilities());
        assertNotNull(formObj.getSuffixes());
        assertNotNull(formObj.getFacilities());
        assertNotNull(formObj.getProviderId());
        assertNotNull(formObj.getSpecialties());
    }

    @Test
    public void updateBasicInfo() throws Exception {

        UUID id = UUID.randomUUID();
        String firstName = "Peter";
        String middleName = "";
        String lastName = "McTavish";

        PersonBasicDto source = new PersonBasicDto();
        source.setFirstName(firstName);
        source.setMiddleName(middleName);
        source.setLastName(lastName);
        source.setId(id);

        PersonBasicDto target = new PersonBasicDto();
        target.setId(source.getId());
        target.setFirstName(source.getFirstName());
        target.setMiddleName(source.getMiddleName());
        target.setLastName(source.getLastName());

        when(veterinarianService.updateVeterinarianBasicInfo(any(UUID.class), any(PersonBasicDto.class))).thenReturn(target);

        mockMvc.perform(patch(VET_BASE_PATH + "/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(source)))
                .andExpect(jsonPath("$.id", equalTo(id.toString())))
                .andExpect(jsonPath("$.firstName", equalTo("Peter")))
                .andExpect(jsonPath("$.middleName", equalTo("")))
                .andExpect(jsonPath("$.lastName", equalTo("McTavish")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<PersonBasicDto> personBasic = ArgumentCaptor.forClass(PersonBasicDto.class);
        verify(veterinarianService, times(1)).updateVeterinarianBasicInfo(personId.capture(), personBasic.capture());
        verifyNoMoreInteractions(veterinarianService);

        PersonBasicDto formObj = personBasic.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(source.getId()));
        assertThat(formObj.getFirstName(), is(source.getFirstName()));
        assertThat(formObj.getMiddleName(), is(source.getMiddleName()));
        assertThat(formObj.getLastName(), is(source.getLastName()));
    }

    @Test
    public void findVeterinarianById_FAIL() throws Exception {

        when(veterinarianService.findOne(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No veterinarian found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/id/{id}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).findOne(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianById_SUCCESS() throws Exception {

        when(veterinarianService.findOne(any(UUID.class))).thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/id/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(source)))
                .andExpect(jsonPath("$.id", equalTo(id.toString())))
                .andExpect(jsonPath("$.username", equalTo("harold")))
                .andExpect(jsonPath("$.plainTextPassword", equalTo("MyPassword@1")))
                .andExpect(jsonPath("$.firstName", equalTo("Harold")))
                .andExpect(jsonPath("$.lastName", equalTo("Davis")))
                .andExpect(jsonPath("$.middleName", equalTo(null)))
                .andExpect(jsonPath("$.email", equalTo("harold@gmail.com")))

                .andExpect(jsonPath("$.addresses[0].address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$.addresses[0].address2", equalTo("713")))
                .andExpect(jsonPath("$.addresses[0].city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$.addresses[0].state", equalTo("CA")))
                .andExpect(jsonPath("$.addresses[0].postalCode", equalTo("94025")))
                .andExpect(jsonPath("$.addresses[0].addressType", equalTo("MAILING")))

                .andExpect(jsonPath("$.phones[0].phone", equalTo("4154151234")))
                .andExpect(jsonPath("$.phones[0].phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$.phones[0].phoneExt", equalTo(null)))

                .andExpect(jsonPath("$.facilities[0].facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$.facilities[0].name", equalTo("Calbee's Pet Clinic")))

                .andExpect(jsonPath("$.suffixes[0].professionalSuffixType", equalTo("DVM")))

                .andExpect(jsonPath("$.providerId.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.providerId.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.providerId.npi", equalTo("NPI1237654")))

                .andExpect(jsonPath("$.signature.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.signature.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signature.image", equalTo(new String(Base64.encodeBase64(bytes)))))

                .andExpect(jsonPath("$.specialties[0].specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))

                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).findOne(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianById2_FAIL() throws Exception {

        when(veterinarianService.findById(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No veterinarian found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/id2/{id}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).findById(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianById2_SUCCESS() throws Exception {

        when(veterinarianService.findById(any(UUID.class))).thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/id2/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(source)))
                .andExpect(jsonPath("$.id", equalTo(id.toString())))
                .andExpect(jsonPath("$.username", equalTo("harold")))
                .andExpect(jsonPath("$.plainTextPassword", equalTo("MyPassword@1")))
                .andExpect(jsonPath("$.firstName", equalTo("Harold")))
                .andExpect(jsonPath("$.lastName", equalTo("Davis")))
                .andExpect(jsonPath("$.middleName", equalTo(null)))
                .andExpect(jsonPath("$.email", equalTo("harold@gmail.com")))

                .andExpect(jsonPath("$.addresses[0].address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$.addresses[0].address2", equalTo("713")))
                .andExpect(jsonPath("$.addresses[0].city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$.addresses[0].state", equalTo("CA")))
                .andExpect(jsonPath("$.addresses[0].postalCode", equalTo("94025")))
                .andExpect(jsonPath("$.addresses[0].addressType", equalTo("MAILING")))

                .andExpect(jsonPath("$.phones[0].phone", equalTo("4154151234")))
                .andExpect(jsonPath("$.phones[0].phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$.phones[0].phoneExt", equalTo(null)))

                .andExpect(jsonPath("$.facilities[0].facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$.facilities[0].name", equalTo("Calbee's Pet Clinic")))

                .andExpect(jsonPath("$.suffixes[0].professionalSuffixType", equalTo("DVM")))

                .andExpect(jsonPath("$.providerId.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.providerId.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.providerId.npi", equalTo("NPI1237654")))

                .andExpect(jsonPath("$.signature.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.signature.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signature.image", equalTo(new String(Base64.encodeBase64(bytes)))))

                .andExpect(jsonPath("$.specialties[0].specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))

                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).findById(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianByUsername_FAIL() throws Exception {

        when(veterinarianService.findByUsername(any(String.class)))
                .thenThrow(new ResourceNotFoundException("Veterinarian not found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/username/{username}", "wrongusername"))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> username = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByUsername(username.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianByUsername_SUCCESS() throws Exception {

        when(veterinarianService.findByUsername(any(String.class))).thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/username/{username}", "harold")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(source)))
                .andExpect(jsonPath("$.id", equalTo(id.toString())))
                .andExpect(jsonPath("$.username", equalTo("harold")))
                .andExpect(jsonPath("$.plainTextPassword", equalTo("MyPassword@1")))
                .andExpect(jsonPath("$.firstName", equalTo("Harold")))
                .andExpect(jsonPath("$.lastName", equalTo("Davis")))
                .andExpect(jsonPath("$.middleName", equalTo(null)))
                .andExpect(jsonPath("$.email", equalTo("harold@gmail.com")))

                .andExpect(jsonPath("$.addresses[0].address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$.addresses[0].address2", equalTo("713")))
                .andExpect(jsonPath("$.addresses[0].city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$.addresses[0].state", equalTo("CA")))
                .andExpect(jsonPath("$.addresses[0].postalCode", equalTo("94025")))
                .andExpect(jsonPath("$.addresses[0].addressType", equalTo("MAILING")))

                .andExpect(jsonPath("$.phones[0].phone", equalTo("4154151234")))
                .andExpect(jsonPath("$.phones[0].phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$.phones[0].phoneExt", equalTo(null)))

                .andExpect(jsonPath("$.facilities[0].facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$.facilities[0].name", equalTo("Calbee's Pet Clinic")))

                .andExpect(jsonPath("$.suffixes[0].professionalSuffixType", equalTo("DVM")))

                .andExpect(jsonPath("$.providerId.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.providerId.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.providerId.npi", equalTo("NPI1237654")))

                .andExpect(jsonPath("$.signature.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.signature.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signature.image", equalTo(new String(Base64.encodeBase64(bytes)))))

                .andExpect(jsonPath("$.specialties[0].specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))

                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> username = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByUsername(username.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianByEmail_FAIL() throws Exception {

        when(veterinarianService.findByEmail(any(String.class)))
                .thenThrow(new ResourceNotFoundException("Veterinarian not found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/email/{email}", "invalid@email.com"))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> email = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByEmail(email.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianByEmail_SUCCESS() throws Exception {

        when(veterinarianService.findByEmail(any(String.class))).thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/email/{email}", "harold@gmailcom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(source)))
                .andExpect(jsonPath("$.id", equalTo(id.toString())))
                .andExpect(jsonPath("$.username", equalTo("harold")))
                .andExpect(jsonPath("$.plainTextPassword", equalTo("MyPassword@1")))
                .andExpect(jsonPath("$.firstName", equalTo("Harold")))
                .andExpect(jsonPath("$.lastName", equalTo("Davis")))
                .andExpect(jsonPath("$.middleName", equalTo(null)))
                .andExpect(jsonPath("$.email", equalTo("harold@gmail.com")))

                .andExpect(jsonPath("$.addresses[0].address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$.addresses[0].address2", equalTo("713")))
                .andExpect(jsonPath("$.addresses[0].city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$.addresses[0].state", equalTo("CA")))
                .andExpect(jsonPath("$.addresses[0].postalCode", equalTo("94025")))
                .andExpect(jsonPath("$.addresses[0].addressType", equalTo("MAILING")))

                .andExpect(jsonPath("$.phones[0].phone", equalTo("4154151234")))
                .andExpect(jsonPath("$.phones[0].phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$.phones[0].phoneExt", equalTo(null)))

                .andExpect(jsonPath("$.facilities[0].facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$.facilities[0].name", equalTo("Calbee's Pet Clinic")))

                .andExpect(jsonPath("$.suffixes[0].professionalSuffixType", equalTo("DVM")))

                .andExpect(jsonPath("$.providerId.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.providerId.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.providerId.npi", equalTo("NPI1237654")))

                .andExpect(jsonPath("$.signature.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.signature.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signature.image", equalTo(new String(Base64.encodeBase64(bytes)))))

                .andExpect(jsonPath("$.specialties[0].specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))

                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> email = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByEmail(email.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinariansByPhone_FAIL() throws Exception {

        when(veterinarianService.findByPhone(any(String.class)))
                .thenThrow(new ResourceNotFoundException("Veterinarian not found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/phone/{phone}", "5555555555"))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> email = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByPhone(email.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinariansByPhone_SUCCESS() throws Exception {

        List<VetDto> list = new ArrayList<>();
        list.add(target);

        when(veterinarianService.findByPhone(any(String.class))).thenReturn(list);

        mockMvc.perform(get(VET_BASE_PATH + "/phone/{phone}", "4155553312")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(source)))
                .andExpect(jsonPath("$[0].id", equalTo(id.toString())))
                .andExpect(jsonPath("$[0].username", equalTo("harold")))
                .andExpect(jsonPath("$[0].plainTextPassword", equalTo("MyPassword@1")))
                .andExpect(jsonPath("$[0].firstName", equalTo("Harold")))
                .andExpect(jsonPath("$[0].lastName", equalTo("Davis")))
                .andExpect(jsonPath("$[0].middleName", equalTo(null)))
                .andExpect(jsonPath("$[0].email", equalTo("harold@gmail.com")))

                .andExpect(jsonPath("$[0].addresses[0].address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$[0].addresses[0].address2", equalTo("713")))
                .andExpect(jsonPath("$[0].addresses[0].city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$[0].addresses[0].state", equalTo("CA")))
                .andExpect(jsonPath("$[0].addresses[0].postalCode", equalTo("94025")))
                .andExpect(jsonPath("$[0].addresses[0].addressType", equalTo("MAILING")))

                .andExpect(jsonPath("$[0].phones[0].phone", equalTo("4154151234")))
                .andExpect(jsonPath("$[0].phones[0].phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$[0].phones[0].phoneExt", equalTo(null)))

                .andExpect(jsonPath("$[0].facilities[0].facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$[0].facilities[0].name", equalTo("Calbee's Pet Clinic")))

                .andExpect(jsonPath("$[0].suffixes[0].professionalSuffixType", equalTo("DVM")))

                .andExpect(jsonPath("$[0].providerId.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$[0].providerId.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$[0].providerId.npi", equalTo("NPI1237654")))

                .andExpect(jsonPath("$[0].signature.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$[0].signature.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$[0].signature.image", equalTo(new String(Base64.encodeBase64(bytes)))))

                .andExpect(jsonPath("$[0].specialties[0].specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))

                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> phone = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByPhone(phone.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianByDEA_FAIL() throws Exception {

        when(veterinarianService.findByDEA(any(String.class)))
                .thenThrow(new ResourceNotFoundException("Veterinarian not found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/dea/{dea}", "DEA0000000"))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> dea = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByDEA(dea.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianByDEA_SUCCESS() throws Exception {

        when(veterinarianService.findByDEA(any(String.class))).thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/dea/{dea}", "DEA0000000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(source)))
                .andExpect(jsonPath("$.id", equalTo(id.toString())))
                .andExpect(jsonPath("$.username", equalTo("harold")))
                .andExpect(jsonPath("$.plainTextPassword", equalTo("MyPassword@1")))
                .andExpect(jsonPath("$.firstName", equalTo("Harold")))
                .andExpect(jsonPath("$.lastName", equalTo("Davis")))
                .andExpect(jsonPath("$.middleName", equalTo(null)))
                .andExpect(jsonPath("$.email", equalTo("harold@gmail.com")))

                .andExpect(jsonPath("$.addresses[0].address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$.addresses[0].address2", equalTo("713")))
                .andExpect(jsonPath("$.addresses[0].city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$.addresses[0].state", equalTo("CA")))
                .andExpect(jsonPath("$.addresses[0].postalCode", equalTo("94025")))
                .andExpect(jsonPath("$.addresses[0].addressType", equalTo("MAILING")))

                .andExpect(jsonPath("$.phones[0].phone", equalTo("4154151234")))
                .andExpect(jsonPath("$.phones[0].phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$.phones[0].phoneExt", equalTo(null)))

                .andExpect(jsonPath("$.facilities[0].facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$.facilities[0].name", equalTo("Calbee's Pet Clinic")))

                .andExpect(jsonPath("$.suffixes[0].professionalSuffixType", equalTo("DVM")))

                .andExpect(jsonPath("$.providerId.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.providerId.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.providerId.npi", equalTo("NPI1237654")))

                .andExpect(jsonPath("$.signature.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.signature.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signature.image", equalTo(new String(Base64.encodeBase64(bytes)))))

                .andExpect(jsonPath("$.specialties[0].specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))

                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> dea = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByDEA(dea.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianByME_FAIL() throws Exception {

        when(veterinarianService.findByME(any(String.class)))
                .thenThrow(new ResourceNotFoundException("Veterinarian not found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/me/{me}", "ME123456987"))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> me = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByME(me.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianByME_SUCCESS() throws Exception {

        when(veterinarianService.findByME(any(String.class))).thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/me/{me}", "ME123456987")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(source)))
                .andExpect(jsonPath("$.id", equalTo(id.toString())))
                .andExpect(jsonPath("$.username", equalTo("harold")))
                .andExpect(jsonPath("$.plainTextPassword", equalTo("MyPassword@1")))
                .andExpect(jsonPath("$.firstName", equalTo("Harold")))
                .andExpect(jsonPath("$.lastName", equalTo("Davis")))
                .andExpect(jsonPath("$.middleName", equalTo(null)))
                .andExpect(jsonPath("$.email", equalTo("harold@gmail.com")))

                .andExpect(jsonPath("$.addresses[0].address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$.addresses[0].address2", equalTo("713")))
                .andExpect(jsonPath("$.addresses[0].city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$.addresses[0].state", equalTo("CA")))
                .andExpect(jsonPath("$.addresses[0].postalCode", equalTo("94025")))
                .andExpect(jsonPath("$.addresses[0].addressType", equalTo("MAILING")))

                .andExpect(jsonPath("$.phones[0].phone", equalTo("4154151234")))
                .andExpect(jsonPath("$.phones[0].phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$.phones[0].phoneExt", equalTo(null)))

                .andExpect(jsonPath("$.facilities[0].facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$.facilities[0].name", equalTo("Calbee's Pet Clinic")))

                .andExpect(jsonPath("$.suffixes[0].professionalSuffixType", equalTo("DVM")))

                .andExpect(jsonPath("$.providerId.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.providerId.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.providerId.npi", equalTo("NPI1237654")))

                .andExpect(jsonPath("$.signature.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.signature.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signature.image", equalTo(new String(Base64.encodeBase64(bytes)))))

                .andExpect(jsonPath("$.specialties[0].specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))

                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> me = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByME(me.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianByNPI_FAIL() throws Exception {

        when(veterinarianService.findByNPI(any(String.class)))
                .thenThrow(new ResourceNotFoundException("Veterinarian not found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/npi/{npi}", "NPI1237654"))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> npi = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByNPI(npi.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findVeterinarianByNPI_SUCCESS() throws Exception {

        when(veterinarianService.findByNPI(any(String.class))).thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/npi/{npi}", "NPI1237654")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(source)))
                .andExpect(jsonPath("$.id", equalTo(id.toString())))
                .andExpect(jsonPath("$.username", equalTo("harold")))
                .andExpect(jsonPath("$.plainTextPassword", equalTo("MyPassword@1")))
                .andExpect(jsonPath("$.firstName", equalTo("Harold")))
                .andExpect(jsonPath("$.lastName", equalTo("Davis")))
                .andExpect(jsonPath("$.middleName", equalTo(null)))
                .andExpect(jsonPath("$.email", equalTo("harold@gmail.com")))

                .andExpect(jsonPath("$.addresses[0].address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$.addresses[0].address2", equalTo("713")))
                .andExpect(jsonPath("$.addresses[0].city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$.addresses[0].state", equalTo("CA")))
                .andExpect(jsonPath("$.addresses[0].postalCode", equalTo("94025")))
                .andExpect(jsonPath("$.addresses[0].addressType", equalTo("MAILING")))

                .andExpect(jsonPath("$.phones[0].phone", equalTo("4154151234")))
                .andExpect(jsonPath("$.phones[0].phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$.phones[0].phoneExt", equalTo(null)))

                .andExpect(jsonPath("$.facilities[0].facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$.facilities[0].name", equalTo("Calbee's Pet Clinic")))

                .andExpect(jsonPath("$.suffixes[0].professionalSuffixType", equalTo("DVM")))

                .andExpect(jsonPath("$.providerId.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.providerId.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.providerId.npi", equalTo("NPI1237654")))

                .andExpect(jsonPath("$.signature.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.signature.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signature.image", equalTo(new String(Base64.encodeBase64(bytes)))))

                .andExpect(jsonPath("$.specialties[0].specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))

                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<String> npi = ArgumentCaptor.forClass(String.class);
        verify(veterinarianService, times(1)).findByNPI(npi.capture());
        verifyZeroInteractions(veterinarianService);
    }

    // TODO
    @Test
    public void getAll() throws Exception {

    }

    // TODO
    @Test
    public void getListOfVeterinarians() throws Exception {

    }

    // TODO
    @Test
    public void getVeterinariansByLastNameIsStartingWith() throws Exception {
    }

    // TODO
    @Test
    public void getVeterinariansByFirstNameIsStartingWith() throws Exception {
    }

    @Test
    public void createAddress() throws Exception {

        // When
        when(veterinarianService.createAddress(any(UUID.class), any(AddressDto.class))).thenReturn(address);

        // Then
        mockMvc.perform(post(VET_BASE_PATH + "/{id}/address", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(address)))
                .andExpect(jsonPath("$.address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$.address2", equalTo("713")))
                .andExpect(jsonPath("$.city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$.state", equalTo("CA")))
                .andExpect(jsonPath("$.postalCode", equalTo("94025")))
                .andExpect(jsonPath("$.addressType", equalTo("MAILING")))

                .andExpect(status().isCreated())
                .andDo(print());

        ArgumentCaptor<UUID> addressId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<AddressDto> addressDto = ArgumentCaptor.forClass(AddressDto.class);
        verify(veterinarianService, times(1)).createAddress(addressId.capture(), addressDto.capture());
        verifyNoMoreInteractions(veterinarianService);

        AddressDto formObj = addressDto.getValue();

        assertThat(formObj.getId(), is(address.getId()));
        assertThat(formObj.getAddress1(), is(address.getAddress1()));
        assertThat(formObj.getAddress2(), is(address.getAddress2()));
        assertThat(formObj.getCity(), is(address.getCity()));
        assertThat(formObj.getState(), is(address.getState()));
        assertThat(formObj.getPostalCode(), is(address.getPostalCode()));
        assertThat(formObj.getAddressType(), is(address.getAddressType()));
    }

    @Test
    public void updateAddress() throws Exception {

        UUID id = UUID.randomUUID();
        AddressDto target = new AddressDto();
        target.setId(address.getId());
        target.setAddress1(address.getAddress1());
        target.setAddress2(address.getAddress2());
        target.setCity(address.getCity());
        target.setState(address.getState());
        target.setPostalCode(address.getPostalCode());
        target.setAddressType(address.getAddressType());

        when(veterinarianService.updateAddress(any(UUID.class), any(UUID.class),any(AddressDto.class)))
                .thenReturn(target);

        mockMvc.perform(patch(VET_BASE_PATH + "/{id}/address/{address_id}", id, target.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(address)))
                .andExpect(jsonPath("$.address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$.address2", equalTo("713")))
                .andExpect(jsonPath("$.city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$.state", equalTo("CA")))
                .andExpect(jsonPath("$.postalCode", equalTo("94025")))
                .andExpect(jsonPath("$.addressType", equalTo("MAILING")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> addressId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<AddressDto> addressDto = ArgumentCaptor.forClass(AddressDto.class);
        verify(veterinarianService, times(1))
                .updateAddress(personId.capture(), addressId.capture(), addressDto.capture());
        verifyNoMoreInteractions(veterinarianService);

        AddressDto formObj = addressDto.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(address.getId()));
        assertThat(formObj.getAddressType(), is(address.getAddressType()));
        assertThat(formObj.getAddress1(), is(address.getAddress1()));
        assertThat(formObj.getAddress2(), is(address.getAddress2()));
        assertThat(formObj.getCity(), is(address.getCity()));
        assertThat(formObj.getState(), is(address.getState()));
        assertThat(formObj.getPostalCode(), is(address.getPostalCode()));
    }

    @Test
    public void deleteAddress() throws Exception {

        UUID personId = UUID.randomUUID();
        UUID addressId = address.getId();

        doNothing().when(veterinarianService).deleteAddress(any(UUID.class), any(UUID.class));

        mockMvc.perform(delete(VET_BASE_PATH + "/{id}/address/{address_id}", personId, addressId))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> addressId1 = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).deleteAddress(personId1.capture(), addressId1.capture());
        verifyZeroInteractions(veterinarianService);

    }

    @Test
    public void findAddressById_FAIL() throws Exception  {

        when(veterinarianService.findAddressById(any(UUID.class), any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No address found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/address/{address_id}", id, UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> addressId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).findAddressById(personId.capture(), addressId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findAddressById_SUCCESS() throws Exception  {

        UUID id = UUID.randomUUID();
        AddressDto target = new AddressDto();
        target.setId(address.getId());
        target.setAddress1(address.getAddress1());
        target.setAddress2(address.getAddress2());
        target.setCity(address.getCity());
        target.setState(address.getState());
        target.setPostalCode(address.getPostalCode());
        target.setAddressType(address.getAddressType());

        when(veterinarianService.findAddressById(any(UUID.class), any(UUID.class)))
                .thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/address/{address_id}", id, address.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(address)))
                .andExpect(jsonPath("$.address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$.address2", equalTo("713")))
                .andExpect(jsonPath("$.city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$.state", equalTo("CA")))
                .andExpect(jsonPath("$.postalCode", equalTo("94025")))
                .andExpect(jsonPath("$.addressType", equalTo("MAILING")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> addressId = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findAddressById(personId.capture(), addressId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findAddressesByPersonId_FAIL() throws Exception {

        when(veterinarianService.findAddressesByPersonId(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No address found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/addresses", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).findAddressesByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findAddressesByPersonId_SUCCESS() throws Exception {

        List<AddressDto> list = new ArrayList<>();
        list.add(address);

        when(veterinarianService.findAddressesByPersonId(any(UUID.class))).thenReturn(list);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/addresses", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(address)))
                .andExpect(jsonPath("$[0].id", equalTo(address.getId().toString())))
                .andExpect(jsonPath("$[0].address1", equalTo("325 Sharon Park Dr")))
                .andExpect(jsonPath("$[0].address2", equalTo("713")))
                .andExpect(jsonPath("$[0].city", equalTo("Menlo Park")))
                .andExpect(jsonPath("$[0].state", equalTo("CA")))
                .andExpect(jsonPath("$[0].postalCode", equalTo("94025")))
                .andExpect(jsonPath("$[0].addressType", equalTo("MAILING")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).findAddressesByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void createPhone() throws Exception {

        when(veterinarianService.createPhone(any(UUID.class), any(PhoneDto.class))).thenReturn(phone);

        mockMvc.perform(post(VET_BASE_PATH + "/{id}/phone", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(phone)))
                .andExpect(jsonPath("$.id", equalTo(phone.getId().toString())))
                .andExpect(jsonPath("$.phone", equalTo("4154151234")))
                .andExpect(jsonPath("$.phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$.phoneExt", equalTo(null)))
                .andExpect(status().isCreated())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<PhoneDto> resource = ArgumentCaptor.forClass(PhoneDto.class);
        verify(veterinarianService, times(1)).createPhone(personId.capture(), resource.capture());
        verifyNoMoreInteractions(veterinarianService);

        PhoneDto formObj = resource.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(phone.getId()));
        assertThat(formObj.getPhoneType(), is(phone.getPhoneType()));
        assertThat(formObj.getPhone(), is(phone.getPhone()));
        assertThat(formObj.getPhoneExt(), is(phone.getPhoneExt()));
    }

    @Test
    public void updatePhone() throws Exception {

        UUID id = UUID.randomUUID();
        PhoneDto target = new PhoneDto();
        target.setId(phone.getId());
        target.setPhoneType(phone.getPhoneType());
        target.setPhone(phone.getPhone());
        target.setPhoneExt(phone.getPhoneExt());

        when(veterinarianService.updatePhone(any(UUID.class), any(UUID.class), any(PhoneDto.class)))
                .thenReturn(target);

        mockMvc.perform(patch(VET_BASE_PATH + "/{id}/phone/{phone_id}", id, target.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(phone)))
                .andExpect(jsonPath("$.id", equalTo(phone.getId().toString())))
                .andExpect(jsonPath("$.phone", equalTo("4154151234")))
                .andExpect(jsonPath("$.phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$.phoneExt", equalTo(null)))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> phoneId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<PhoneDto> phoneDto = ArgumentCaptor.forClass(PhoneDto.class);
        verify(veterinarianService, times(1))
                .updatePhone(personId.capture(), phoneId.capture(), phoneDto.capture());
        verifyNoMoreInteractions(veterinarianService);

        PhoneDto formObj = phoneDto.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(phone.getId()));
        assertThat(formObj.getPhoneType(), is(phone.getPhoneType()));
        assertThat(formObj.getPhone(), is(phone.getPhone()));
        assertThat(formObj.getPhoneExt(), is(phone.getPhoneExt()));
    }

    @Test
    public void deletePhone() throws Exception {

        UUID personId = UUID.randomUUID();
        UUID phoneId = phone.getId();

        doNothing().when(veterinarianService).deletePhone(any(UUID.class), any(UUID.class));

        mockMvc.perform(delete(VET_BASE_PATH + "/{id}/phone/{phone_id}", personId, phoneId))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> phoneId1 = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).deletePhone(personId1.capture(), phoneId1.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findPhoneById_FAIL() throws Exception {

        when(veterinarianService.findPhoneById(any(UUID.class), any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No phone found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/phone/{phone_id}", id, UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> phoneID = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findPhoneById(personId.capture(), phoneID.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findPhoneById_SUCCESS() throws Exception {

        UUID id = UUID.randomUUID();
        PhoneDto target = new PhoneDto();
        target.setId(phone.getId());
        target.setPhoneType(phone.getPhoneType());
        target.setPhone(phone.getPhone());
        target.setPhoneExt(phone.getPhoneExt());

        when(veterinarianService.findPhoneById(any(UUID.class), any(UUID.class)))
                .thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/phone/{phone_id}", id, phone.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(phone)))
                .andExpect(jsonPath("$.id", equalTo(phone.getId().toString())))
                .andExpect(jsonPath("$.phone", equalTo("4154151234")))
                .andExpect(jsonPath("$.phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$.phoneExt", equalTo(null)))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> phoneId = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findPhoneById(personId.capture(), phoneId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findPhonesByPersonId_FAIL() throws Exception {

        when(veterinarianService.findPhonesByPersonId(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No phone found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/phones", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1))
                .findPhonesByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findPhonesByPersonId_SUCCESS() throws Exception {

        List<PhoneDto> list = new ArrayList<>();
        list.add(phone);

        when(veterinarianService.findPhonesByPersonId(any(UUID.class))).thenReturn(list);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/phones", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(phone)))
                .andExpect(jsonPath("$[0].id", equalTo(phone.getId().toString())))
                .andExpect(jsonPath("$[0].phone", equalTo("4154151234")))
                .andExpect(jsonPath("$[0].phoneType", equalTo("MOBILE")))
                .andExpect(jsonPath("$[0].phoneExt", equalTo(null)))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).findPhonesByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void createProviderIdentifier() throws Exception {

        when(veterinarianService.createProviderIdentifier(any(UUID.class), any(ProviderIdDto.class)))
                .thenReturn(providerId);

        mockMvc.perform(post(VET_BASE_PATH + "/{id}/provider_identifier", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(providerId)))
                .andExpect((jsonPath("$.id", equalTo(providerId.getId().toString()))))
                .andExpect(jsonPath("$.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.npi", equalTo("NPI1237654")))
                .andExpect(jsonPath("$.providerIdType", equalTo("VETERINARIAN")))
                .andExpect(status().isCreated())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<ProviderIdDto> resource = ArgumentCaptor.forClass(ProviderIdDto.class);
        verify(veterinarianService, times(1)).createProviderIdentifier(personId.capture(), resource.capture());
        verifyNoMoreInteractions(veterinarianService);

        ProviderIdDto formObj = resource.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(providerId.getId()));
        assertThat(formObj.getProviderIdType(), is(providerId.getProviderIdType()));
        assertThat(formObj.getDea(), is(providerId.getDea()));
        assertThat(formObj.getMe(), is(providerId.getMe()));
        assertThat(formObj.getNpi(), is(providerId.getNpi()));
    }

    @Test
    public void updateProviderIdentifier() throws Exception {

        UUID id = UUID.randomUUID();
        ProviderIdDto target = new ProviderIdDto();
        target.setId(providerId.getId());
        target.setProviderIdType(providerId.getProviderIdType());
        target.setDea(providerId.getDea());
        target.setMe(providerId.getMe());
        target.setNpi(providerId.getNpi());

        when(veterinarianService.updateProviderIdentifier(any(UUID.class), any(UUID.class), any(ProviderIdDto.class)))
                .thenReturn(target);

        mockMvc.perform(patch(VET_BASE_PATH + "/{id}/provider_identifier/{provider_id}", id, target.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(providerId)))
                .andExpect((jsonPath("$.id", equalTo(providerId.getId().toString()))))
                .andExpect(jsonPath("$.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.npi", equalTo("NPI1237654")))
                .andExpect(jsonPath("$.providerIdType", equalTo("VETERINARIAN")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> providerId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<ProviderIdDto> providerDto1 = ArgumentCaptor.forClass(ProviderIdDto.class);
        verify(veterinarianService, times(1))
                .updateProviderIdentifier(personId1.capture(), providerId1.capture(), providerDto1.capture());
        verifyNoMoreInteractions(veterinarianService);

        ProviderIdDto formObj = providerDto1.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(providerId.getId()));
        assertThat(formObj.getProviderIdType(), is(providerId.getProviderIdType()));
        assertThat(formObj.getDea(), is(providerId.getDea()));
        assertThat(formObj.getMe(), is(providerId.getMe()));
        assertThat(formObj.getNpi(), is(providerId.getNpi()));
    }

    @Test
    public void deleteProvider() throws Exception {

        UUID personId = UUID.randomUUID();
        UUID providerIdId = providerId.getId();

        doNothing().when(veterinarianService).deleteProviderIdentifier(any(UUID.class), any(UUID.class));

        mockMvc.perform(delete(VET_BASE_PATH + "/{id}/provider_identifier/{provider_id}", personId, providerIdId))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> providerId1 = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1))
                .deleteProviderIdentifier(personId1.capture(), providerId1.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findProviderIdentifierById_FAIL() throws Exception {

        when(veterinarianService.findProviderIdById(any(UUID.class), any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No provider identifier found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/provider_identifier/{provider_id}", id, UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> providerId1 = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findProviderIdById(personId.capture(), providerId1.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findProviderIdentifierById_SUCCESS() throws Exception {

        UUID id = UUID.randomUUID();
        ProviderIdDto target = new ProviderIdDto();
        target.setId(providerId.getId());
        target.setProviderIdType(providerId.getProviderIdType());
        target.setDea(providerId.getDea());
        target.setMe(providerId.getMe());
        target.setNpi(providerId.getNpi());

        when(veterinarianService.findProviderIdById(any(UUID.class), any(UUID.class)))
                .thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/provider_identifier/{provider_id}", id, providerId.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(providerId)))
                .andExpect((jsonPath("$.id", equalTo(providerId.getId().toString()))))
                .andExpect(jsonPath("$.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.npi", equalTo("NPI1237654")))
                .andExpect(jsonPath("$.providerIdType", equalTo("VETERINARIAN")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> providerId1 = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findProviderIdById(personId.capture(), providerId1.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findProviderIdentifierByPersonId_FAIL() throws Exception {

        when(veterinarianService.findProviderIdByPersonId(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No provider identifier found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/provider_identifier", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).findProviderIdByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findProviderIdentifierByPersonId_SUCCESS() throws Exception {

        when(veterinarianService.findProviderIdByPersonId(any(UUID.class)))
                .thenReturn(providerId);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/provider_identifier", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(providerId)))
                .andExpect((jsonPath("$.id", equalTo(providerId.getId().toString()))))
                .andExpect(jsonPath("$.dea", equalTo("DEA0000000")))
                .andExpect(jsonPath("$.me", equalTo("ME123456987")))
                .andExpect(jsonPath("$.npi", equalTo("NPI1237654")))
                .andExpect(jsonPath("$.providerIdType", equalTo("VETERINARIAN")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1)).findProviderIdByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void createFacility() throws Exception {

        when(veterinarianService.createFacility(any(UUID.class), any(FacilityDto.class)))
                .thenReturn(facility);

        mockMvc.perform(post(VET_BASE_PATH + "/{id}/facility", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(facility)))
                .andExpect(jsonPath("$.id", equalTo(facility.getId().toString())))
                .andExpect(jsonPath("$.facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$.name", equalTo("Calbee's Pet Clinic")))
                .andExpect(status().isCreated())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<FacilityDto> resource = ArgumentCaptor.forClass(FacilityDto.class);
        verify(veterinarianService, times(1)).createFacility(personId.capture(), resource.capture());
        verifyNoMoreInteractions(veterinarianService);

        FacilityDto formObj = resource.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(facility.getId()));
        assertThat(formObj.getFacilityType(), is(facility.getFacilityType()));
        assertThat(formObj.getName(), is(facility.getName()));
    }

    @Test
    public void updateFacility() throws Exception {

        UUID id = UUID.randomUUID();
        FacilityDto target = new FacilityDto();
        target.setId(facility.getId());
        target.setFacilityType(facility.getFacilityType());
        target.setName(facility.getName());

        when(veterinarianService.updateFacility(any(UUID.class), any(UUID.class), any(FacilityDto.class)))
                .thenReturn(target);

        mockMvc.perform(patch(VET_BASE_PATH + "/{id}/facility/{facility_id}", id, target.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(facility)))
                .andExpect(jsonPath("$.id", equalTo(facility.getId().toString())))
                .andExpect(jsonPath("$.facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$.name", equalTo("Calbee's Pet Clinic")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> facilityId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<FacilityDto> facilityDto1 = ArgumentCaptor.forClass(FacilityDto.class);
        verify(veterinarianService, times(1))
                .updateFacility(personId1.capture(), facilityId.capture(), facilityDto1.capture());
        verifyNoMoreInteractions(veterinarianService);

        FacilityDto formObj = facilityDto1.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(facility.getId()));
        assertThat(formObj.getFacilityType(), is(facility.getFacilityType()));
        assertThat(formObj.getName(), is(facility.getName()));
    }

    @Test
    public void deleteFacility() throws Exception {

        UUID personId = UUID.randomUUID();
        UUID facilityId = facility.getId();

        doNothing().when(veterinarianService).deleteFacility(any(UUID.class), any(UUID.class));

        mockMvc.perform(delete(VET_BASE_PATH + "/{id}/facility/{facility_id}", personId, facilityId))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> facilityId1 = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .deleteFacility(personId1.capture(), facilityId1.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findFacilityById_FAIL() throws Exception {

        when(veterinarianService.findFacilityById(any(UUID.class), any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No facility found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/facility/{facility_id}", id, UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> facilityId1 = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findFacilityById(personId.capture(), facilityId1.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findFacilityById_SUCCESS() throws Exception {

        UUID id = UUID.randomUUID();
        FacilityDto target = new FacilityDto();
        target.setId(facility.getId());
        target.setFacilityType(facility.getFacilityType());
        target.setName(facility.getName());

        when(veterinarianService.findFacilityById(any(UUID.class), any(UUID.class)))
                .thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/facility/{facility_id}", id, facility.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(facility)))
                .andExpect(jsonPath("$.id", equalTo(facility.getId().toString())))
                .andExpect(jsonPath("$.facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$.name", equalTo("Calbee's Pet Clinic")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> facilityId1 = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findFacilityById(personId.capture(), facilityId1.capture());
        verifyZeroInteractions(veterinarianService);
    }


    @Test
    public void findFacilitiesByPersonId_FAIL() throws Exception {

        when(veterinarianService.findFacilitiesByPersonId(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No facility found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/facilities", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1))
                .findFacilitiesByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findFacilitiesByPersonId_SUCCESS() throws Exception {

        List<FacilityDto> list = new ArrayList<>();
        list.add(facility);

        when(veterinarianService.findFacilitiesByPersonId(any(UUID.class)))
                .thenReturn(list);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/facilities", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(facility)))
                .andExpect(jsonPath("$[0].id", equalTo(facility.getId().toString())))
                .andExpect(jsonPath("$[0].facilityType", equalTo("HOSPITAL")))
                .andExpect(jsonPath("$[0].name", equalTo("Calbee's Pet Clinic")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1))
                .findFacilitiesByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void createSpecialty() throws Exception {

        when(veterinarianService.createSpecialty(any(UUID.class), any(SpecialtyDto.class)))
                .thenReturn(specialty);

        mockMvc.perform(post(VET_BASE_PATH + "/{id}/specialty", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(specialty)))
                .andExpect(jsonPath("$.id", equalTo(specialty.getId().toString())))
                .andExpect(jsonPath("$.specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))
                .andExpect(status().isCreated())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<SpecialtyDto> resource = ArgumentCaptor.forClass(SpecialtyDto.class);
        verify(veterinarianService, times(1))
                .createSpecialty(personId.capture(), resource.capture());
        verifyNoMoreInteractions(veterinarianService);

        SpecialtyDto formObj = resource.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(specialty.getId()));
        assertThat(formObj.getSpecialty(), is(specialty.getSpecialty()));
    }

    @Test
    public void updateSpecialty() throws Exception {

        UUID id = UUID.randomUUID();
        SpecialtyDto target = new SpecialtyDto();
        target.setId(specialty.getId());
        target.setSpecialty(specialty.getSpecialty());

        when(veterinarianService.updateSpecialty(any(UUID.class), any(UUID.class), any(SpecialtyDto.class)))
                .thenReturn(target);

        mockMvc.perform(patch(VET_BASE_PATH + "/{id}/specialty/{specialty_id}", id, target.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(specialty)))
                .andExpect(jsonPath("$.id", equalTo(specialty.getId().toString())))
                .andExpect(jsonPath("$.specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> specialtyId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<SpecialtyDto> specialtyDto = ArgumentCaptor.forClass(SpecialtyDto.class);
        verify(veterinarianService, times(1))
                .updateSpecialty(personId.capture(), specialtyId.capture(), specialtyDto.capture());
        verifyNoMoreInteractions(veterinarianService);

        SpecialtyDto formObj = specialtyDto.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(specialty.getId()));
        assertThat(formObj.getSpecialty(), is(specialty.getSpecialty()));
    }

    @Test
    public void deleteSpecialty() throws Exception {

        UUID personId = UUID.randomUUID();
        UUID specialtyId = UUID.randomUUID();

        doNothing().when(veterinarianService).deleteSpecialty(any(UUID.class), any(UUID.class));

        mockMvc.perform(delete(VET_BASE_PATH + "/{id}/specialty/{specialty_id}", personId, specialtyId))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> specialtyId1 = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .deleteSpecialty(personId1.capture(), specialtyId1.capture());
        verifyNoMoreInteractions(veterinarianService);
    }

    @Test
    public void findSpecialtyById_FAIL() throws Exception {

        when(veterinarianService.findSpecialtyById(any(UUID.class), any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No specialty found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/specialty/{specialty_id}", id, UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> specialtyId = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findSpecialtyById(personId.capture(), specialtyId.capture());
        verifyNoMoreInteractions(veterinarianService);
    }

    @Test
    public void findSpecialtyById_SUCCESS() throws Exception {

        UUID id = UUID.randomUUID();
        SpecialtyDto target = new SpecialtyDto();
        target.setId(specialty.getId());
        target.setSpecialty(specialty.getSpecialty());

        when(veterinarianService.findSpecialtyById(any(UUID.class), any(UUID.class)))
                .thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/specialty/{specialty_id}", id, specialty.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(specialty)))
                .andExpect(jsonPath("$.id", equalTo(specialty.getId().toString())))
                .andExpect(jsonPath("$.specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> specialtyId = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findSpecialtyById(personId.capture(), specialtyId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findSpecialtiesByPersonId_FAIL() throws Exception {

        when(veterinarianService.findSpecialtiesByPersonId(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No specialty found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/specialties", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1))
                .findSpecialtiesByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findSpecialtiesByPersonId_SUCCESS() throws Exception {
        List<SpecialtyDto> list = new ArrayList<>();
        list.add(specialty);

        when(veterinarianService.findSpecialtiesByPersonId(any(UUID.class)))
                .thenReturn(list);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/specialties", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(specialty)))
                .andExpect(jsonPath("$[0].id", equalTo(specialty.getId().toString())))
                .andExpect(jsonPath("$[0].specialty", equalTo(VetSpecialtyType.ANIMAL_BEHAVIOR.name())))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1))
                .findSpecialtiesByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void createProfessionalSuffix() throws Exception {

        when(veterinarianService.createProfessionalSuffix(any(UUID.class), any(ProfessionalSuffixDto.class)))
                .thenReturn(suffix);

        mockMvc.perform(post(VET_BASE_PATH + "/{id}/professional_suffix", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(suffix)))
                .andExpect(jsonPath("$.id", equalTo(suffix.getId().toString())))
                .andExpect(jsonPath("$.professionalSuffixType", equalTo("DVM")))
                .andExpect(status().isCreated())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<ProfessionalSuffixDto> resource = ArgumentCaptor.forClass(ProfessionalSuffixDto.class);
        verify(veterinarianService, times(1))
                .createProfessionalSuffix(personId.capture(), resource.capture());
        verifyNoMoreInteractions(veterinarianService);

        ProfessionalSuffixDto formObj = resource.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(suffix.getId()));
        assertThat(formObj.getProfessionalSuffixType(), is(suffix.getProfessionalSuffixType()));
    }

    @Test
    public void updateProfessionalSuffix() throws Exception {

        UUID id = UUID.randomUUID();
        ProfessionalSuffixDto target = new ProfessionalSuffixDto();
        target.setId(suffix.getId());
        target.setProfessionalSuffixType(suffix.getProfessionalSuffixType());

        when(veterinarianService.updateProfessionalSuffix(any(UUID.class), any(UUID.class), any(ProfessionalSuffixDto.class)))
                .thenReturn(target);

        mockMvc.perform(patch(VET_BASE_PATH + "/{id}/professional_suffix/{suffix_id}", id, target.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(suffix)))
                .andExpect(jsonPath("$.id", equalTo(suffix.getId().toString())))
                .andExpect(jsonPath("$.professionalSuffixType", equalTo("DVM")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> suffixId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<ProfessionalSuffixDto> resource = ArgumentCaptor.forClass(ProfessionalSuffixDto.class);
        verify(veterinarianService, times(1))
                .updateProfessionalSuffix(personId.capture(), suffixId.capture(), resource.capture());
        verifyNoMoreInteractions(veterinarianService);

        ProfessionalSuffixDto formObj = resource.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(suffix.getId()));
        assertThat(formObj.getProfessionalSuffixType(), is(suffix.getProfessionalSuffixType()));
    }

    @Test
    public void deleteProfessionalSuffix() throws Exception {

        UUID personId = UUID.randomUUID();
        UUID suffixId = suffix.getId();

        doNothing().when(veterinarianService).deleteProfessionalSuffix(any(UUID.class), any(UUID.class));

        mockMvc.perform(delete(VET_BASE_PATH + "/{id}/professional_suffix/{suffix_id}", personId, suffixId))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> suffixId1 = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .deleteProfessionalSuffix(personId1.capture(), suffixId1.capture());

        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findProfessionalSuffixById_FAIL() throws Exception {

        when(veterinarianService.findProfessionalSuffixById(any(UUID.class), any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No professional suffix found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/professional_suffix/{suffix_id}", id, UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> suffixId = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findProfessionalSuffixById(personId.capture(), suffixId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findProfessionalSuffixById_SUCCESS() throws Exception {

        UUID id = UUID.randomUUID();
        ProfessionalSuffixDto target = new ProfessionalSuffixDto();
        target.setId(suffix.getId());
        target.setProfessionalSuffixType(suffix.getProfessionalSuffixType());

        when(veterinarianService.findProfessionalSuffixById(any(UUID.class), any(UUID.class)))
                .thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/professional_suffix/{suffix_id}", id, suffix.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(suffix)))
                .andExpect(jsonPath("$.id", equalTo(suffix.getId().toString())))
                .andExpect(jsonPath("$.professionalSuffixType", equalTo("DVM")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> suffixID = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findProfessionalSuffixById(personId.capture(), suffixID.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findProfessionalSuffixesByPersonId_FAIL() throws Exception {

        when(veterinarianService.findProfessionalSuffixesByPersonId(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No professional suffix found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/professional_suffix", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1))
                .findProfessionalSuffixesByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findProfessionalSuffixesByPersonId_SUCCESS() throws Exception {

        List<ProfessionalSuffixDto> list = new ArrayList<>();
        list.add(suffix);

        when(veterinarianService.findProfessionalSuffixesByPersonId(any(UUID.class)))
                .thenReturn(list);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/professional_suffix", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(suffix)))
                .andExpect(jsonPath("$[0].id", equalTo(suffix.getId().toString())))
                .andExpect(jsonPath("$[0].professionalSuffixType", equalTo("DVM")))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1))
                .findProfessionalSuffixesByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void createSignature() throws Exception {

        when(veterinarianService.createSignature(any(UUID.class), any(SignatureDto.class)))
                .thenReturn(signature);

        mockMvc.perform(post(VET_BASE_PATH + "/{id}/signature", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signature)))
                .andExpect(jsonPath("$.id", equalTo(signature.getId().toString())))
                .andExpect(jsonPath("$.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signatureType", equalTo("VETERINARIAN")))
                .andExpect(jsonPath("$.image", equalTo(new String(Base64.encodeBase64(bytes)))))
                .andExpect(status().isCreated())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<SignatureDto> resource = ArgumentCaptor.forClass(SignatureDto.class);
        verify(veterinarianService, times(1))
                .createSignature(personId.capture(), resource.capture());
        verifyNoMoreInteractions(veterinarianService);

        SignatureDto formObj = resource.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(signature.getId()));
        assertThat(formObj.getSignatureType(), is(signature.getSignatureType()));
        assertThat(formObj.getContentType(), is(signature.getContentType()));
        assertThat(formObj.getSize(), is(signature.getSize()));
        assertThat(formObj.getImage(), is(signature.getImage()));
    }

    @Test
    public void updateSignature() throws Exception {

        UUID id = UUID.randomUUID();
        SignatureDto target = new SignatureDto();
        target.setId(signature.getId());
        target.setSignatureType(signature.getSignatureType());
        target.setContentType(signature.getContentType());
        target.setSize(signature.getSize());
        target.setImage(signature.getImage());

        when(veterinarianService.updateSignature(any(UUID.class), any(UUID.class), any(SignatureDto.class)))
                .thenReturn(target);

        mockMvc.perform(patch(VET_BASE_PATH + "/{id}/signature/{sig_id}", id, target.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signature)))
                .andExpect(jsonPath("$.id", equalTo(signature.getId().toString())))
                .andExpect(jsonPath("$.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signatureType", equalTo("VETERINARIAN")))
                .andExpect(jsonPath("$.image", equalTo(new String(Base64.encodeBase64(bytes)))))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> signatureId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<SignatureDto> resource = ArgumentCaptor.forClass(SignatureDto.class);
        verify(veterinarianService, times(1))
                .updateSignature(personId1.capture(), signatureId1.capture(), resource.capture());
        verifyNoMoreInteractions(veterinarianService);

        SignatureDto formObj = resource.getValue();

        assertNotNull(formObj);
        assertThat(formObj.getId(), is(signature.getId()));
        assertThat(formObj.getSignatureType(), is(signature.getSignatureType()));
        assertThat(formObj.getContentType(), is(signature.getContentType()));
        assertThat(formObj.getSize(), is(signature.getSize()));
        assertThat(formObj.getImage(), is(signature.getImage()));
    }

    @Test
    public void deleteSignature() throws Exception {

        UUID personId = UUID.randomUUID();
        UUID signatureId = signature.getId();

        doNothing().when(veterinarianService).deleteSignature(any(UUID.class), any(UUID.class));

        mockMvc.perform(delete(VET_BASE_PATH + "/{id}/signature/{sig_id}", personId, signatureId))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId1 = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> signatureId1 = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1))
                .deleteSignature(personId1.capture(), signatureId1.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findSignatureById_FAIL() throws Exception {

        when(veterinarianService.findSignatureById(any(UUID.class), any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No signature found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/signature/{sig_id}", id, UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> signatureId = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findSignatureById(personId.capture(), signatureId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findSignatureById_SUCCESS() throws Exception {

        UUID id = UUID.randomUUID();
        SignatureDto target = new SignatureDto();
        target.setId(signature.getId());
        target.setSignatureType(signature.getSignatureType());
        target.setContentType(signature.getContentType());
        target.setSize(signature.getSize());
        target.setImage(signature.getImage());

        when(veterinarianService.findSignatureById(any(UUID.class), any(UUID.class)))
                .thenReturn(target);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/signature/{sig_id}", id, signature.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signature)))
                .andExpect(jsonPath("$.id", equalTo(signature.getId().toString())))
                .andExpect(jsonPath("$.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signatureType", equalTo("VETERINARIAN")))
                .andExpect(jsonPath("$.image", equalTo(new String(Base64.encodeBase64(bytes)))))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> signatureId = ArgumentCaptor.forClass(UUID.class);

        verify(veterinarianService, times(1))
                .findSignatureById(personId.capture(), signatureId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findSignatureDtoByPersonId_FAIL() throws Exception {

        when(veterinarianService.findSignatureByPersonId(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No signature found!"));

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/signature", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1))
                .findSignatureByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);
    }

    @Test
    public void findSignatureDtoByPersonId_SUCCESS() throws Exception {

        when(veterinarianService.findSignatureByPersonId(any(UUID.class)))
                .thenReturn(signature);

        mockMvc.perform(get(VET_BASE_PATH + "/{id}/signature", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signature)))
                .andExpect(jsonPath("$.id", equalTo(signature.getId().toString())))
                .andExpect(jsonPath("$.size", equalTo(bytes.length)))
                .andExpect(jsonPath("$.contentType", equalTo("jpeg")))
                .andExpect(jsonPath("$.signatureType", equalTo("VETERINARIAN")))
                .andExpect(jsonPath("$.image", equalTo(new String(Base64.encodeBase64(bytes)))))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<UUID> personId = ArgumentCaptor.forClass(UUID.class);
        verify(veterinarianService, times(1))
                .findSignatureByPersonId(personId.capture());
        verifyZeroInteractions(veterinarianService);

    }
}