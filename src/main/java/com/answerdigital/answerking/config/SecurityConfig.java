package com.answerdigital.answerking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] PERMITTED_PATTERNS = {
        "/api/swagger/**",
        "/api/swagger-ui/**",
        "/api/swagger-ui.html",
        "/api/swagger-ui-custom.html",
        "/webjars/**",
        "/api/swagger-resources/**",
        "/api/configuration/**",
        "/api/api-docs/**"
    };

    private static final String COMMON_ROLE = "ROLE_USER";

    private static final String COMMON_PASSWORD = "{noop}secret";

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(PERMITTED_PATTERNS).permitAll()
                    .anyRequest().authenticated()
            )
            .httpBasic()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager(
                Stream.of("paul", "john", "ringo", "george")
                        .map(user ->
                                User.withUsername(user)
                                        .password(COMMON_PASSWORD)
                                        .authorities(COMMON_ROLE)
                                        .build()
                        ).toList()
        );
    }
}
