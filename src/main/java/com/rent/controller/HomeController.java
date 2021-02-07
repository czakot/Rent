package com.rent.controller;

import com.rent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    UserService userService;

    @RequestMapping({"/", "/index"})
    public String index(Authentication authentication, Model model) {
        if (authentication!=null && authentication.isAuthenticated()) {
            return "redirect:/homebyuserrole";
        }
        model.addAttribute("adminExists", userService.adminExists());
        return "/index";
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
