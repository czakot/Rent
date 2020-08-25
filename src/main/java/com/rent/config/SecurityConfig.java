package com.rent.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// @EnableGlobalMethodSecurity(securedEnabled = true) // for usage of @Secured("<role>") to secure a method
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
/*
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
*/
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSec) throws Exception {
        httpSec
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/css/**", "/js/*").permitAll()
                .antMatchers("/getauthdata").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/activation/*").permitAll()
                .antMatchers("/forgottenpassword").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
            .requiresChannel().anyRequest().requiresSecure()
//                .and() // instead of server.ssl.enabled = true
//            .portMapper().http(8080).mapsTo(8443)
                .and()
//            .formLogin().loginPage("/authpage").successForwardUrl("/index").permitAll()
            .formLogin().loginPage("/authpage").defaultSuccessUrl("/index", true).permitAll()
                .and()
            .logout().logoutSuccessUrl("/authpage").permitAll()
//            .formLogin().loginPage("/login").permitAll().failureForwardUrl("/login?redirection=/login")
//                .and()
//            .logout().logoutSuccessUrl("/login?logout").permitAll()
                ;
    }

}
