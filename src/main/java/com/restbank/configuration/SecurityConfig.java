package com.restbank.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_MATCHERS = {
            "/",
            "/endpoints"
    };

    private static final String[] ADMIN_ENDPOINTS = {
            "/api/1.0/roles",
            "/api/1.0/accounts",
            "/api/1.0/accounts/*",
            "/api/1.0/users",
            "/api/1.0/users/*",
            "/api/1.0/transactions",
            "/api/1.0/transactions/*",
            "/api/1.0/credit-cards",
            "/api/1.0/credit-cards/*",
    };

    private static final String[] USER_ENDPOINTS = {
            "/api/1.0/customers",
            "/api/1.0/customers/*",
            "/api/1.0/login"
    };

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();

        http.headers().disable();

        http
                .authorizeRequests().antMatchers(ADMIN_ENDPOINTS).hasAnyAuthority("ADMIN")
            .and()
                .authorizeRequests().antMatchers(USER_ENDPOINTS).hasAnyAuthority(
                        "USER","ADMIN")
            .and()
                .authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll()
        .and()
                .httpBasic()
        .and()
                .csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
}

