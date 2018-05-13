package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.PetDiagnosisType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Pet diagnosis entity
 *
 * @author Ranel del Pilar
 */

@Entity
@Audited
@DiscriminatorValue("PET")
public class PetDiagnosis extends Diagnosis {

    public PetDiagnosis() { super(); }

    public PetDiagnosis(PetDiagnosisType petDiagnosisType, String definition) {
        super();
        this.petDiagnosisType = petDiagnosisType;
        this.definition = definition;
    }

    @Getter
    @Setter
    @NotNull(message = "{message.PetDiagnosis.petDiagnosisType.null}")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "DIAGNOSIS_TYPE", nullable = false, length = 64, columnDefinition = "VARCHAR(64) DEFAULT 'OTHER'")
    private PetDiagnosisType petDiagnosisType = PetDiagnosisType.OTHER;

    @Getter
    @Setter
    @NotNull(message = "{message.PetDiagnosis.definition.null}")
    @Size(max = 100, message = "{message.PetDiagnosis.definition.size}")
    @Column(name = "DEFINITION", nullable = false, length = 128, columnDefinition = "VARCHAR(128) DEFAULT ''")
    private String definition;

    @NotAudited
    @NotNull(message = "{message.PetDiagnosis.medication.null}")
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
            ((PetMedication) prev).removeDiagnosis(this);

        if(medication != null)
            ((PetMedication) medication).addDiagnosis(this);
    }

    private boolean sameAsFormer(Medication medication) {
        return this.medication == null ? medication == null : this.medication.equals(medication);
    }
}
