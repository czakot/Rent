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
        registry.addViewController("/userprofile").setViewName("rootcontent/userprofile");
        registry.addViewController("/contentframe").setViewName("rootcontent/contentframe");
        addViewController(registry, "auth",
				"/login", "/registration");
/*
        addViewController(registry, "rootcontent",
				"/activationloggedin", "/contentframe", "/dashboard", "/noticeboard", "/noticeboard_2", "/userprofile");
*/
//        registry.addViewController("/login").setViewName("auth/login");
//        registry.addViewController("/registration").setViewName("auth/registration");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);		
	}

	private void addViewController(ViewControllerRegistry registry, String viewNamePrefix, String ... urlPaths) {
		for (String urlPath : urlPaths) {
			registry.addViewController(urlPath).setViewName(viewNamePrefix + urlPath);
		}
	}

}
