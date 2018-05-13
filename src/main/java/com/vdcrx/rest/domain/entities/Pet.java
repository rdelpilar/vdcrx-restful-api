package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.PetGenderType;
import com.vdcrx.rest.domain.enums.SpeciesType;
import com.vdcrx.rest.domain.interfaces.IPerson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Pet entity
 *
 * @author Ranel del Pilar
 */

@Entity
@Audited
@Table(name = "PETS")
@NamedEntityGraph(name = "Pet.allergies.graph",
        attributeNodes = {
        @NamedAttributeNode("allergies"),
        @NamedAttributeNode("medications")
})
public class Pet extends BaseEntity {

    public Pet() { super(); }

    public Pet(String name, SpeciesType speciesType,
               PetGenderType petGenderType,
               long dateOfBirth,
               double weight,
               String diseaseState,
               Set<Allergy> allergies,
               Set<Medication> medications,
               byte[] photo) {
        this.name = name;
        this.speciesType = speciesType;
        this.petGenderType = petGenderType;
        this.dateOfBirth = dateOfBirth;
        this.weight = weight;
        this.diseaseState = diseaseState;
        this.allergies = allergies;
        this.medications = medications;
        this.photo = photo;
    }

    @Getter
    @Setter
    @NotBlank(message = "{message.Pet.name.blank}")
    @Size(min = 2, max = 32, message = "{message.PetDto.name.size}")
    @Column(name = "NAME", nullable = false, length = 32)
    private String name;

    @Getter
    @Setter
    @NotNull(message = "{message.Pet.speciesType.null}")
    @Enumerated(EnumType.STRING)
    @Column(name = "SPECIES_TYPE", nullable = false, columnDefinition = "VARCHAR(32) NOT NULL", length = 32)
    private SpeciesType speciesType = SpeciesType.OTHERS;

    @Getter
    @Setter
    @NotNull(message = "{message.Pet.petGenderType.null}")
    @Enumerated(EnumType.STRING)
    @Column(name = "PET_GENDER_TYPE", nullable = false, columnDefinition = "VARCHAR(16) NOT NULL", length = 16)
    private PetGenderType petGenderType = PetGenderType.MALE;

    @Getter
    @Setter
    @Column(name = "DATE_OF_BIRTH", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long dateOfBirth;

    @Getter
    @Setter
    @Column(name = "WEIGHT", nullable = false, columnDefinition = "DOUBLE PRECISION(3,5) DEFAULT 0")
    private double weight;

    @NotBlank(message = "{message.Pet.diseaseState.blank}")
    @Size(min = 2, max = 32, message = "{message.PetDto.diseaseState.size}")
    @Column(name = "DISEASE_STATE", length = 32)
    private String diseaseState;

    @Getter
    @Setter
    @Lob
    @Column(name = "IMAGE", nullable = false, columnDefinition = "MEDIUMBLOB")
    private byte [] photo;

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long version = 0L;

    ///////////////// Pet Allergy (Bi-directional One-to-many) ///////////////////

    @NotAudited
    @Valid
    @NotNull(message = "message.Pet.allergies.null")
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "PERSONS_PET_ALLERGIES",
            joinColumns = @JoinColumn(name = "PERSON_UUID"),
            inverseJoinColumns = @JoinColumn(name = "PET_UUID"))
    private Set<Allergy> allergies;

    private Set<Allergy> getPetAllergiesInternal() {
        if(this.allergies == null)
            this.allergies = new HashSet<>();

        return this.allergies;
    }

    public Set<Allergy> getAllergies() {
        Set<Allergy> allergies = new HashSet<>(getPetAllergiesInternal());
        return Collections.unmodifiableSet(allergies);
    }

    public void addPetAllergy(Allergy allergy) {
        if(allergy == null || getPetAllergiesInternal().contains(allergy))
            return;

        getPetAllergiesInternal().add(allergy);
        ((PetAllergy) allergy).addPet(this);
    }

    public void removePetAllergy(Allergy allergy) {
        if(allergy == null || !getPetAllergiesInternal().contains(allergy))
            return;

        getPetAllergiesInternal().remove(allergy);
        ((PetAllergy) allergy).removePet(this);
    }

    ///////////////// Pet Medication (Bi-directional One-to-many) ///////////////////

