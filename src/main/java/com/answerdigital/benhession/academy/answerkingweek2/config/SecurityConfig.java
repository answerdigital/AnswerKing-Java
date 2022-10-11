package com.answerdigital.benhession.academy.answerkingweek2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("paul").password("{noop}secret").authorities("ROLE_USER")
                .and()
                .withUser("john").password("{noop}secret").authorities("ROLE_USER")
                .and()
                .withUser("ringo").password("{noop}secret").authorities("ROLE_USER")
                .and()
                .withUser("george").password("{noop}secret").authorities("ROLE_USER");
    }
}
