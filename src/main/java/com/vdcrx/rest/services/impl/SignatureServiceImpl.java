package com.vdcrx.rest.services.impl;

import com.vdcrx.rest.api.v1.mapper.entities.SignatureMapper;
import com.vdcrx.rest.api.v1.model.dto.SignatureDto;
import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.domain.entities.Signature;
import com.vdcrx.rest.domain.entities.Veterinarian;
import com.vdcrx.rest.domain.enums.SignatureType;
import com.vdcrx.rest.exceptions.ResourceNotFoundException;
import com.vdcrx.rest.repositories.PersonRepository;
import com.vdcrx.rest.repositories.SignatureRepository;
import com.vdcrx.rest.services.SignatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Signature service implementation
 *
 * @author Ranel del Pilar
 */

@Service
public class SignatureServiceImpl implements SignatureService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    final private SignatureRepository signatureRepository;
    final private SignatureMapper signatureMapper;
    final private PersonRepository personRepository;

    @Autowired
    public SignatureServiceImpl(SignatureRepository signatureRepository,
                                SignatureMapper signatureMapper,
                                PersonRepository personRepository) {
        this.signatureRepository = signatureRepository;
        this.signatureMapper = signatureMapper;
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public SignatureDto createSignature(final UUID id, final SignatureDto resource) throws DataAccessException {
        Assert(id, resource);

        LOG.debug("Creating new signature for person with id '" + id + "'");

        return personRepository
                .findById(id)
                .map(person ->
                        saveAndReturnDto(person, resource)
                ).orElseThrow(() -> new ResourceNotFoundException("No person found with id '" + id + "'"));
    }

    @Override
    @Transactional
    public SignatureDto updateSignature(final UUID id, final UUID sig_id, final SignatureDto resource) throws DataAccessException {
        Assert(id, sig_id, resource);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Updating signature with id '" + sig_id + "'");

        return signatureRepository.findById(sig_id)
                .map(signature -> {
                    String contentType = resource.getContentType();
                    int size = resource.getSize();
                    byte [] image = resource.getImage();

                    if(contentType != null && !contentType.equals(signature.getContentType()))
                        signature.setContentType(contentType);
                    if(size != signature.getSize())
                        signature.setSize(size);
                    if((image.length != signature.getImage().length) && (!Arrays.equals(image, signature.getImage())))
                        signature.setImage(image.clone());

                    return signatureMapper.mapToSignatureDto(signatureRepository.saveAndFlush(signature));
                }).orElseThrow(() -> new ResourceNotFoundException("No signature found with id '" + sig_id + "'"));
    }

    @Override
    @Transactional
    public void deleteSignature(final UUID id, final UUID sig_id) throws DataAccessException {
        Assert(id, sig_id);

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        LOG.debug("Deleting signature with id '" + sig_id + "'");

        signatureRepository.deleteById(sig_id);
    }

    private SignatureDto saveAndReturnDto(final Person person, final SignatureDto resource) {
        Signature signature = signatureMapper.mapToSignature(resource);
        signature.setPerson(person);

        if(person instanceof Veterinarian)
            signature.setSignatureType(SignatureType.VETERINARIAN);

        return signatureMapper.mapToSignatureDto(signatureRepository.saveAndFlush(signature));
    }

    @Override
    @Transactional(readOnly = true)
    public SignatureDto findSignatureById(final UUID id, final UUID sig_Id) {
        Assert(id, sig_Id);

        LOG.debug("Retrieving signature by id '" + id + "'");

        if(!personRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No person found with id '" + id + "'");

        return signatureRepository.findById(sig_Id)
                .map(signatureMapper::mapToSignatureDto)
                .orElseThrow(() -> new ResourceNotFoundException("No signature found with id '" + sig_Id + "'"));
    }

    @Override
    @Transactional
    public SignatureDto findSignatureByPersonId(final UUID id) {
        Assert.notNull(id, "Person id must not be null!");

        LOG.debug("Retrieving signature by person with id '" + id + "'");

        return signatureRepository.findSignatureByPersonId(id)
                .map(signatureMapper::mapToSignatureDto)
                .orElseThrow(() -> new ResourceNotFoundException("No signature found with id '" + id + "'"));
    }


    @Override
    @Transactional(readOnly = true)
    public SignatureDto findSignatureByUsername(final String username) {
        Assert.notNull(username, "Username must not be null!");

        LOG.debug("Retrieving signature for username '" + username + "'");

        return signatureRepository.findSignatureByPersonUsername(username)
                .map(signatureMapper::mapToSignatureDto)
                .orElseThrow(() -> new ResourceNotFoundException("No signature found using '" + username + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public SignatureDto findSignatureByEmail(final String email) {
        Assert.notNull(email, "Email must not be null!");

        LOG.debug("Retrieving signature for email '" + email + "'");

        return signatureRepository.findSignatureByPersonEmail(email)
                .map(signatureMapper::mapToSignatureDto)
                .orElseThrow(() -> new ResourceNotFoundException("No signature found using '" + email + "'"));
    }

    private void Assert(final UUID id, final UUID sig_id) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(sig_id, "Signature id must not be null!");
    }

    private void Assert(final UUID id, final SignatureDto resource) {
        Assert.notNull(id, "Person id must not be null!");
        Assert.notNull(resource, "Signature Dto resource must not be null");
    }

    private void Assert(final UUID id, final UUID sig_id, final SignatureDto resource) {
        Assert(id, resource);
        Assert.notNull(sig_id, "Signature id must not be null!");
    }
}
