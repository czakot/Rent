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
    protected void configure(final HttpSecurity httpSec) throws Exception {
        httpSec
            .csrf().disable()
              // avoiding console warning: "Strict-Transport-Security: The connection to the site is untrustworthy, so the specified header was ignored."
            .headers()
                .httpStrictTransportSecurity().disable()
//                .frameOptions().sameOrigin()
                ;
        httpSec
            .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/favicon.ico").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/login", "/registration").permitAll()
                .antMatchers("/getauthdata", "/registrationprocess", "/activation/*").permitAll()
                .antMatchers("/forgottenpassword").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
            .requiresChannel().anyRequest().requiresSecure() // instead of server.ssl.enabled = true
//            .portMapper().http(8080).mapsTo(8443)
                .and()
            .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/content")
//                .defaultSuccessUrl("/dashboard")
//                .successForwardUrl("/index")
//                .failureForwardUrl("/authpage?pageMode=login&")
                .and()
            .logout()
                .logoutSuccessUrl("/login?logout=success")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
                ;
    }

}
