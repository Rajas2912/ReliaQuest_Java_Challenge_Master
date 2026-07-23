package com.challenge.api.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Validates a shared API key on each request. Chosen over session/cookie-based auth since the consumer
 * (Employees-R-US) is a server-to-server webhook caller, not a browser client.
 */
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private final String expectedApiKey;

    public ApiKeyAuthFilter(String expectedApiKey) {
        this.expectedApiKey = expectedApiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String providedKey = request.getHeader(API_KEY_HEADER);
        if (expectedApiKey.equals(providedKey)) {
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken("employees-r-us", null, List.of()));
        }
        chain.doFilter(request, response);
    }
}
