package com.vdcrx.rest.validators;

import com.vdcrx.rest.domain.interfaces.ISignature;
import org.apache.tika.Tika;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.vdcrx.rest.domain.entities.Signature.MAX_SIG_SIZE;

/**
 * Signature validator using @ValidSignature
 *
 * @author Ranel del Pilar
 */

public class SignatureValidator implements ConstraintValidator<ValidSignature, ISignature> {

    private Tika tika;

    @Override
    public void initialize(ValidSignature annotation) {
        tika = new Tika();
    }

    @Override
    public boolean isValid(final ISignature signature, ConstraintValidatorContext context) {
        return validSignature(signature);
    }

    private boolean validSignature(final ISignature signature) {

        if(signature == null)
            return false;

        byte image [] = signature.getImage();
        int imageSize = signature.getSize();

        if(image == null || image.length <= 0 || image.length != imageSize || imageSize > MAX_SIG_SIZE)
            return false;

        final String [] mimeType = tika.detect(signature.getImage()).split("/");

        return mimeType[1].equals("jpeg");
    }
}
