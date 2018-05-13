package com.vdcrx.rest.domain.entities;

import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Pet medication entity
 *
 * @author Ranel del Pilar
 */

@Entity
@DiscriminatorValue("PET")
public class PetMedication extends Medication {

    public PetMedication() { super(); }

    @NotAudited
    @NotNull(message = "{message.PetMedication.pets.null}")
    @ManyToMany(mappedBy = "medications", targetEntity = Pet.class)
    private Set<Pet> pets;

    private Set<Pet> getPetsInternal() {
        if(this.pets == null)
            this.pets = new HashSet<>();
        return pets;
    }

    public Set<Pet> getPets() {
        Set<Pet> pets = new HashSet<>(getPetsInternal());
        return Collections.unmodifiableSet(pets);
    }

    public void addPet(Pet pet) {
        if(pet == null || getPetsInternal().contains(pet))
            return;

        getPetsInternal().add(pet);
        pet.addPetMedication(this);
    }

    public void removePet(Pet pet) {
        if(pet == null || !getPetsInternal().contains(pet))
            return;

        getPetsInternal().remove(pet);
        pet.removePetMedication(this);
    }

    @NotAudited
    @Valid
    @NotNull(message = "{message.PetMedication.diagnosis.null}")
    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = PetDiagnosis.class)
    private Set<Diagnosis> diagnoses;

    private Set<Diagnosis> getDiagnosesInternal() {
        if(this.diagnoses == null)
            this.diagnoses = new HashSet<>();

        return this.diagnoses;
    }

    public Set<Diagnosis> getDiagnoses() {
        Set<Diagnosis> diagnoses = new HashSet<>(getDiagnosesInternal());
        return Collections.unmodifiableSet(diagnoses);
    }

    public void addDiagnosis(Diagnosis diagnosis) {
        if(diagnosis == null || getDiagnosesInternal().contains(diagnosis))
            return;

        getDiagnosesInternal().add(diagnosis);

        if(diagnosis instanceof PetDiagnosis)
            ((PetDiagnosis) diagnosis).setMedication(this);
    }

    public void removeDiagnosis(Diagnosis diagnosis) {
        if(diagnosis == null || !getDiagnosesInternal().contains(diagnosis))
            return;

        getDiagnosesInternal().remove(diagnosis);

        if(diagnosis instanceof PetDiagnosis)
            ((PetDiagnosis)diagnosis).setMedication(null);
    }

    @Override
    public String toString() {
        return "[Pets empty]; " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetMedication)) return false;
        return getInternalId().equals(((PetMedication) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
