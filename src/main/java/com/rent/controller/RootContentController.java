/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.controller;

import com.rent.domain.Role;
import com.rent.domain.menu.Menu;
import com.rent.entity.UserProfile;
import com.rent.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author czakot
 */
@Controller
public class RootContentController {

    private Menu menu;

    // todo clean up /activationloggedin (only anonymous access)
    @RequestMapping("/activationloggedin")
    public String activationAnswerWhenAUserLoggedIn(Model model) {
        model.addAttribute("menuItems", menu.getMenuItems());
        return "rootcontent/activationloggedin";
    }

    @RequestMapping("/homebyuserrole")
    public String homeByUserRole(Model model, Authentication authentication) {
        Role role = getPreferredInitialRoleOfAuthenticatedUser(authentication);
        menu.changeRoleTo(role);
        String view = initViewAuthorizedMainContentFrame(model);
        return initViewAuthorizedMainContentFrame(model);
    }

    @RequestMapping(path = "/menuselect/{target}")
    public String menuSelect(@PathVariable String target, Model model) {
        menu.setSelectedMenuItemByControllerUri('/' + target);
        return initViewAuthorizedMainContentFrame(model);
    }

    @RequestMapping({"/noticeboard*", "/userprofile", "/dashboard"})
    public String selectedMenu(HttpServletRequest request) {
        return "rootcontent" + request.getRequestURI();
    }
    
    @PostMapping("/roleselection")
    public String roleSelection(@RequestParam ("roleselector") String roleName, Model model) {
        menu.changeRoleTo(Role.valueOf(roleName));
        return initViewAuthorizedMainContentFrame(model);
    }
    
    private String initViewAuthorizedMainContentFrame(Model model) {
        model.addAttribute("currentRole", menu.getCurrentRole());
        model.addAttribute("menuItems", menu.getMenuItems());
        model.addAttribute("selectedMenuItem", menu.getSelectedMenuItem());
        return "layout/main/maincontentframe";
    }

    private Role getPreferredInitialRoleOfAuthenticatedUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserProfile userProfile = userDetails.getUserProfile();

        Role role = userProfile==null ? null : userProfile.getPreferredInitialRole();
        return role != null ? role : Role.getMostWeightedRoleFromSet(userDetails.getRoles());
    }

    @Autowired
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
}
