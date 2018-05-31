package com.vdcrx.rest.exceptions.controller_advice;

import com.vdcrx.rest.exceptions.*;
import com.vdcrx.rest.exceptions.errors.ApiError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.NoArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

/**
 * Rest response exception handler
 *
 * Application wide exception handler
 *
 * @author Ranel del Pilar
 */

@NoArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Jackson2JsonObjectMapper jackson2JsonObjectMapper;

    /**
     * Default Exception handler.
     *
     * @param ex    Exception
     * @param req   HttpServletRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler({Exception.class})
    public void defaultErrorHandler(Exception ex,
                                    HttpServletRequest req,
                                    HttpServletResponse res) {

        LOG.error("Exception thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        INTERNAL_SERVER_ERROR));
    }


    /**
     * Overloaded handler for HttpRequestMethodNotSupportedException.
     *
     * @param ex    HttpRequestMethodNotSupportedException
     * @param req   HttpServletRequest
     * @param res   HttpServletResponse
     */
    public void handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                    HttpServletRequest req,
                                                    HttpServletResponse res) {

        LOG.error("HttpRequestMethodNotSupportedException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        METHOD_NOT_ALLOWED));
    }


    /**
     * Handles account already exist in the database.
     * https://stackoverflow.com/questions/3825990/http-response-code-for-post-when-resource-already-exists
     *
     * @param ex    AccountExistException
     * @param req   HttpServletRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler({AccountExistException.class})
    public void handleAccountExistException(AccountExistException ex,
                                            HttpServletRequest req,
                                            HttpServletResponse res) {

        LOG.error("AccountExistException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        CONFLICT));
    }


    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex    ConstraintViolationException
     * @param req   HttpServletRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public void handleConstraintViolation(javax.validation.ConstraintViolationException ex,
                                          HttpServletRequest req,
                                          HttpServletResponse res) {

        LOG.error("ConstraintViolationException thrown");

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations ) {
            strBuilder.append(violation.getMessage());
        }

        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        strBuilder.toString(),
                        ex.getConstraintViolations(),
                        BAD_REQUEST));
    }


    /**
     * Handles java.lang.NullPointerException.
     *
     * @param ex    java.lang.NullPointerException
     * @param req   HttpServletRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(java.lang.NullPointerException.class)
    public void handleNullPointerException(java.lang.NullPointerException ex,
                                           HttpServletRequest req,
                                           HttpServletResponse res) {

        LOG.error("java.lang.NullPointerException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        BAD_REQUEST));
    }


    /**
     * Handles javax.validation.ValidationException.
     *
     * @param ex    javax.validation.ValidationException
     * @param req   HttpServletRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(javax.validation.ValidationException.class)
    public void handleValidationException(javax.validation.ValidationException ex,
                                          HttpServletRequest req,
                                          HttpServletResponse res) {

        LOG.error("javax.validation.ValidationException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        BAD_REQUEST));
    }


    /**
     * Handles org.springframework.dao.InvalidDataAccessApiUsageException.
     *
     * @param ex    org.springframework.dao.InvalidDataAccessApiUsageException
     * @param req   HttpServletRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(org.springframework.dao.InvalidDataAccessApiUsageException.class)
    public void InvalidDataAccessApiUsageException(org.springframework.dao.InvalidDataAccessApiUsageException ex,
                                                   HttpServletRequest req,
                                                   HttpServletResponse res) {

        LOG.error("InvalidDataAccessApiUsageException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        BAD_REQUEST));
    }


    /**
     * Handles java.lang.IllegalArgumentException.
     *
     * @param ex    java.lang.IllegalArgumentException
     * @param req   HttpServletRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(java.lang.IllegalArgumentException.class)
    public void handleIllegalArgumentException(java.lang.IllegalArgumentException ex,
                                               HttpServletRequest req,
                                               HttpServletResponse res) {

        LOG.error("IllegalArgumentException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        BAD_REQUEST));
    }


    /**
     * Handles com.fasterxml.jackson.databind.JsonMappingException.
     *
     * @param ex    com.fasterxml.jackson.databind.JsonMappingException
     * @param req   HttpServletRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(com.fasterxml.jackson.databind.JsonMappingException.class)
    public void handleJsonMappingException(com.fasterxml.jackson.databind.JsonMappingException ex,
                                           HttpServletRequest req,
                                           HttpServletResponse res) {

        LOG.error("JsonMappingException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        BAD_REQUEST));
    }


    /**
     * Handles javax.persistence.EntityNotFoundException.
     *
     * @param ex    EntityNotFoundException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    public void handleEntityNotFoundPersistenceException(javax.persistence.EntityNotFoundException ex,
                                                         HttpServletRequest req,
                                                         HttpServletResponse res) {

        LOG.error("EntityNotFoundException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        NOT_FOUND));
    }


    /**
     * Handles IEntity Not Found Exception.
     * Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     * Same as ResourceNotFoundException.
     *
     * @param ex    EntityNotFoundException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected void handleEntityNotFoundException(EntityNotFoundException ex,
                                                 HttpServletRequest req,
                                                 HttpServletResponse res) {

        LOG.error("EntityNotFoundException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        NOT_FOUND));
    }


    /**
     * Handles Resource Not Found Exception. Same as EntityNotFoundException.
     *
     * @param ex    ResourceNotFoundException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleResourceNotFoundException(ResourceNotFoundException ex,
                                                HttpServletRequest req,
                                                HttpServletResponse res) {

        LOG.error("ResourceNotFoundException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        NOT_FOUND));
    }


    /**
     * Handles Passwords Mismatched Exception
     *
     * @param ex    PasswordsMismatchException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(PasswordsMismatchException.class)
    public void handlePasswordsMismatchException(PasswordsMismatchException ex,
                                                 HttpServletRequest req,
                                                 HttpServletResponse res) {

        LOG.error("PasswordsMismatchException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        BAD_REQUEST));
    }


    /**
     * Handles Transaction System Exception.
     *
     * @param ex    TransactionSystemException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(TransactionSystemException.class)
    public void handleTransactionSystemException(TransactionSystemException ex,
                                                 HttpServletRequest req,
                                                 HttpServletResponse res) {

        LOG.error("TransactionSystemException thrown");

        String msg = ex.getMostSpecificCause().getMessage();

        if(ex.getCause().getCause() instanceof javax.validation.ConstraintViolationException) {
            javax.validation.ConstraintViolationException t = (javax.validation.ConstraintViolationException)ex.getCause().getCause();
            Iterator<ConstraintViolation<?>> it = t.getConstraintViolations().iterator();
            if(it.hasNext()) {
                msg = it.next().getMessage();
            }

        }

        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        msg,
                        BAD_REQUEST));
    }


    /**
     * Handles Data Integrity Violation Exception, inspects the cause for different DB violations.
     *
     * @param ex    DataIntegrityViolationException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                             HttpServletRequest req,
                                             HttpServletResponse res) {

        LOG.error("DataIntegrityViolationException thrown");
        HttpStatus status = (ex.getCause() instanceof ConstraintViolationException)
                ? CONFLICT
                : INTERNAL_SERVER_ERROR;

        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMostSpecificCause().getMessage(),
                        status));
    }


    /**
     * Handles Bad Credential Exception.
     *
     * @param ex    BadCredentialsException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(BadCredentialsException.class)
    public void handleBadCredentialsException(BadCredentialsException ex,
                                              HttpServletRequest req,
                                              HttpServletResponse res) {

        LOG.error("BadCredentialsException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        NOT_FOUND));
    }


    /**
     * Handles Bad Authentication Exception.
     *
     * @param ex    AuthenticationException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(AuthenticationException ex,
                                              HttpServletRequest req,
                                              HttpServletResponse res) {

        LOG.error("AuthenticationException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        UNAUTHORIZED));
    }


    /**
     * Handles Expired JWT Exception.
     *
     * @param ex    ExpiredJwtException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public void handleExpiredJwtException(ExpiredJwtException ex,
                                          HttpServletRequest req,
                                          HttpServletResponse res) {

        LOG.error("ExpiredJwtException thrown)");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        UNAUTHORIZED));
    }

    /**
     * Handles Unsupported JWT Exception.
     *
     * @param ex    UnsupportedJwtException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(UnsupportedJwtException.class)
    public void handleUnsupportedJwtException(UnsupportedJwtException ex,
                                              HttpServletRequest req,
                                              HttpServletResponse res) {

        LOG.error("UnsupportedJwtException thrown)");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        UNAUTHORIZED));
    }

    /**
     * Handles Malformed JWT Exception.
     *
     * @param ex    MalformedJwtException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(MalformedJwtException.class)
    public void handleMalformedJwtException(MalformedJwtException ex,
                                            HttpServletRequest req,
                                            HttpServletResponse res) {

        LOG.error("MalformedJwtException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        UNAUTHORIZED));
    }

    /**
     * Handles JWT SignatureEntity Exception (Tampered JWT SignatureEntity)
     *
     * @param ex    SignatureException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(SignatureException.class)
    public void handleSignatureException(SignatureException ex,
                                         HttpServletRequest req,
                                         HttpServletResponse res) {

        LOG.error("SignatureException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        UNAUTHORIZED));
    }


    /**
     * Handles UTF-8 encoding exception.
     *
     * @param ex    UnsupportedEncodingException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(UnsupportedEncodingException.class)
    public void handleUnsupportedEncodingException(UnsupportedEncodingException ex,
                                                   HttpServletRequest req,
                                                   HttpServletResponse res) {

        LOG.error("UnsupportedEncodingException thrown)");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        UNAUTHORIZED));
    }


    /**
     * Handles Access Denied Exception.
     *
     * @param ex    Authentication
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException ex,
                                            HttpServletRequest req,
                                            HttpServletResponse res) {

        LOG.error("AccessDeniedException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        FORBIDDEN));
    }


    /**
     * Handles Authentication Credentials Not Found Exception
     *
     * @param ex    Authentication
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public void handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex,
                                                                 HttpServletRequest req,
                                                                 HttpServletResponse res) {

        LOG.error("AuthenticationCredentialsNotFoundException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        BAD_REQUEST));
    }


    /**
     * Handles Internal Authentication Service Exception
     *
     * @param ex    Authentication
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public void handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex,
                                                             HttpServletRequest req,
                                                             HttpServletResponse res) {

        LOG.error("InternalAuthenticationServiceException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        UNAUTHORIZED));
    }


    /**
     * Catch all controller path not found (/**)
     *
     * @param ex    HandlerNotFoundException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(HandlerNotFoundException.class)
    public void handleHandlerNotFoundException(HandlerNotFoundException ex,
                                               HttpServletRequest req,
                                               HttpServletResponse res) {

        LOG.error("HandlerNotFoundException thrown");
        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getMessage(),
                        BAD_REQUEST));
    }


    /**
     * Handles HttpRequestMethodNotSupportedException
     * @param ex        HttpRequestMethodNotSupportedException
     * @param status    HttpStatus
     * @param request   WebRequest
     * @return          The ApiError object
     */
    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                      HttpHeaders headers,
                                                                      HttpStatus status,
                                                                      WebRequest request) {

        LOG.error("HttpRequestMethodNotSupportedException thrown");

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String method = servletWebRequest.getHttpMethod().toString();
        String URI = servletWebRequest.getRequest().getRequestURI();

        return buildResponseEntity(method, URI, ex.getMessage(), headers, status);
    }


    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return        The ApiError object
     */
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {

        LOG.error("HttpMessageNotReadableException thrown");

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String method = servletWebRequest.getHttpMethod().toString();
        String URI = servletWebRequest.getRequest().getRequestURI();

        String exMessage = ex.getMessage();
        String message = exMessage.substring(0, exMessage.indexOf(";"));

        return buildResponseEntity(method, URI, message, headers, status);
    }


    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return        The ApiError object
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {

        LOG.error("MethodArgumentNotValidException thrown");

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String method = servletWebRequest.getHttpMethod().toString();
        String URI = servletWebRequest.getRequest().getRequestURI();
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return buildResponseEntity(method, URI, message, headers, status);
    }


    /**
     * Handle MethodArgumentTypeMismatchException
     *
     * @param ex      MethodArgumentTypeMismatchException
     * @param req   HttpRequest
     * @param res   HttpServletResponse
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                 HttpServletRequest req,
                                                 HttpServletResponse res) {

        LOG.error("MethodArgumentTypeMismatchException thrown");

        responseHelper(res,
                buildResponseEntity(req.getMethod(),
                        req.getRequestURI(),
                        ex.getCause().getMessage(),
                        BAD_REQUEST));
    }


//    /**
//     * Handle MethodArgumentTypeMismatchException
//     *
//     * @param ex      MethodArgumentTypeMismatchException
//     * @param headers HttpHeaders
//     * @param status  HttpStatus
//     * @param request WebRequest
//     * @return        The ApiError object
//     */
//    @ExceptionHandler
//    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
//                                                                   HttpHeaders headers,
//                                                                   HttpStatus status,
//                                                                   WebRequest request) {
//
//        LOG.error("MethodArgumentTypeMismatchException thrown");
//
//        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
//        String method = servletWebRequest.getHttpMethod().toString();
//        String URI = servletWebRequest.getRequest().getRequestURI();
//
//        String exMessage = ex.getMessage();
//        String message = exMessage.substring(0, exMessage.indexOf(";"));
//
//        return buildResponseEntity(method, URI, message, headers, status);
//    }


    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return        The ApiError object
     */
    @Override
    public ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {

        LOG.error("HttpMessageNotWritableException thrown");

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String method = servletWebRequest.getHttpMethod().toString();
        String URI = servletWebRequest.getRequest().getRequestURI();

        String exMessage = ex.getMessage();
        String message = exMessage.substring(0, exMessage.indexOf(";"));

        return buildResponseEntity(method, URI, message, headers, status);
    }


    /**
     *  Handle ServletRequestBindingException.
     *
     * @param ex        ServletRequestBindingException
     * @param headers   HttpHeaders
     * @param status    HttpStatus
     * @param request   WebRequest
     * @return          The ApiError object
     */
    @Override
    public ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                       HttpHeaders headers,
                                                                       HttpStatus status,
                                                                       WebRequest request) {

        LOG.error("ServletRequestBindingException thrown");

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;

        String method = servletWebRequest.getHttpMethod().toString();
        String URI = servletWebRequest.getRequest().getRequestURI();
        String exMessage = ex.getMessage();
        String message = exMessage.substring(0, exMessage.indexOf(";"));

        return buildResponseEntity(method, URI, message, headers, status);

    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return        The ApiError object
     */
    @Override
    public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        LOG.error("HttpMediaTypeNotSupportedException thrown");

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");

        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;

        String method = servletWebRequest.getHttpMethod().toString();
        String URI = servletWebRequest.getRequest().getRequestURI();

        return buildResponseEntity(method, URI, builder.substring(0, builder.length() - 2), headers, status);
    }


    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return        The ApiError object
     */
    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                       HttpHeaders headers,
                                                                       HttpStatus status,
                                                                       WebRequest request) {

        LOG.error("MissingServletRequestParameterException thrown");

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String method = servletWebRequest.getHttpMethod().toString();
        String URI = servletWebRequest.getRequest().getRequestURI();
        String message = ex.getParameterName() + " parameter is missing";

        return buildResponseEntity(method, URI, message, headers, status);
    }


    private ResponseEntity<Object> buildResponseEntity(String method,
                                                       String URI,
                                                       String msg,
                                                       HttpStatus status) {

        ApiError apiError = new ApiError(method, URI, msg, status);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


    private ResponseEntity<Object> buildResponseEntity(String method,
                                                       String URI,
                                                       String msg,
                                                       Set<ConstraintViolation<?>> constraintViolations,
                                                       HttpStatus status) {

        ApiError apiError = new ApiError(method, URI, msg, status);
        apiError.addValidationErrors(constraintViolations);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


    private ResponseEntity<Object> buildResponseEntity(String method,
                                                       String URI,
                                                       String msg,
                                                       HttpHeaders headers,
                                                       HttpStatus status) {

        ApiError apiError = new ApiError(method, URI, msg, status);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }


    private void responseHelper(HttpServletResponse response,
                                ResponseEntity<Object> responseEntity) {

        // Don't overwrite buffer when it's not empty
        if(response.isCommitted())
            return;

        try {
            String json = jackson2JsonObjectMapper.toJson(responseEntity.getBody());
            response.setStatus(responseEntity.getStatusCodeValue());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().write(json);
            response.flushBuffer();
        } catch (Exception e) {
            LOG.error("Exception thrown in responseHelper method");
        }
    }
}
