/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.controller;

import com.rent.service.UserDetailsImpl;
import com.rent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author czakot
 */
@Controller
public class RentController {
    
    UserService userService;
    
    @RequestMapping({"/", "/index"})
    public String index(Authentication authentication, Model model) {
        if (authentication!=null && authentication.isAuthenticated()) {
            return "redirect:/dashboard";
        }
        model.addAttribute("adminExists", userService.adminExists());
        return "/index";
    }

    @RequestMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @PostMapping("/roleselection")
    public String roleSelection(@RequestParam ("roleselector") String roleName, Authentication authentication) {
        setSelectedRoleOfAuthenticatedUser(authentication, roleName);
        return "redirect:/dashboard";
    }
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    private void setSelectedRoleOfAuthenticatedUser(Authentication authentication, String roleName) {
        ((UserDetailsImpl)authentication.getPrincipal()).setUserSelectedRoleByName(roleName);
    }

}
