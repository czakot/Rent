package com.rent.config;

import com.rent.domain.menu.MenuInitTree;
import com.rent.domain.menu.MenuInitValueNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.Arrays;

// @EnableGlobalMethodSecurity(securedEnabled = true) // for usage of @Secured("<role>") to secure a method
@Configuration()
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true/*, securedEnabled = true, jsr250Enabled = false*/)
/*
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
}
*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
//                .antMatchers("/db/**").hasRole("ADMIN") // for H2Console
                .antMatchers("/", "/index").anonymous()
                .antMatchers("/login", "/registration").anonymous()
                .antMatchers("/getauthdata", "/registrationprocess", "/activation/*").anonymous()
                .antMatchers("/forgottenpassword").anonymous()
                .antMatchers("/homebyuserrole", "/roleselection").authenticated()
                ;

        generateAntMatchersFromMenuInitTree(httpSec);

        httpSec
                .authorizeRequests()
                .anyRequest().denyAll()
//                .anyRequest().authenticated()
                .and()

            .requiresChannel().anyRequest().requiresSecure() // instead of server.ssl.enabled = true
//            .portMapper().http(8080).mapsTo(8443)
                .and()

            .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/homebyuserrole", true)
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

    private void generateAntMatchersFromMenuInitTree(HttpSecurity httpSec) throws Exception {
        for (MenuInitValueNode menuInitValueNode : menuInitTree) {
            generateAntMatchersFromMenuInitValueNode(httpSec, menuInitValueNode);
        }
    }

    private void generateAntMatchersFromMenuInitValueNode(HttpSecurity httpSec,  MenuInitValueNode menuInitValueNode) {

        String controllerUri = menuInitValueNode.getControllerUri();
        String menuselectUri = getMenuselectUriFromMenuInitValueNode(menuInitValueNode);

        if (controllerUri != null || menuselectUri != null) {
            String[] roleNames = getRoleNamesFromMenuInitValueNode(menuInitValueNode);

            generateAntMatchers(httpSec, controllerUri, roleNames);
            generateAntMatchers(httpSec, menuselectUri, roleNames);
        }
    }

    private String getMenuselectUriFromMenuInitValueNode(MenuInitValueNode node) {
        String menuselectUri = null;
        if (node.getParent() == null) {
            String controllerUri = node.getControllerUri();
            menuselectUri = "/menuselect" +
                    (controllerUri != null ? controllerUri : '/' + node.getReference());
        }
        return menuselectUri;
    }

    private String[] getRoleNamesFromMenuInitValueNode(MenuInitValueNode node) {
        return node.getAvailableForRoles().stream().map(Enum::name).toArray(String[]::new);
    }

    private void generateAntMatchers(HttpSecurity httpSec, String uri, String[] roles) {
        if (uri != null) {
            try {
                httpSec.authorizeRequests().antMatchers(uri).hasAnyRole(roles);
                logger.debug(String.format("Generated antMatchers: \"%s\" => %s", uri, Arrays.asList(roles)));
            } catch (Exception e) {
                logger.error(String.format("Could not create antMatchers() (uri: '%s' , roles: %s)\n", uri, Arrays.asList(roles)));
            }
        }
    }

}
