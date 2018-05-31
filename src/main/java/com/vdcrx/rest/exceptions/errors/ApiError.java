package com.vdcrx.rest.exceptions.errors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class ApiError {

    private Long timestamp;
    private String method;
    private Integer code;
    private HttpStatus status;
    private String path;
    private String message;
    @JsonIgnore
    private String debugMessage;
    @JsonIgnore
    private List<ApiSubError> subErrors;


    public ApiError() {
    }

    public ApiError(String method, String path, HttpStatus status) {
        this();
        this.method = method;
        this.code = status.value();
        this.path = path;
        this.status = status;
        this.timestamp = Instant.now().toEpochMilli();
    }

    public ApiError(String method, String path, HttpStatus status, Throwable ex) {
        this();
        this.method = method;
        this.code = status.value();
        this.path = path;
        this.status = status;
        this.message = "Unexpected error";
        this.timestamp = Instant.now().toEpochMilli();
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(String method, String path, String message, HttpStatus status /*, Throwable ex*/) {
        this();
        this.method = method;
        this.code = status.value();
        this.path = path;
        this.message = message;
        this.status = status;
        this.timestamp = Instant.now().toEpochMilli();
        //this.debugMessage = ex.getLocalizedMessage();
    }

    private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addSubError(new ApiValidationError(object, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError) {
        this.addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     * @param cv the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
                cv.getRootBeanClass().getSimpleName(),
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage());
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }



    abstract class ApiSubError {

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    class ApiValidationError extends ApiSubError {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;

        ApiValidationError(String object, String message) {
            this.object = object;
            this.message = message;
        }
    }
}

class LowerCaseClassNameResolver extends TypeIdResolverBase {

    @Override
    public String idFromValue(Object value) {
        return value.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
