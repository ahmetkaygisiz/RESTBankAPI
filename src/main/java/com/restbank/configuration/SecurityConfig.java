package com.restbank.configuration;

import com.restbank.domain.Role;
import com.restbank.repository.RoleRepository;
import com.restbank.utils.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthUserService authUserService;

    private static final String[] PUBLIC_MATCHERS = {
            "/",
            "/endpoints"
    };

    private static final String[] ADMIN_ENDPOINTS = {
            "/api/1.0/roles/**",
            "/api/1.0/accounts/**",
            "/api/1.0/users/**"
    };

    private static final String[] USER_ENDPOINTS = {
            "/api/1.0/user/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.httpBasic().authenticationEntryPoint(new BasicAuthentictionEntryPoint());

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/1.0/login").authenticated()
                .and()
                .authorizeRequests().anyRequest().permitAll();

/*        http.authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS)
                	.permitAll()
                .antMatchers(ADMIN_ENDPOINTS)
                    .hasAuthority("ROLE_ADMIN")
                .antMatchers(USER_ENDPOINTS)
                    .hasAuthority("ROLE_USER");*/

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
