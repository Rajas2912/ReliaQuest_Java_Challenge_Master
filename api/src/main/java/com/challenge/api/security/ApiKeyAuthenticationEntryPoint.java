package com.challenge.api.security;

import com.challenge.api.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Without this, Spring Security's default behavior for an unauthenticated request to a non-browser API is a bare
 * 403 with no body. This produces a 401 with the same {@link ErrorResponse} shape used everywhere else in the API,
 * so a caller gets a consistent, informative response regardless of which layer rejected the request.
 */
@Component
public class ApiKeyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    // Injects Spring Boot's autoconfigured ObjectMapper (already has the JSR-310 module registered for Instant)
    // rather than `new ObjectMapper()`, so this serializes ErrorResponse.timestamp the same way the rest of the
    // app's @ExceptionHandler responses do.
    public ApiKeyAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse("Missing or invalid API key")));
    }
}
