package com.rent.controller;

import com.rent.domain.Role;
import com.rent.domain.menu.Menu;
import com.rent.utility.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainSelectionController {

    private Menu menu;

    @RequestMapping("/homebyuserrole")
    public String homeByUserRole(Model model, Authentication authentication) {
        menu.setRole(Users.getPreferredInitialRole(authentication));
        return initViewMainContentFrame(model);
    }

    @RequestMapping(path = "/menuselect/{target}")
    public String menuSelect(@PathVariable String target, Model model) {
        menu.setSelectedMenuItemByControllerUri('/' + target);
        return initViewMainContentFrame(model);
    }

    @PostMapping("/roleselection")
    public String roleSelection(@RequestParam("roleselector") String roleName, Model model) {
        menu.setRole(Role.valueOf(roleName));
        return initViewMainContentFrame(model);
    }

    private String initViewMainContentFrame(Model model) {
        model.addAttribute("menu", menu);
        return "layout/main/contentframe";
    }

    @Autowired
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

}
