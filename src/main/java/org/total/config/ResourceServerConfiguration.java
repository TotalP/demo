/* Copyright 2019 by Avid Technology, Inc. */
package org.total.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pavlo.Fandych
 */

@EnableResourceServer
@RestController
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/oauth/token").permitAll();

        http.requestMatchers().antMatchers("/users/*")
                .and().authorizeRequests().antMatchers("/users/*").access("hasRole ('USER')");
    }
}
