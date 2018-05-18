package com.vdcrx.rest.security.entry_points;

import com.vdcrx.rest.config.ApplicationContextProvider;
import com.vdcrx.rest.exceptions.controller_advice.RestResponseExceptionHandler;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Entry point for Authentication Filter class
 *
 * @author Ranel del Pilar
 */

@NoArgsConstructor
public class AuthenticationFilterEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private RestResponseExceptionHandler exceptionHandler;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException ex) {

        exceptionHandler.handleAuthenticationException(ex, request, response);
    }
}