    @NotAudited
    @Valid
    @NotNull(message = "{message.Pet.medications.null}")
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "PERSONS_PET_MEDICATIONS",
            joinColumns = @JoinColumn(name = "PERSON_UUID"),
            inverseJoinColumns = @JoinColumn(name = "PET_MEDICATION_UUID"))
    private Set<Medication> medications;

    private Set<Medication> getMedicationsInternal() {
        if(this.medications == null)
            this.medications = new HashSet<>();

        return this.medications;
    }

    public Set<Medication> getMedications() {
        Set<Medication> medications = new HashSet<>(getMedicationsInternal());
        return Collections.unmodifiableSet(medications);
    }

    public void addPetMedication(Medication medication) {
        if(medication == null || getMedicationsInternal().contains(medication))
            return;

        getMedicationsInternal().add(medication);
        ((PetMedication) medication).addPet(this);
    }

    public void removePetMedication(Medication medication) {
        if(medication == null || !getMedicationsInternal().contains(medication))
            return;

        getMedicationsInternal().remove(medication);
        ((PetMedication) medication).removePet(this);
    }

    ///////////////// Pet owner (Bi-directional Many-to-many) ///////////////////

    @NotAudited
    @Valid
    @NotNull(message = "{message.Pet.petOwner.null}")
    @ManyToMany(mappedBy = "pets", targetEntity = PetOwner.class)
    private Set<Person> petOwners;

    private Set<Person> getPetOwnersInternal() {
        if(this.petOwners == null)
            this.petOwners = new HashSet<>();

        return this.petOwners;
    }

    public Set<IPerson> getPetOwners() {
        Set<IPerson> petOwners = new HashSet<>(getPetOwnersInternal());
        return Collections.unmodifiableSet(petOwners);
    }

    void addPetOwner(IPerson person) {
        if(person == null || getPetOwnersInternal().contains(person))
            return;

        getPetOwnersInternal().add((Person) person);
        ((PetOwner) person).addPet(this);
    }

    void removePetOwner(IPerson person) {
        if(person == null || !getPetOwnersInternal().contains(person))
            return;

        getPetOwnersInternal().remove(person);
        ((PetOwner) person).removePet(this);
    }

    ///////////////// Veterinarian (Bi-directional Many-to-many) ///////////////////

    @NotAudited
    @Valid
    @NotNull(message = "{message.Pet.veterinarian.null}")
    @ManyToMany(mappedBy = "pets", targetEntity = Veterinarian.class)
    private Set<Person> veterinarians;

    private Set<Person> getVeterinariansInternal() {
        if(this.veterinarians == null)
            this.veterinarians = new HashSet<>();

        return this.veterinarians;
    }

    public Set<IPerson> getVeterinarians() {
        Set<IPerson> veterinarians = new HashSet<>(getVeterinariansInternal());
        return Collections.unmodifiableSet(veterinarians);
    }

    void addVeterinarian(IPerson person) {
        if(person == null || getVeterinariansInternal().contains(person))
            return;

        getVeterinariansInternal().add((Person) person);
        ((Veterinarian) person).addPet(this);
    }

    void removeVeterinarian(IPerson person) {
        if(person == null || !getVeterinariansInternal().contains(person))
            return;

        getVeterinariansInternal().remove(person);
        ((Veterinarian) person).removePet(this);
    }

    ///////////////// Veterinarian (Bi-directional One-to-many) ///////////////////

    @NotAudited
    @Valid
    @NotNull
    @ManyToOne
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pet [");
        sb.append("Name: ").append(this.name).append("; ");
        sb.append("Specialty Type: ").append(this.speciesType).append("; ");
        sb.append("Pet Gender Type: ").append(this.petGenderType).append("; ");
        sb.append("Date of Birth: ").append(this.dateOfBirth).append("; ");
        sb.append("Weight: ").append(this.weight).append("; ");
        sb.append("Disease state: ").append(this.diseaseState).append("; ");
        sb.append("Version: ").append(this.version);
        sb.append("]; ");

        sb.append("[Image empty]; ");
        sb.append("[Pet Allergy empty]; ");
        sb.append("[Pet Medication empty]; ");
        sb.append("[Pet Owners empty]; ");
        sb.append("[Veterinarians empty]; ");

        sb.append(super.toString());

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Pet)) return false;
        return getInternalId().equals(((Pet) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
