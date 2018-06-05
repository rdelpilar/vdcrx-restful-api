package com.vdcrx.rest.services;

import com.vdcrx.rest.api.v1.model.dto.SignatureDto;
import org.springframework.dao.DataAccessException;

import java.util.UUID;

/**
 * Signature service interface
 *
 * @author Ranel del Pilar
 */

public interface SignatureService {

    /* Create */
    SignatureDto createSignature(final UUID id, final SignatureDto resource) throws DataAccessException;

    /* Update */
    SignatureDto updateSignature(final UUID id, final UUID sig_id, SignatureDto resource) throws DataAccessException;

    /* Delete */
    void deleteSignature(final UUID id, final UUID sig_id) throws DataAccessException;

    /* Retrieve */
    SignatureDto findSignatureById(final UUID id, final UUID sig_id);
    SignatureDto findSignatureByPersonId(final UUID id);
    SignatureDto findSignatureByUsername(final String username);
    SignatureDto findSignatureByEmail(final String email);
}
