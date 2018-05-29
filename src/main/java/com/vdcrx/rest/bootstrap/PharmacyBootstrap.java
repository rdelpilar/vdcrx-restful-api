package com.vdcrx.rest.bootstrap;

import com.vdcrx.rest.domain.entities.*;
import com.vdcrx.rest.domain.enums.*;
import com.vdcrx.rest.repositories.PersonRepository;
import com.vdcrx.rest.repositories.ProviderIdRepository;
import com.vdcrx.rest.repositories.SignatureRepository;
import com.vdcrx.rest.services.impl.VeterinarianServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

/**
 * Pharmacy bootstrap
 *
 * @author Ranel del Pilar
 */

@Slf4j
@Component
public class PharmacyBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(VeterinarianServiceImpl.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SignatureRepository signatureRepository;

    @Autowired
    private ProviderIdRepository providerIdRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean alreadySetup = false;
    private int facilityHospitalIndex;
    private int facilityClinicIndex;
    private int phoneIndex;
    private int addressIndex;
    private int identifierIndex;
    private int signatureIndex;
    private int professionalSuffixIndex;

    private String [] hospitals = {
            "Calbee's Super Dog Hospital",
            "Bark Hospital",
            "Woof Hospital",
            "Meow Hospital"
    };

    private String [] clinics = {
            "Calbee's Super Animal Clinic",
            "Bark Clinic",
            "Woof Clinic",
            "Wswswswsws Clinic"
    };

    Phone[] phones = {
            new Phone(PhoneType.MOBILE, "4152226543", ""),
            new Phone(PhoneType.WORK, "2138889999", "3312"),
            new Phone(PhoneType.MOBILE, "6502786527", ""),
            new Phone(PhoneType.FAX, "2133331212", "000"),
            new Phone(PhoneType.MOBILE, "2133331213", "111"),
            new Phone(PhoneType.WORK, "2133331214", "222"),
            new Phone(PhoneType.MOBILE, "2133331215", "333")
    };

    ProfessionalSuffix [] suffixes = {
            new ProfessionalSuffix(ProfessionalSuffixType.DVM),
            new ProfessionalSuffix(ProfessionalSuffixType.MD),
            new ProfessionalSuffix(ProfessionalSuffixType.DDS),
            new ProfessionalSuffix(ProfessionalSuffixType.OD)
    };

    Address[] addresses = {
            new Address(
                    "1212 Los Angeles Blvd.",
                    "713",
                    "Los Angeles",
                    "CA",
                    "90210", AddressType.MAILING),
            new Address(
                    "325 Sharon Park Dr",
                    "713",
                    "Menlo Park",
                    "California",
                    "94025",
                    AddressType.MAILING),
            new Address(
                    "3 Laura St.",
                    "",
                    "San Francisco",
                    "CA",
                    "92135",
                    AddressType.MAILING
            ),
            new Address(
                    "111 San Jose St.",
                    "#3",
                    "San Jose",
                    "CALIFORNIA",
                    "94582",
                    AddressType.BILLING
            ),
            new Address(
                    "123 Canyon Park Circle",
                    "",
                    "Dublin",
                    "CA",
                    "94582",
                    AddressType.BILLING
            ),
            new Address(
                    "420A Canyon Woods Pl",
                    null,
                    "San Ramon",
                    "California",
                    "94583",
                    AddressType.MAILING
            )
    };

    private ProviderId [] identifiers = {
            new ProviderId(null, "DEA0000000", "ME123456987", "NPI1237654"),
            new ProviderId(null, "DEA4567890", "ME123456789", "NPI1234567"),
            new ProviderId(null, "DEA1000000", "ME223456987", "NPI2237654")
    };



    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(alreadySetup)
            return;

        createVet1();

        createVet2();

        createVet3();

        alreadySetup = true;


        ///////////////////// PETOWNER /////////////////////

//        // 0. Create PetOwner
//        PetOwner petOwner1 = createPetOwner1();
//        //PetOwner petOwner2 = createPetOwner2();
//
//        // 1. Save veterinarianAddresses
//        VeterinarianAddress address3 = createAddress3();
//        petOwner1.addAddress(address3);
//
//        VeterinarianAddress address4 = createAddress4();
//        petOwner1.addAddress(address4);
//
//        // 2. Save PetOwner
//        petOwnerRepository.save(petOwner1);

    }

