/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.controller;

import com.rent.service.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author czakot
 */
@Controller
public class RentController {
    
    @RequestMapping("/content")
    public String content() {
        //TODO redirection to starting pages by role
        return "content";
    }
    
    @RequestMapping("/dashboard")
    public String dashboard() {
        //TODO redirection to starting pages by role
        return "dashboard";
    }
    
    @PostMapping("/roleselection")
    public String roleSelection(@RequestParam ("roleselector") String roleName, Authentication authentication) {
        setSelectedRoleOfAuthenticatedUser(authentication, roleName);
        return "redirect:/dashboard";
    }
    
    private void setSelectedRoleOfAuthenticatedUser(Authentication authentication, String roleName) {
        ((UserDetailsImpl)authentication.getPrincipal()).setUserSelectedRoleByName(roleName);
    }

}
