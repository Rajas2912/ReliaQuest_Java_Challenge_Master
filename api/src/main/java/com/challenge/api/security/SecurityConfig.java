package com.challenge.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Secures every endpoint in the API behind a shared API key (see {@link ApiKeyAuthFilter}). Csrf protection is
 * disabled and sessions are stateless: csrf exists to protect browser form submissions using cookies, neither of
 * which applies to a server-to-server webhook caller authenticating on every request via a header.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${employee-api.api-key}")
    private String apiKey;

    private final ApiKeyAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(ApiKeyAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(new ApiKeyAuthFilter(apiKey), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