//    private PetOwner createPetOwner1() {
//        PetOwner petOwner = new PetOwner();
//        petOwner.setUsername("charles102");
//        petOwner.setFirstName("Charles");
//        petOwner.setMiddleName("");
//        petOwner.setLastName("Petzold");
//        petOwner.setEmail("charles.petzold@microsoft.com");
//        petOwner.setEmailConfirmed(false);
//        petOwner.setPhone("2031234587");
//        petOwner.setAltPhone("2031234561");
//        petOwner.setHashedPassword("$2b$10$.IMbXdU4WknFQ/Q/hEVGLOyI2xiSS0sSZHeliLvQYHO5QbiG96/sG");
//        petOwner.setLoginEnabled(true);
//        petOwner.setTwoFactorEnabled(true);
//        petOwner.setAccessFailedCount(0);
//
//        return petOwner;
//    }
//
//    private PetOwner createPetOwner2() {
//        PetOwner petOwner = new PetOwner();
//        petOwner.setUsername("mellow");
//        petOwner.setFirstName("Jano");
//        petOwner.setMiddleName("");
//        petOwner.setLastName("Stevens");
//        petOwner.setEmail("jstevents@aol.com");
//        petOwner.setEmailConfirmed(false);
//        petOwner.setPhone("2135460321");
//        petOwner.setAltPhone("2135460321");
//        petOwner.setHashedPassword("$2b$10$.IMbXdU4WknFQ/Q/hEVGLOyI2xiSS0sSZHeliLvQYHO5QbiG96/sG");
//        petOwner.setLoginEnabled(true);
//        petOwner.setTwoFactorEnabled(true);
//        petOwner.setAccessFailedCount(0);
//
//        return petOwner;
//    }

    private void createVet1() {

        Person person = new Veterinarian();

        person.setUsername("harold");
        person.setFirstName("Harold");
        person.setMiddleName("");
        person.setLastName("Davis");
        person.setEmail("harold@gmail.com");
        person.setHashedPassword(passwordEncoder.encode("password"));
        person.setLoginEnabled(true);
        person.setAccessFailedCount(0);
        person.setLastPasswordResetDate(System.currentTimeMillis());

        // Professional suffix
        ((Veterinarian) person).addProfessionalSuffix(createProfessionalSuffix());

        // Role
        person.addRole(createRole(RoleType.VETERINARIAN));

        // Address
        person.addAddress(createAddress());
        person.addAddress(createAddress());
        person.addAddress(createAddress());

        // Facility
        ((Veterinarian) person).addFacility(createVetFacility(FacilityType.HOSPITAL));
        ((Veterinarian) person).addFacility(createVetFacility(FacilityType.HOSPITAL));

        // SpecialtyDto
        ((Veterinarian) person).addSpecialty(createVeterinarianSpecialty(VetSpecialtyType.ANIMAL_BEHAVIOR));
        ((Veterinarian) person).addSpecialty(createVeterinarianSpecialty(VetSpecialtyType.INTERNAL_MEDICINE_NEUROLOGY));

        // Phone
        person.addPhone(createPhone());
        person.addPhone(createPhone());
        person.addPhone(createPhone());
        person.addPhone(createPhone());

        // Persist veterinarian
        Person ret = personRepository.saveAndFlush(person);

        // Signature
        createSignature(ret, SignatureType.VETERINARIAN);

        // Provider identifier
        createProviderId(ret, ProviderIdType.VETERINARIAN);
    }

    private void createVet2() {

        Person person = new Veterinarian();

        person.setUsername("hey_ranel");
        person.setFirstName("Ranel");
        person.setMiddleName("Rainier");
        person.setLastName("del Pilar");
        person.setEmail("ranel@gmail.com");
        person.setHashedPassword(passwordEncoder.encode("hello"));
        person.setLoginEnabled(true);
        person.setAccessFailedCount(0);
        person.setLastPasswordResetDate(System.currentTimeMillis());

        ((Veterinarian) person).addProfessionalSuffix(createProfessionalSuffix());

        person.addRole(createRole(RoleType.VETERINARIAN));

        person.addAddress(createAddress());

        ((Veterinarian) person).addFacility(createVetFacility(FacilityType.CLINIC));

        ((Veterinarian) person).addSpecialty(createVeterinarianSpecialty(VetSpecialtyType.DENTISTRY));
        ((Veterinarian) person).addSpecialty(createVeterinarianSpecialty(VetSpecialtyType.DERMATOLOGY));

        person.addPhone(createPhone());
        person.addPhone(createPhone());

        Person ret = personRepository.saveAndFlush(person);

        createSignature(ret, SignatureType.VETERINARIAN);

        createProviderId(ret, ProviderIdType.VETERINARIAN);
    }

    private void  createVet3() {

        Person person = new Veterinarian();

        person.setUsername("hey_peter");
        person.setFirstName("Peter");
        person.setMiddleName("");
        person.setLastName("McTavish");
        person.setEmail("peter@gmail.com");
        person.setHashedPassword(passwordEncoder.encode("test"));
        person.setAccessFailedCount(3);
        person.setLastPasswordResetDate(System.currentTimeMillis());

        ((Veterinarian) person).addProfessionalSuffix(createProfessionalSuffix());

        person.addRole(createRole(RoleType.VETERINARIAN));

        person.addAddress(createAddress());

        ((Veterinarian) person).addFacility(createVetFacility(FacilityType.CLINIC));

        ((Veterinarian) person).addSpecialty(createVeterinarianSpecialty(VetSpecialtyType.NUTRITION));
        ((Veterinarian) person).addSpecialty(createVeterinarianSpecialty(VetSpecialtyType.EMERGENCY_AND_CRITICAL_CARE));

        person.addPhone(createPhone());

        Person ret = personRepository.saveAndFlush(person);

        createSignature(ret, SignatureType.VETERINARIAN);

        createProviderId(ret, ProviderIdType.VETERINARIAN);
    }

    private ProfessionalSuffix createProfessionalSuffix() {

        if(professionalSuffixIndex >= suffixes.length)
            professionalSuffixIndex = 0;

        return suffixes[professionalSuffixIndex++];
    }

    private Role createRole(RoleType roleType) {
        return new Role(roleType);
    }

    private Facility createVetFacility(FacilityType facilityType) {

        if(facilityType == FacilityType.HOSPITAL) {
            if(facilityHospitalIndex >= hospitals.length)
                facilityHospitalIndex = 0;

            return new Facility(facilityType, hospitals[facilityHospitalIndex++]);
        } else if(facilityType == FacilityType.CLINIC) {
            if(facilityClinicIndex >= clinics.length)
                facilityClinicIndex = 0;

            return new Facility(facilityType, clinics[facilityClinicIndex++]);
        }

        return null;
    }

    private Specialty createVeterinarianSpecialty(VetSpecialtyType specialtyType) {
        return new VeterinarianSpecialty(specialtyType);
    }

    private Phone createPhone() {

        if(phoneIndex >= phones.length)
            phoneIndex = 0;

        return phones[phoneIndex++];
    }

    private Address createAddress() {

        if(addressIndex >= addresses.length)
            addressIndex = 0;

        return addresses[addressIndex++];
    }

    private void createProviderId(Person vet, final ProviderIdType idType) {

        if(identifierIndex >= identifiers.length)
            identifierIndex = 0;

        identifiers[identifierIndex].setProviderIdType(idType);
        identifiers[identifierIndex].setPerson(vet);

        // Persist professional identifier
        providerIdRepository.saveAndFlush(identifiers[identifierIndex++]);
    }

    private void createSignature(Person person, final SignatureType signatureType) throws NullPointerException {
        URL url = this.getClass().getClassLoader().getResource("static/images/dog.jpg");
        File file;
        byte [] bytes = null;

        try {
            file = new File(url.toURI());
            bytes = Files.readAllBytes(file.toPath());
            Tika tika = new Tika();
            tika.detect(bytes);
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Signature [] signatures = { new Signature(signatureType, bytes.length, "image/jpeg", bytes)};

        if(signatureIndex >= signatures.length)
            signatureIndex = 0;
        signatures[signatureIndex].setPerson(person);

        // Persist signature
        signatureRepository.saveAndFlush(signatures[signatureIndex++]);
    }
}
