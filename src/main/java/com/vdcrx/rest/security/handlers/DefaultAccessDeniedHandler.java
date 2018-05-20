package com.vdcrx.rest.security.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Default access denied handler classs
 *
 * @author Ranel del Pilar
 */

public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        throw new AccessDeniedException("Client is not authorized to access the resource");
    }
}
