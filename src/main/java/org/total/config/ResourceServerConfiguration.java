/* Copyright 2019 by Avid Technology, Inc. */
package org.total.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * @author Pavlo.Fandych
 */

@EnableResourceServer
@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/oauth/token").permitAll();

        httpSecurity.csrf().disable().anonymous().disable().authorizeRequests().antMatchers("/api/users/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/users/**").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/api/users/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/users/**").access("hasRole('ROLE_ADMIN')").and().exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}