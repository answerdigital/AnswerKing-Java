package com.answerdigital.answerking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .httpBasic()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        final UserDetails paul = User.withUsername("paul")
                .password("{noop}secret")
                .authorities("ROLE_USER")
                .build();

        final UserDetails john = User.withUsername("john")
                .password("{noop}secret")
                .authorities("ROLE_USER")
                .build();

        final UserDetails ringo = User.withUsername("ringo")
                .password("{noop}secret")
                .authorities("ROLE_USER")
                .build();

        final UserDetails george = User.withUsername("george")
                .password("{noop}secret")
                .authorities("ROLE_USER")
                .build();

        return new InMemoryUserDetailsManager(paul, john, ringo, george);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                        .ignoring()
                        .antMatchers("/")
                        .antMatchers("/api/swagger/**”,”/api/swagger-ui/**”,”/api/swagger-ui.html”," +
                                "/api/swagger-ui-custom.html", "/webjars/**", "/api/swagger-resources/**",
                                "/api/configuration/**”, ”/api/api-docs/**");
    }
}
