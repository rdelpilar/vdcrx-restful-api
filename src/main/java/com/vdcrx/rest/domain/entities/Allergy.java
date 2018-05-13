package com.vdcrx.rest.domain.entities;

import lombok.Getter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Allergy entity
 *
 * @author Ranel del Pilar
 */

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
@Entity
@Audited
@Table(name = "ALLERGIES")
public abstract class Allergy extends BaseEntity {

    public Allergy() { super(); }

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long version = 0L;

    @Override
    public String toString() {
        return "Allergy [" +
                ", version: " + version +
                "]; " +
                super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Allergy)) return false;
        return getInternalId().equals(((Allergy) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
