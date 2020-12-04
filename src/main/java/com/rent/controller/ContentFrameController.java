/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.controller;

import com.rent.domain.Role;
import com.rent.domain.authmessage.AuthMessage;
import com.rent.domain.menu.Menu;
import com.rent.service.UserService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author czakot
 */
@Controller
public class ContentFrameController {

    UserService userService;
    Menu menu;

    @RequestMapping({"/", "/index"})
    public String index(Authentication authentication, Model model) {
        if (authentication!=null && authentication.isAuthenticated()) {
            return "redirect:/homebyuserrole";
        }
        model.addAttribute("adminExists", userService.adminExists());
        return "/index";
    }

    @RequestMapping("/activationloggedin")
    public String activationAnswerWhenAUserLoggedIn(Model model) {
        System.out.println(model.getAttribute("messageList"));
        System.out.println(model.getAttribute("activatedUserEmail"));
        model.addAttribute("menuItems", menu.getMenuItems());
        return "/activationloggedin";
    }

    @RequestMapping("/homebyuserrole")
    public String homeByUserRole(Model model, Authentication authentication) {
        Role role = Role.valueOf(userService.getSelectedRoleOfAuthenticatedUser(authentication));
        menu.setMenuByRole(role);
        return initAuthorizedContentFrame(model);
    }
    
    @RequestMapping(path = "/menuselect/{target}")
    public String menuSelect(@PathVariable String target, Model model) {
        menu.setSelectedMenuItem(target);
        return initAuthorizedContentFrame(model);
    }
    
    @RequestMapping({"/noticeboard*", "/userprofile", "/dashboard"})
    public String selectedMenu(HttpServletRequest request) {
        return request.getRequestURI();
    }
    
    @PostMapping("/roleselection")
    public String roleSelection(@RequestParam ("roleselector") String roleName, Authentication authentication) {
        userService.setSelectedRoleOfAuthenticatedUser(authentication, roleName);
        return "redirect:/homebyuserrole";
    }
    
    private String initAuthorizedContentFrame(Model model) {
        model.addAttribute("menuItems", menu.getMenuItems());
        model.addAttribute("selectedMenuItem", menu.getSelectedMenuItem());
        return "/contentframe";
    }
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
}
