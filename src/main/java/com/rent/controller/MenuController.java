/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.controller;

import com.rent.domain.Role;
import com.rent.domain.menu.Menu;
import com.rent.service.UserDetailsImpl;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
public class MenuController {
    
    Menu menu;

    @RequestMapping("/homebyuserrole")
    public String homeByUserRole(Authentication authentication) {
        Role role = Role.valueOf(getSelectedRoleOfAuthenticatedUser(authentication));
        menu.setMenu(role);
        return "redirect:/" + menu.getSelectedMenuItem().getContentPageUri();
    }
    
    @RequestMapping({"/noticeboard", "/userprofile", "/dashboard"})
    public String content(Model model, HttpServletRequest request) {
        String target = request.getRequestURI().substring(1);
        menu.setSelectedMenuItem(target);
        model.addAttribute("menuItems", menu.getMenuItems());
        model.addAttribute("selectedMenuItem", menu.getSelectedMenuItem());
        return target;
    }
    
    @PostMapping("/roleselection")
    public String roleSelection(@RequestParam ("roleselector") String roleName, Authentication authentication) {
        setSelectedRoleOfAuthenticatedUser(authentication, roleName);
        return homeByUserRole(authentication);
    }
    
    private String getSelectedRoleOfAuthenticatedUser(Authentication authentication) {
        return ((GrantedAuthority)((UserDetailsImpl)authentication.getPrincipal()).getAuthorities().toArray()[0]).getAuthority();
    }
    
    private void setSelectedRoleOfAuthenticatedUser(Authentication authentication, String roleName) {
        ((UserDetailsImpl)authentication.getPrincipal()).setUserSelectedRoleByName(roleName);
    }

    @Autowired
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
}
