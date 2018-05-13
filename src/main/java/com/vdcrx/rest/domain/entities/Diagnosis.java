package com.vdcrx.rest.domain.entities;

import lombok.Getter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Diagnosis entity
 *
 * @author Ranel del Pilar
 */

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
@Entity
@Audited
@Table(name = "DIAGNOSES")
public abstract class Diagnosis extends BaseEntity {

    public Diagnosis() { super(); }

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long version = 0L;

    @Override
    public String toString() {
        return "Diagnosis [version: " + version + "]; " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Diagnosis)) return false;
        return getInternalId().equals(((Diagnosis) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
