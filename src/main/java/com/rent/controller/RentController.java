/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.controller;

import com.rent.entity.User;
import com.rent.service.UserService;
import java.security.Principal;
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
    public String roleSelection(@RequestParam ("roleselector") String roleName , Principal principal) {
        System.err.println("roleSelection controller");
        System.err.println("Principal name: " + principal.getName());
        System.err.println("Chosen role: " + roleName);
        userService.setSelectedRole(principal.getName(), roleName);
        
        return "redirect:/dashboard";
    }
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
