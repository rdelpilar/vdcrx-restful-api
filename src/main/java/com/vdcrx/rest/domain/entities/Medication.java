package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.FlavoringType;
import com.vdcrx.rest.domain.enums.FormulationType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Medication entity
 *
 * @author Ranel del Pilar
 */

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
@Entity
@Audited
@Table(name = "MEDICATIONS")
public abstract class Medication extends BaseEntity {

    public Medication() { super(); }

    public Medication(String name) {
        super();
        this.name = name;
    }

    @Getter
    @Setter
    @NotBlank(message = "{message.Medication.name.blank}")
    @Size(min = 3, max = 64, message = "{message.Medication.name.size}")
    @Column(name = "NAME", nullable = false, length = 64)
    private String name;

    @Getter
    @Setter
    @NotNull(message = "{message.Medication.formulationType.null}")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "FORMULATION_TYPE", nullable = false, length = 32, columnDefinition = "VARCHAR(32) DEFAULT 'OTHERS'")
    private FormulationType formulationType = FormulationType.OTHERS;

    @Getter
    @Setter
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    @Column(name = "STRENGTH", nullable = false, columnDefinition = "DOUBLE PRECISION(4,2) DEFAULT 0")
    private double strength;

    @Getter
    @Setter
    @Max(value = 10, message = "{message.Medication.size.max}")
    @Column(name = "QUANTITY", nullable = false, columnDefinition = "INT(2) DEFAULT 0")
    private int quantity;

    @Getter
    @Setter
    @NotNull(message = "{message.Medication.flavoringType.null}")
    @Enumerated(EnumType.STRING)
    @Column(name = "FLAVORING", nullable = false)//, columnDefinition = "VARCHAR(16) NOT NULL", length = 16)
    private FlavoringType flavoringType = FlavoringType.NONE;

    @Getter
    @Setter
    @Column(name = "REFILL", nullable = false)//, columnDefinition = "BOOLEAN(1) DEFAULT 0")
    private boolean refill;

    @Getter
    @Setter
    @Column(name = "POST_DATE_REFILL", nullable = false)//, columnDefinition = "BIGINT(20) DEFAULT 0")
    private long postDateRefill;

    @Getter
    @Setter
    @NotBlank(message = "{message.Medication.intervals.blank}")
    @Size(min = 3, max = 32, message = "{message.Medication.intervals.size}")
    @Column(name = "INTERVALS", nullable = false, length = 32)
    private String intervals;

    @Getter
    @Setter
    @Column(name = "DURATION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private int duration;

    @NotAudited
    @Valid
    @NotNull(message = "{message.Medication.sigCodes.null}")
    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SigCode> sigCodes;

    private Set<SigCode> getSigCodesInternal() {
        if(this.sigCodes == null)
            this.sigCodes = new HashSet<>();

        return this.sigCodes;
    }

    public Set<SigCode> getSigCodes() {
        Set<SigCode> sigCodes = new HashSet<>(getSigCodesInternal());
        return Collections.unmodifiableSet(sigCodes);
    }

    public void addSigCode(SigCode sigCode) {
        if(sigCode == null || getSigCodesInternal().contains(sigCode))
            return;

        getSigCodesInternal().add(sigCode);
        sigCode.setMedication(this);
    }

    public void removeSigCode(SigCode sigCode) {
        if(sigCode == null || !getSigCodesInternal().contains(sigCode))
            return;

        getSigCodesInternal().remove(sigCode);
        sigCode.setMedication(null);
    }

    @Getter
    @Setter
    @Column(name = "GENERIC", nullable = false)//, columnDefinition = "BOOLEAN(1) DEFAULT 0")
    private boolean generic;

    @Getter
    @Setter
    @Column(name = "PRESCRIBED_ON", nullable = false, columnDefinition = "BIGINT(20) DEFAULT 0")
    private long prescribedOn;

    @Getter
    @Setter
    @Size(max = 65535, message = "{message.Medication.notes.size}")
    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long version = 0L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Medication [");
        sb.append("Name: ").append(name).append("; ");
        sb.append("Formulation: ").append(formulationType).append("; ");
        sb.append("Strength: ").append(strength).append("; ");
        sb.append("Quantity: ").append(quantity).append("; ");
        sb.append("Flavoring: ").append(flavoringType).append("; ");
        sb.append("Refill: ").append(refill).append("; ");
        sb.append("Post Date Refill: ").append(postDateRefill).append("; ");
        //sb.append("Interval: ").append(interval).append("; ");
        sb.append("Duration: ").append(duration).append("; ");
        sb.append("Generic: ").append(generic).append("; ");
        sb.append("Prescribed on: ").append(prescribedOn).append("; ");
        sb.append("Notes: ").append(notes).append("; ");
        sb.append("Version: ").append(version).append("]; ");

        sb.append("[Sig codes empty]; ");

        sb.append(super.toString());

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medication)) return false;
        return getInternalId().equals(((Medication) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}
