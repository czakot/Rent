package com.rent.controller;

import com.rent.domain.menu.Menu;
import com.rent.service.UserDetailsImpl;
import com.rent.utility.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/userprofile")
public class UserProfileController {

    private Menu menu;

    @RequestMapping("/basics")
    public String basics(HttpServletRequest request, Model model, Authentication authentication) {
        UserDetailsImpl userDetails = Users.getUserDetails(authentication);
        model.addAttribute("email", userDetails.getUsername());
        model.addAttribute("fullName", userDetails.getFullName());
        model.addAttribute("roles", userDetails.getRoles());
        return request.getRequestURI();
    }

    @RequestMapping("/preferences")
    public String preferences(HttpServletRequest request, Model model) {
        return request.getRequestURI();
    }

    @Autowired
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @ModelAttribute("menu")
    public Menu propagateChangedMenu(HttpServletRequest request) {
        menu.setSelectedTabByControllerUri(request.getRequestURI());
        return menu;
    }

}

