package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.ProfessionalSuffixType;
import com.vdcrx.rest.domain.interfaces.IPerson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Professional suffix entity
 *
 * @author Ranel del Pilar
 */

@Entity
@Audited
@Table(name = "PROFESSIONAL_SUFFIX")
public class ProfessionalSuffix extends BaseEntity {

    public ProfessionalSuffix() { super(); }

    public ProfessionalSuffix(ProfessionalSuffixType professionalSuffixType) {
        super();
        this.professionalSuffixType = professionalSuffixType;
    }

    @Getter
    @Setter
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "SUFFIX", nullable = false, length = 16, columnDefinition = "VARCHAR(16) DEFAULT 'DVM'")
    private ProfessionalSuffixType professionalSuffixType = ProfessionalSuffixType.DVM;

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    protected long version = 0L;

    @NotNull(message = "{message.ProfessionalSuffix.person.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_UUID", nullable = false)
    private Person person;

    public Person getPerson() { return person; }

    public void setPerson(IPerson person) {
        if(sameAsFormer(person))
            return;

        Person prev = this.person;
        this.person = (Person) person;

        if(prev != null)
            ((Veterinarian) prev).removeProfessionalSuffix(this);

        if(person != null)
            ((Veterinarian) person).addProfessionalSuffix(this);
    }

    private boolean sameAsFormer(IPerson person) {
        return this.person == null ? person == null : this.person.equals(person);
    }

    @Override
    public String toString() {
        return "ProfessionalSuffix [" +
                "professionalSuffixType: " + professionalSuffixType +
                ", version: " + version +
                "]; " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfessionalSuffix)) return false;
        return getInternalId().equals(((ProfessionalSuffix) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
