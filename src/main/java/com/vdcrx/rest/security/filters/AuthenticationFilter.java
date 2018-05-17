package com.vdcrx.rest.security.filters;

import com.vdcrx.rest.api.v1.mapper.security.AuthorizedUserMapper;
import com.vdcrx.rest.api.v1.model.dto.JwtAuthorizedUserDto;
import com.vdcrx.rest.exceptions.controller_advice.RestResponseExceptionHandler;
import com.vdcrx.rest.security.JwtTokenUtil;
import com.vdcrx.rest.security.entry_points.AuthenticationFilterEntryPoint;
import com.vdcrx.rest.security.user_details.AuthorizedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.vdcrx.rest.constants.ApiConstants.LOGIN_PATH;
import static com.vdcrx.rest.constants.SecurityConstants.HEADER_AUTH;
import static com.vdcrx.rest.constants.SecurityConstants.HEADER_BASIC;
import static com.vdcrx.rest.constants.SecurityConstants.HEADER_BEARER;

/**
 * Authentication filter class
 *
 * @author Ranel del Pilar
 */

public class AuthenticationFilter extends BasicAuthenticationFilter {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestResponseExceptionHandler exceptionHandler;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthorizedUserMapper authorizedUserMapper;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                AuthenticationFilterEntryPoint authenticationFilterEntryPoint) {
        super(authenticationManager, authenticationFilterEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String method = request.getMethod();
        String URI = request.getRequestURI();

        if(method.equals("POST") && URI.equals(LOGIN_PATH)) {
            String header = request.getHeader(HEADER_AUTH);
            if (header != null && header.startsWith(HEADER_BASIC)) {
                super.doFilterInternal(request, response, chain);
            } else {
                this.exceptionHandler
                        .handleAuthenticationCredentialsNotFoundException(
                                new AuthenticationCredentialsNotFoundException("Authentication credentials not found"), request, response);
            }
        } else if(!method.equals("POST") && URI.equals(LOGIN_PATH)) {
            this.exceptionHandler
                    .handleHttpRequestMethodNotSupported(new HttpRequestMethodNotSupportedException(method), request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {

        AuthorizedUser authorizedUser = (AuthorizedUser) authResult.getPrincipal();
        JwtAuthorizedUserDto dto = authorizedUserMapper.mapToJwtUserDto(authorizedUser);

        final String token = jwtTokenUtil.generateToken(authorizedUser.getUsername(), dto);
        response.addHeader(HEADER_AUTH, HEADER_BEARER + token);

        LOG.debug("Successful authentication for user - " + authorizedUser.getUsername());
    }
}
