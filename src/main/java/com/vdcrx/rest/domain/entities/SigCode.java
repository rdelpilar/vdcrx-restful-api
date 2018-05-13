package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.SigCodeType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Sig code entity
 *
 * @author Ranel del Pilar
 */

@Entity
@Audited
@Table(name = "SIG_CODES")
public class SigCode extends BaseEntity {

    public SigCode() { super(); }

    public SigCode(SigCodeType sigCodeType, String definition) {
        super();
        this.sigCodeType = sigCodeType;
        this.definition = definition;
    }

    @Getter
    @Setter
    @NotNull(message = "{message.SigCode.sigCodeType.null}")
    @Enumerated(EnumType.STRING)
    @Column(name = "SIG_CODE_TYPE", nullable = false, columnDefinition = "VARCHAR(16) NOT NULL", length = 16)
    private SigCodeType sigCodeType = SigCodeType.NONE;

    @Getter
    @Setter
    @NotNull(message = "{message.SigCode.definition.null}")
    private String definition;

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long version = 0L;

    @NotNull(message = "{message.SigCode.medication.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEDICATION_UUID", nullable = false)
    private Medication medication;

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        if(sameAsFormer(medication))
            return;

        Medication prev = this.medication;
        this.medication = medication;

        if(prev != null)
            prev.removeSigCode(this);

        if(medication != null)
            medication.addSigCode(this);
    }

    private boolean sameAsFormer(Medication medication) {
        return this.medication == null ? medication == null : this.medication.equals(medication);
    }
}
