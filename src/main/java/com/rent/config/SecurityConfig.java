package com.rent.config;

import com.rent.domain.Role;
import com.rent.domain.menu.MenuInitTree;
import com.rent.domain.menu.MenuInitValueNode;
import com.rent.entity.Matcher;
import com.rent.entity.MenuNode;
import com.rent.repo.MenuNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

// @EnableGlobalMethodSecurity(securedEnabled = true) // for usage of @Secured("<role>") to secure a method
@Configuration()
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true/*, securedEnabled = true, jsr250Enabled = false*/)
/*
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
}
*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MenuInitTree menuInitTree;

    @Autowired
    private UserDetailsService userDetailsService;

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
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

/*
    @Bean
    public SessionRegistry getSessionRegistry() {
        return new SessionRegistryImpl();
    }
*/

    @Override
    protected void configure(final HttpSecurity httpSec) throws Exception {
        httpSec
            .csrf().disable()
              // avoiding console warning: "Strict-Transport-Security: The connection to the site is untrustworthy, so the specified header was ignored."
            .headers()
                .httpStrictTransportSecurity().disable()
                .frameOptions().sameOrigin()
                .and()

            .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/favicon.ico").permitAll()
                .antMatchers("/db/**").permitAll() // for H2Console
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/login", "/registration").permitAll()
                .antMatchers("/getauthdata", "/registrationprocess", "/activation/*").permitAll()
                .antMatchers("/forgottenpassword").permitAll()

                .antMatchers("/realestatelist").permitAll()
//                .antMatchers("/realestatelist").hasRole("OWNER")

//                .antMatchers("/homebyuserrole", "/menuselect/*", "/roleselection").authenticated()
                ;

        antMatchersFromControllerUriDatabase(httpSec, false); // true for printing Matchers

        httpSec
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()

            .requiresChannel().anyRequest().requiresSecure() // instead of server.ssl.enabled = true
//            .portMapper().http(8080).mapsTo(8443)
                .and()

            .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/homebyuserrole")
//                .defaultSuccessUrl("/dashboard")
//                .successForwardUrl("/index")
//                .failureForwardUrl("/authpage?pageMode=login&")
                .and()

            .logout()
                .logoutSuccessUrl("/login?logout=success")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()

            .sessionManagement()
                .sessionFixation().newSession()
                .maximumSessions(1)
                .expiredUrl("/login?expired=true")
        // for default operation hashCode and equals in User and UserDetailsImpl were needed for "same" user recognition
//                .sessionRegistry(getSessionRegistry())
                ;

    }

    private void antMatchersFromControllerUriDatabase(HttpSecurity httpSec, boolean doPrint) throws Exception {

        for (MenuInitValueNode menuInitValueNode : menuInitTree) {
            System.out.println(menuInitValueNode);
            String uri = menuInitValueNode.getControllerUri();
            if (uri != null) {
                Set<Role> roles = menuInitValueNode.getAvailableForRoles();
                if (doPrint) {
                    System.out.printf("%s => %s\n",uri, roles);
                }
                String[] rolesArray = roles.stream().map(Enum::name).toArray(String[]::new);
                System.out.printf("[%s]", uri);
                for (String s : rolesArray) {
                    System.out.printf("[%s]", s);
                    httpSec.authorizeRequests()
                            .antMatchers(uri).hasRole(s);
                }
                System.out.println();
/*
                httpSec.authorizeRequests()
                        .antMatchers(uri).hasAnyRole(rolesArray);
*/
//                        .antMatchers(uri).hasAnyRole(roles.stream().map(Enum::name).toArray(String[]::new));
            }
        }
    }

}
