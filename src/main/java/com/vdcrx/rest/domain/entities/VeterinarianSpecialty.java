package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.VetSpecialtyType;
import com.vdcrx.rest.domain.interfaces.IPerson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Veterinarian specialty entity
 *
 * @author Ranel del Pilar
 */

@Entity
@Audited
@DiscriminatorValue("VETERINARIAN")
public class VeterinarianSpecialty extends Specialty {

    public VeterinarianSpecialty() { super(); }

    public VeterinarianSpecialty(VetSpecialtyType vetSpecialtyType) {
        super();
        this.vetSpecialtyType = vetSpecialtyType;
    }

    @Getter
    @Setter
    @NotNull(message = "{message.VeterinarianSpecialty.vetSpecialtyType.null}")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "SPECIALTY_TYPE", nullable = false, length = 40, columnDefinition = "VARCHAR(40) DEFAULT 'OTHER'")
    private VetSpecialtyType vetSpecialtyType = VetSpecialtyType.OTHER;

    @NotAudited
    @NotNull(message = "{message.VeterinarianSpecialty.persons.null}")
    @ManyToMany(mappedBy = "specialties", targetEntity = Veterinarian.class)
    private Set<Person> persons;

    private Set<Person> getPersonsInternal() {
        if(this.persons == null) {
            this.persons = new HashSet<>();
        }
        return this.persons;
    }

    public Set<IPerson> getPersons() {
        Set<IPerson> persons = new HashSet<>(getPersonsInternal());
        return Collections.unmodifiableSet(persons);
    }

    void addPerson(IPerson person) {
        if(person == null || getPersonsInternal().contains(person))
            return;

        getPersonsInternal().add((Person) person);
        ((Veterinarian) person).addSpecialty(this);
    }

    void removePerson(IPerson person) {
        if(person == null || !getPersonsInternal().contains(person))
            return;

        getPersonsInternal().remove(person);
        ((Veterinarian) person).removeSpecialty(this);
    }

    @Override
    public String toString() {
        return "Veterinarian Specialty [name: " +
                vetSpecialtyType.name() +
                "[Person empty]; " + "]; " +
                super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof VeterinarianSpecialty)) return false;
        return getInternalId().equals(((VeterinarianSpecialty) o).getInternalId());
    }

    @Override
    public int hashCode() { return getInternalId().hashCode(); }
}
