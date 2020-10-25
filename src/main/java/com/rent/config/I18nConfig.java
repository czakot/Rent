/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.config;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 *
 * @author czakot
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

  @Bean
  public MessageSource messageSource() {
      ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//      messageSource.setBasename("messages");
      messageSource.setBasenames("messages/messages",
                                "messages/auth_messages",
                                "messages/layout_messages",
                                "messages/menu_messages");
      messageSource.setDefaultEncoding("UTF-8");
      return messageSource;
  }

/*  
//        default Bean name 
//            Component class => created from class name
//            Bean source method => method name
//            Both of them can be overridden by name annotation argument
//  
//        Ambiguation: more Beans with same type
//            For default annotate Bean with @Primary
//            Qualifier("...")
//            Using Bean name as the name of the field to inject
  
  @Bean(name = "businessMessageSource")
  public MessageSource businessMessageSource() {
      ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
      messageSource.setBasename(basename);
      messageSource.setBasenames(basenames);
      return messageSource;
  }
  
// @Autowire
// MessageSource businessMessageSource;

// @Autowire
// @Qualifer("businessMessageSource")
// MessageSource messageSource;
*/
  
  @Bean
  public LocaleResolver localeResolver() {
      CookieLocaleResolver clr = new CookieLocaleResolver();
//      clr.setDefaultLocale(new Locale("hu"));
      clr.setCookieSecure(true);
      clr.setCookieName("rentLocal");
      return clr;

//      SessionLocaleResolver slr = new SessionLocaleResolver();
//      slr.setDefaultLocale(new Locale("hu"));
//      return slr;
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
      LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
      lci.setParamName("lang");
      return lci;
  }  

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(localeChangeInterceptor());
  }

}
