package com.vdcrx.rest.controllers.v1;

import com.vdcrx.rest.api.v1.model.dto.PasswordDto;
import com.vdcrx.rest.exceptions.HandlerNotFoundException;
import com.vdcrx.rest.services.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

import static com.vdcrx.rest.constants.ApiConstants.LOGIN_PATH;

/**
 * Base controller
 *
 * @author Ranel del Pilar
 */

@RestController
@CrossOrigin(exposedHeaders = "error, content-type")
public class BaseController {

    private PasswordService passwordService;

    @Autowired
    public BaseController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @RequestMapping(value = "/**")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noHandlerMappingFound(HttpServletRequest request) throws HandlerNotFoundException {
        throw new HandlerNotFoundException("No handler mapping for request method '" + request.getMethod() + "'");
    }

    @PostMapping({LOGIN_PATH})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> login() {
        return ResponseEntity.ok("Authenticated!");
    }

    @PatchMapping(value = "/password/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@PathVariable @Valid final UUID id,
                               @Valid @Validated @RequestBody final PasswordDto resource) {
        passwordService.updatePassword(id, resource.getReplacement(), resource.getCurrent());
    }
}
