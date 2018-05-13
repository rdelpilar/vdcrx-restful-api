package com.vdcrx.rest.domain.entities;

import com.vdcrx.rest.domain.enums.SignatureType;
import com.vdcrx.rest.domain.interfaces.ISignature;
import com.vdcrx.rest.validators.ValidSignature;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Signature entity
 *
 * @author Ranel del Pilar
 */

@Entity
@Audited
@ValidSignature
@Table(name = "SIGNATURES")
public class Signature extends BaseEntity implements ISignature {

    public final static int MAX_SIG_SIZE = 5242880; // 5 MB

    public Signature() { super(); }

    public Signature(SignatureType signatureType, int size, String contentType, byte [] image) {
        super();
        this.signatureType = signatureType;
        this.size = size;
        this.contentType = contentType;
        this.image = (image == null) ? null : image.clone();
    }

    @Getter
    @Setter
    @NotNull(message = "{message.Signature.signatureType.null}")
    @Column(name = "SIGNATURE_TYPE", nullable = false, length = 32,
            columnDefinition = "VARCHAR(32) DEFAULT 'VETERINARIAN'")
    @Enumerated(EnumType.STRING)
    private SignatureType signatureType = SignatureType.VETERINARIAN;

    @Getter
    @Setter
    @Max(value = MAX_SIG_SIZE, message = "{message.Signature.size.max}")
    @Column(name = "SIZE", nullable = false, columnDefinition = "INT(10) DEFAULT 0")
    private int size;

    @Getter
    @Setter
    @NotBlank(message = "{message.Signature.contentType.blank}")
    @Size(max = 32, message = "{message.Signature.contentType.size}")
    @Column(name = "CONTENT_TYPE", nullable = false, length = 32)
    private String contentType;

    @Getter
    @Setter
    @Lob
    @Column(name = "IMAGE", nullable = false, columnDefinition = "MEDIUMBLOB")
    private byte [] image;

    @Getter
    @Version
    @Column(name = "VERSION", nullable = false, columnDefinition = "INT(3) DEFAULT 0")
    private long version = 0L;


    // Signature Entity (Uni-directional One-to-one with Veterinarian Entity) //

    @Getter
    @Setter
    @NotAudited
    @NotNull(message = "{message.Signature.person.null}")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", unique = true, nullable = false, updatable = false)
    private Person person;

    @Override
    public String toString() {
        return "Signature [" +
                "signatureType: " + signatureType +
                ", size: " + size +
                ", contentType: '" + contentType +
                ", version: " + version + "]; " +
                "[Person is empty]; " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Signature)) return false;
        return getInternalId().equals(((Signature) o).getInternalId());
    }

    @Override
    public int hashCode() {
        return getInternalId().hashCode();
    }
}


