package com.rent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/dashboard").setViewName("rootcontent/dashboard");
//        registry.addViewController("/login").setViewName("auth/login");
//        registry.addViewController("/registration").setViewName("auth/registration");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);		
	}

}
