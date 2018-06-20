package com.vdcrx.rest.domain.entities;

import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Veterinarian entity
 *
 * @author Ranel del Pilar
 */

@Entity
@DiscriminatorValue("VETERINARIAN")
@NamedEntityGraph(name = "Veterinarian.graph",
        attributeNodes = {
        @NamedAttributeNode("roles"),
        @NamedAttributeNode("addresses"),
        @NamedAttributeNode("phones"),
        @NamedAttributeNode("suffixes"),
        @NamedAttributeNode("facilities"),
        @NamedAttributeNode("specialties"),
        @NamedAttributeNode("pets"),
        @NamedAttributeNode("petOwners")
})
public class Veterinarian extends Person {

    // Veterinarian Suffix (Bi-directional Many-to-many //

    @NotAudited
    @Valid
    @NotNull(message = "{message.Veterinarian.ProfessionalSuffix.null}")
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfessionalSuffix> suffixes;

    private Set<ProfessionalSuffix> getProfessionalSuffixesInternal() {
        if(this.suffixes == null)
            this.suffixes = new HashSet<>();

        return this.suffixes;
    }

    public Set<ProfessionalSuffix> getSuffixes() {
        Set<ProfessionalSuffix> suffixes = new HashSet<>(getProfessionalSuffixesInternal());
        return Collections.unmodifiableSet(suffixes);
    }

    public void addProfessionalSuffix(ProfessionalSuffix suffix) {
        if (suffix == null || getProfessionalSuffixesInternal().contains(suffix))
            return;

        getProfessionalSuffixesInternal().add(suffix);
        suffix.setPerson(this);
    }

    public void removeProfessionalSuffix(ProfessionalSuffix suffix) {
        if(!getProfessionalSuffixesInternal().contains(suffix))
            return;

        getProfessionalSuffixesInternal().remove(suffix);
        suffix.setPerson(null);
    }

    // Veterinarian Facility (Bi-directional Many-to-many //

    @NotAudited
    @Valid
    @NotNull(message = "{message.Veterinarian.facilities.null}")
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "VETERINARIANS_FACILITIES",
            joinColumns =  @JoinColumn(name = "VETERINARIAN_UUID"),
            inverseJoinColumns = @JoinColumn(name = "FACILITY_UUID"))
    private Set<Facility> facilities;

    private Set<Facility> getFacilitiesInternal() {
        if(this.facilities == null) {
            this.facilities = new HashSet<>();
        }
        return this.facilities;
    }

    public Set<Facility> getFacilities() {
        Set<Facility> facilities = new HashSet<>(getFacilitiesInternal());
        return Collections.unmodifiableSet(facilities);
    }

    public void addFacility(Facility facility) {
        if(facility == null || getFacilitiesInternal().contains(facility))
            return;

        getFacilitiesInternal().add(facility);
        facility.addPerson(this);
    }

    public void removeFacility(Facility facility) {
        if(facility == null || !getFacilitiesInternal().contains(facility))
            return;

        getFacilitiesInternal().remove(facility);
        facility.removePerson(this);
    }

    // Pet owner (Bi-directional Many-to-many //
    @NotAudited
    @Valid
    //@NotNull(message = "{message.Veterinarian.petOwners.null}")
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "VETERINARIANS_PET_OWNERS",
            joinColumns = @JoinColumn(name = "VETERINARIAN_UUID"),
            inverseJoinColumns = @JoinColumn(name = "PET_OWNER_UUID"))
    private Set<PetOwner> petOwners;

    private Set<PetOwner> getPetOwnersInternal() {
        if(this.petOwners == null)
            this.petOwners = new HashSet<>();

        return this.petOwners;
    }

    private Set<PetOwner> getPetOwners() {
        Set<PetOwner> petOwners = new HashSet<>(getPetOwnersInternal());
        return Collections.unmodifiableSet(petOwners);
    }

    public void addPetOwner(PetOwner petOwner) {
        if (petOwner == null || getPetOwnersInternal().contains(petOwner)) {
            return;
        }

        getPetOwnersInternal().add(petOwner);
        petOwner.addVeterinarian(this);
    }

    public void removePetOwner(PetOwner petOwner) {
        if(petOwner == null || !getPetOwnersInternal().contains(petOwner))
            return;

        getPetOwnersInternal().remove(petOwner);
        petOwner.removeVeterinarian(this);
    }

    // Pets (Bi-directional Many-to-many //
    @NotAudited
    @Valid
    //@NotNull(message = "{message.Veterinarian.pets.null}")
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "VETERINARIANS_PETS",
            joinColumns = @JoinColumn(name = "VETERINARIAN_UUID"),
            inverseJoinColumns = @JoinColumn(name = "PETS_UUID"))
    private Set<Pet> pets;

    private Set<Pet> getPetsInternal() {
        if(this.pets == null)
            this.pets = new HashSet<>();

        return this.pets;
    }

    public Set<Pet> getPets() {
        Set<Pet> pets = new HashSet<>(getPetsInternal());
        return Collections.unmodifiableSet(pets);
    }

    public void addPet(Pet pet) {
        if(pet == null || getPetsInternal().contains(pet))
            return;

        getPetsInternal().add(pet);
        pet.addVeterinarian(this);
    }

    public void removePet(Pet pet) {
        if(pet == null || !getPetsInternal().contains(pet))
            return;

        getPetsInternal().remove(pet);
        pet.removeVeterinarian(this);
    }

    // Veterinarian SpecialtyService (Bi-directional Many-to-many //

    @NotAudited
    @Valid
    @NotNull(message = "{message.Veterinarian.specialties.null}")
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "VETERINARIANS_SPECIALTIES",
            joinColumns = @JoinColumn(name = "VETERINARIAN_UUID"),
            inverseJoinColumns = @JoinColumn(name = "VETERINARIAN_SPECIALTY_UUID"))
    private Set<Specialty> specialties;

    private Set<Specialty> getSpecialtiesInternal() {
        if(this.specialties == null) {
            this.specialties = new HashSet<>();
        }
        return this.specialties;
    }

    public Set<Specialty> getSpecialties() {
        Set<Specialty> specialties = new HashSet<>(getSpecialtiesInternal());
        return Collections.unmodifiableSet(specialties);
    }

    public void addSpecialty(Specialty specialty) {
        if(specialty == null || getSpecialtiesInternal().contains(specialty))
            return;

        getSpecialtiesInternal().add(specialty);
        ((VeterinarianSpecialty) specialty).addPerson(this);
    }

    public void removeSpecialty(Specialty specialty) {
        if(specialty == null || !getSpecialtiesInternal().contains(specialty))
            return;

        getSpecialtiesInternal().remove(specialty);
        ((VeterinarianSpecialty) specialty).removePerson(this);
    }

    @Override
    public String toString() {
        return "[Veterinarian pets empty]; " +
                "[Pet Owners empty]; " +
                "[Veterinarian facilities empty]; " +
                "[Veterinarian specialties empty]; " +
                super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veterinarian)) return false;
        return getInternalId().equals(((Veterinarian) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
