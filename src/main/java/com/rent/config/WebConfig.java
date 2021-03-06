package com.rent.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(@NotNull ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("auth/login");
//        registry.addViewController("/registration").setViewName("auth/registration");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);		
	}

}
